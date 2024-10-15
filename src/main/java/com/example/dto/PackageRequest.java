package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageRequest {
     private String packageName;
     private int weight;
     private String senderId;
     private String receiverId;
}
