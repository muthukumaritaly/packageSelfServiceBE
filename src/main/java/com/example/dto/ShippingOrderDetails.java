package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingOrderDetails {
    private String packageId;
    private String packageName;
    private String packageSize;
    private String postalCode;
    private String streetName;
    private String receiverName;
    private StatusEnum orderStatus;
    private LocalDate expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
}
