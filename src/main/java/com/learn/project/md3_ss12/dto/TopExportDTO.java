package com.learn.project.md3_ss12.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopExportDTO {
    private Long supplyId;
    private String topSupplyName;
    private Long totalExportQuantity;
}
