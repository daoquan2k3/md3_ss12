package com.learn.project.md3_ss12.service;

import com.learn.project.md3_ss12.dto.DailyExportDTO;
import com.learn.project.md3_ss12.dto.SupplyRequestDTO;
import com.learn.project.md3_ss12.dto.TopExportDTO;
import com.learn.project.md3_ss12.entity.MedicalSupply;
import com.learn.project.md3_ss12.entity.Transaction;
import com.learn.project.md3_ss12.exception.ResourceNotFoundException;
import com.learn.project.md3_ss12.repository.IMedicalSupplyRepository;
import com.learn.project.md3_ss12.repository.ITransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalSupplyService {
    @Autowired
    private IMedicalSupplyRepository medicalSupplyRepository;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Transactional
    public MedicalSupply createSupply(SupplyRequestDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên vật tư không được để trống.");
        }

        MedicalSupply supply = new MedicalSupply();
        supply.setName(dto.getName());
        supply.setSpecification(dto.getSpecification());
        supply.setProvider(dto.getProvider());
        supply.setUnit(dto.getUnit());

        supply.setQuantity(0);

        MedicalSupply savedSupply = medicalSupplyRepository.save(supply);

        log.info("Đã tạo mới vật tư: [{}] với ID: [{}]", savedSupply.getName(), savedSupply.getId());

        return savedSupply;
    }

    @Transactional
    public MedicalSupply updateSupply(Long id, Map<String, Object> updates) throws  ResourceNotFoundException {
        if (updates.containsKey("id") || updates.containsKey("quantity")) {
            log.warn("CẢNH BÁO: Client cố tình gửi dữ liệu cấm (id hoặc quantity) cho vật tư ID: {}", id);
            throw new IllegalArgumentException("Không được phép cập nhật trường 'id' hoặc 'quantity' qua API này.");
        }

        MedicalSupply existingSupply = medicalSupplyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với ID: " + id));

        if (updates.containsKey("name")) {
            existingSupply.setName((String) updates.get("name"));
        }
        if (updates.containsKey("specification")) {
            existingSupply.setSpecification((String) updates.get("specification"));
        }
        if (updates.containsKey("provider")) {
            existingSupply.setProvider((String) updates.get("provider"));
        }

        MedicalSupply savedSupply = medicalSupplyRepository.save(existingSupply);
        log.info("Đã cập nhật thông tin vật tư ID: {}", id);

        return savedSupply;
    }

    @Transactional
    public void deleteSupply(Long id) {
        MedicalSupply supply = medicalSupplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy vật tư hoặc vật tư đã bị xóa."));

        supply.setIsDeleted(true);
        medicalSupplyRepository.save(supply);

        log.info("Đã xóa mềm vật tư ID: [{}]", id);
    }

    public List<MedicalSupply> getAllSupplies() {
        List<MedicalSupply> supplies = medicalSupplyRepository.getAllActiveSupplies();

        log.debug("Truy vấn danh sách vật tư thành công. Số lượng bản ghi: {}", supplies.size());

        return supplies;
    }

    public List<MedicalSupply> searchSuppliesByName(String name) {
        List<MedicalSupply> results = medicalSupplyRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name);

        if (results.isEmpty()) {
            log.info("Không tìm thấy vật tư nào khớp với từ khóa: [{}]", name);
        } else {
            log.info("Tìm thấy {} kết quả cho từ khóa: [{}]", results.size(), name);
        }

        return results;
    }

    @Transactional
    public MedicalSupply exportSupply(Long id, Integer amount) throws ResourceNotFoundException {
        MedicalSupply supply = medicalSupplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư."));

        if (supply.getQuantity() < amount) {
            log.error("Thất bại khi xuất kho ID [{}]: Yêu cầu [{}], hiện có [{}]", id, amount, supply.getQuantity());
            throw new IllegalArgumentException("Số lượng tồn kho không đủ.");
        }

        supply.setQuantity(supply.getQuantity() - amount);

        Transaction tx = Transaction.builder()
                .supply(supply)
                .amount(amount)
                .type("EXPORT")
                .transactionDate(LocalDateTime.now())
                .build();
        transactionRepository.save(tx);

        return medicalSupplyRepository.save(supply);
    }

    private static final Logger historyLogger = LoggerFactory.getLogger("InventoryHistory");

    @Transactional
    public MedicalSupply importSupply(Long id, Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0.");
        }

        MedicalSupply supply = medicalSupplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy vật tư để nhập kho."));

        int oldQuantity = supply.getQuantity();

        supply.setQuantity(oldQuantity + amount);
        MedicalSupply savedSupply = medicalSupplyRepository.save(supply);

        historyLogger.info("Nhập kho ID [{}], số lượng [+{}] , tồn cũ [{}]",
                id, amount, oldQuantity);

        log.info("Đã cập nhật tồn kho vật tư ID: {} lên {}", id, savedSupply.getQuantity());

        return savedSupply;
    }

    public List<DailyExportDTO> getDailyExportStatistics() {
        long startTime = System.currentTimeMillis();
        log.info("Bắt đầu chạy thống kê xuất kho ngày: {}", LocalDate.now());

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<DailyExportDTO> report = transactionRepository.getDailyExportReport(startOfDay, endOfDay);

        long endTime = System.currentTimeMillis();
        log.info("Hoàn thành thống kê. Thời gian xử lý: {} ms. Số lượng loại vật tư đã xuất: {}",
                (endTime - startTime), report.size());

        return report;
    }

    public TopExportDTO getTopExportedSupply() {
        log.info("Bắt đầu truy vấn vật tư xuất kho nhiều nhất.");

        // Lấy Top 1 bằng cách dùng Pageable
        List<TopExportDTO> results = transactionRepository.findTopExportedSupplies(PageRequest.of(0, 1));

        if (results.isEmpty()) {
            log.warn("Thống kê thất bại: Chưa có dữ liệu giao dịch EXPORT nào.");
            throw new NoSuchElementException("Chưa có dữ liệu giao dịch để thống kê.");
        }

        TopExportDTO topSupply = results.get(0);
        log.info("Vật tư xuất nhiều nhất là: {} với tổng lượng: {}",
                topSupply.getTopSupplyName(), topSupply.getTotalExportQuantity());

        return topSupply;
    }
}
