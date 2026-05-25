package com.cesde.eventhub.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralReportResponseDTO {
    private long totalOrders;
    private long totalTicketsSold;
    private double totalRevenue;
    private List<CategoryReportDTO> categoryBreakdown;
    private List<TopEventDTO> topEvents;
}