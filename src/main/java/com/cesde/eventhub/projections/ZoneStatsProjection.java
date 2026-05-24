package com.cesde.eventhub.projections;



public interface ZoneStatsProjection {
    String getZoneName();
    Long getTicketsSold();
    Double getRevenue();
}