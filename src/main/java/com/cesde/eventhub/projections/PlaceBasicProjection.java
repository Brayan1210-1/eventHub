package com.cesde.eventhub.projections;

public interface PlaceBasicProjection {
    Long getId();
    String getName();
    String getCity();
    Integer getTotalZones();
    Integer getTotalCapacityZones();
}