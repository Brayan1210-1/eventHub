package com.cesde.eventhub.dto.response;


import com.cesde.eventhub.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReportDTO {
    private Category category; 
    private long ticketsSold;
    private double revenue;
}