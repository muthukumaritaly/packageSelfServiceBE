package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverDetails {
    private String receiverId;
    private String receiverName;
    private String streetName;
    private String postalCode;
}
