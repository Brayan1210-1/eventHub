package com.cesde.eventhub.projections;

import com.cesde.eventhub.enums.Category;

public interface CategorySalesProjection {
    Category getCategoryName(); 
    Long getTicketsSold();
    Double getRevenue();
}