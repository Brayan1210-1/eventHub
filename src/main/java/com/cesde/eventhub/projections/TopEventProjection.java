package com.cesde.eventhub.projections;

public interface TopEventProjection {
    String getEventName();
    Long getTicketsSold();
    Double getRevenue();
}
