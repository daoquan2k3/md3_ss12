package com.learn.project.md3_ss12.repository;

import com.learn.project.md3_ss12.entity.MedicalSupply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMedicalSupplyRepository extends JpaRepository<MedicalSupply, Long> {
    Optional<MedicalSupply> findByIdAndIsDeletedFalse(Long id);
    @Query("SELECT s FROM MedicalSupply s WHERE s.isDeleted = false")
    List<MedicalSupply> getAllActiveSupplies();

    List<MedicalSupply> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name);
}
