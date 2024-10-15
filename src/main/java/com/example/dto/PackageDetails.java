package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageDetails {
    private String senderId;
    private String receiverId;
    private String packageId;
    private String packageName;
    private LocalDate dateOfRegistration;
    private StatusEnum status;
    private LocalDate dateOfReceipt;
}
