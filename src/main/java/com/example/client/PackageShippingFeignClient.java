package com.example.client;

import com.example.config.FeignConfig;
import com.example.dto.ShippingOrderDetails;
import com.example.dto.SubmitPackageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name = "${feign.client.name}", url = "${feign.client.url}", configuration = FeignConfig.class)
public interface PackageShippingFeignClient {
    @PostMapping("/shippingOrders")
    void submitPackage(@RequestBody SubmitPackageRequest request);

    @GetMapping("/shippingOrders")
    List<ShippingOrderDetails> listOrders(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit);

    @GetMapping("/shippingOrders/{orderId}")
    ShippingOrderDetails getOrderDetails(@PathVariable("orderId") String orderId);
}

