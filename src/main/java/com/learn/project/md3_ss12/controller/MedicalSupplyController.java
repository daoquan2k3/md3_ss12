package com.learn.project.md3_ss12.controller;

import com.learn.project.md3_ss12.dto.*;
import com.learn.project.md3_ss12.entity.MedicalSupply;
import com.learn.project.md3_ss12.exception.ResourceNotFoundException;
import com.learn.project.md3_ss12.service.MedicalSupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/supplies")
@RequiredArgsConstructor
public class MedicalSupplyController {
    @Autowired
    private MedicalSupplyService medicalSupplyService;

    @PostMapping
    public ResponseEntity<ApiResponse<MedicalSupply>> createSupply(@RequestBody SupplyRequestDTO request) {
        MedicalSupply createdSupply = medicalSupplyService.createSupply(request);

        ApiResponse<MedicalSupply> response = ApiResponse.<MedicalSupply>builder()
                .code(HttpStatus.CREATED.value())
                .message("Tạo vật tư thành công.")
                .data(createdSupply)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalSupply>> updateSupply (
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) throws ResourceNotFoundException {

        MedicalSupply updatedSupply = medicalSupplyService.updateSupply(id, updates);

        ApiResponse<MedicalSupply> response = ApiResponse.<MedicalSupply>builder()
                .code(HttpStatus.OK.value())
                .message("Cập nhật vật tư thành công.")
                .data(updatedSupply)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSupply(@PathVariable Long id) {
        medicalSupplyService.deleteSupply(id);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalSupply>>> getAllSupplies() {
        List<MedicalSupply> supplies = medicalSupplyService.getAllSupplies();

        ApiResponse<List<MedicalSupply>> response = ApiResponse.<List<MedicalSupply>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy danh sách vật tư thành công.")
                .data(supplies)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicalSupply>>> searchSupplies(
            @RequestParam("name") String name) {

        List<MedicalSupply> results = medicalSupplyService.searchSuppliesByName(name);

        ApiResponse<List<MedicalSupply>> response = ApiResponse.<List<MedicalSupply>>builder()
                .code(HttpStatus.OK.value())
                .message("Kết quả tìm kiếm cho: " + name)
                .data(results)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/export")
    public ResponseEntity<ApiResponse<MedicalSupply>> exportSupply(
            @PathVariable Long id,
            @RequestBody ExportRequestDTO request) throws ResourceNotFoundException {

        MedicalSupply updatedSupply = medicalSupplyService.exportSupply(id, request.getAmount());

        ApiResponse<MedicalSupply> response = ApiResponse.<MedicalSupply>builder()
                .code(HttpStatus.OK.value())
                .message("Xuất kho thành công.")
                .data(updatedSupply)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/import")
    public ResponseEntity<ApiResponse<MedicalSupply>> importSupply(
            @PathVariable Long id,
            @RequestBody ImportRequestDTO request) {

        MedicalSupply updatedSupply = medicalSupplyService.importSupply(id, request.getAmount());

        ApiResponse<MedicalSupply> response = ApiResponse.<MedicalSupply>builder()
                .code(HttpStatus.OK.value())
                .message("Nhập kho thành công.")
                .data(updatedSupply)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/daily-export")
    public ResponseEntity<ApiResponse<List<DailyExportDTO>>> getDailyExportReport() {
        List<DailyExportDTO> report = medicalSupplyService.getDailyExportStatistics();

        ApiResponse<List<DailyExportDTO>> response = ApiResponse.<List<DailyExportDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thống kê xuất kho trong ngày thành công.")
                .data(report)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/top-export")
    public ResponseEntity<ApiResponse<TopExportDTO>> getTopExport() {
        TopExportDTO data = medicalSupplyService.getTopExportedSupply();

        ApiResponse<TopExportDTO> response = ApiResponse.<TopExportDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Lấy thông tin vật tư xuất kho nhiều nhất thành công.")
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }
}
