package com.example.dto;

public enum SizeEnum {
    S(0, 0.2),
    M(0.2, 1.0),
    L(1.0, 10.0),
    XL(10.0, Double.MAX_VALUE);

    private final double minWeight;
    private final double maxWeight;

    SizeEnum(double minWeight, double maxWeight) {
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
    }

    public static String getSize(double weight) {
        for (SizeEnum size : values()) {
            if (weight >= size.minWeight && weight < size.maxWeight) {
                return size.name();
            }
        }
        throw new IllegalArgumentException("Invalid weight: " + weight);
    }
}

