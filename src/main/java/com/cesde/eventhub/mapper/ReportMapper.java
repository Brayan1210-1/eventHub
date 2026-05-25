package com.cesde.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cesde.eventhub.dto.response.CategoryReportDTO;
import com.cesde.eventhub.dto.response.GeneralReportResponseDTO;
import com.cesde.eventhub.dto.response.TopEventDTO;
import com.cesde.eventhub.projections.CategorySalesProjection;
import com.cesde.eventhub.projections.TopEventProjection;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "categoryName", target = "category")
    CategoryReportDTO toCategoryDTO(CategorySalesProjection projection);

    @Mapping(source = "eventName", target = "eventName")
    TopEventDTO toTopEventDTO(TopEventProjection projection);

    List<CategoryReportDTO> toCategoryDTOList(List<CategorySalesProjection> projections);
    List<TopEventDTO> toTopEventDTOList(List<TopEventProjection> projections);

    @Mapping(source = "totalOrders", target = "totalOrders")
    @Mapping(source = "totalTickets", target = "totalTicketsSold")
    @Mapping(source = "totalRevenue", target = "totalRevenue")
    @Mapping(source = "categoryBreakdown", target = "categoryBreakdown")
    @Mapping(source = "topEvents", target = "topEvents")
    GeneralReportResponseDTO toGeneralReportDTO(
            long totalOrders, 
            long totalTickets, 
            double totalRevenue, 
            List<CategoryReportDTO> categoryBreakdown, 
            List<TopEventDTO> topEvents
    );
}
