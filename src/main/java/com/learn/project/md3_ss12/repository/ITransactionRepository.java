package com.learn.project.md3_ss12.repository;

import com.learn.project.md3_ss12.dto.DailyExportDTO;
import com.learn.project.md3_ss12.dto.TopExportDTO;
import com.learn.project.md3_ss12.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT new com.learn.project.md3_ss12.dto.DailyExportDTO(t.supply.id, t.supply.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.type = 'EXPORT' " +
            "AND t.transactionDate >= :startOfDay " +
            "AND t.transactionDate <= :endOfDay " +
            "GROUP BY t.supply.id, t.supply.name")
    List<DailyExportDTO> getDailyExportReport(LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT new com.learn.project.md3_ss12.dto.TopExportDTO(t.supply.id, t.supply.name, SUM(t.amount)) " +
            "FROM Transaction t " +
            "WHERE t.type = 'EXPORT' " +
            "GROUP BY t.supply.id, t.supply.name " +
            "ORDER BY SUM(t.amount) DESC")
    List<TopExportDTO> findTopExportedSupplies(Pageable pageable);
}
