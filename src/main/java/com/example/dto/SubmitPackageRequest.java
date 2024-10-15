package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitPackageRequest {
    private String packageName;
    private String postalCode;
    private String streetName;
    private String receiverName;
    private String packageSize;
}
