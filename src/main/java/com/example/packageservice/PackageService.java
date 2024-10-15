package com.example.packageservice;

import com.example.client.PackageShippingFeignClient;
import com.example.dto.*;
import com.example.validator.Validator;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PackageService{
    @Autowired
    private PackageShippingFeignClient feignClient;
    @Autowired
    private Validator validator;
    private final List<ReceiverDetails> receivers = new ArrayList<>();
    private final List<PackageDetails> packageDetailsList = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(PackageService.class);

    public PackageService(){
        receivers.add(new ReceiverDetails("emp001", "Alice Smith", "123 Main St, Amsterdam","1082PP"));
        receivers.add(new ReceiverDetails("emp002", "Bob Johnson", "456 High St, Amsterdam", "1082QQ"));
        receivers.add(new ReceiverDetails("emp003", "Charlie Brown", "789 Elm St, Amsterdam", "1082RR"));

        packageDetailsList.add(new PackageDetails("emp001", "emp003", "3f6c794b-2c96-491e-81fb-a2f9731d02a5", "package A", LocalDate.of(2024, 10, 1), StatusEnum.DELIVERED, LocalDate.of(2024, 10, 3)));
        packageDetailsList.add(new PackageDetails("emp002", "emp001", "4g6c794b-2c96-491e-81fb-a2f9731d03b4", "package B", LocalDate.of(2024, 10, 5), StatusEnum.IN_PROGRESS, null));
        packageDetailsList.add(new PackageDetails("emp001", "emp002", "5j6c794b-2c96-491e-81fb-a2f9731d05eq", "package C", LocalDate.of(2024, 10, 10), StatusEnum.IN_PROGRESS, null));
        packageDetailsList.add(new PackageDetails("emp003", "emp002", "2e6c794b-2c96-491e-81fb-a2f9731d01y2", "package D", LocalDate.of(2024, 10, 8), StatusEnum.DELIVERED, LocalDate.of(2024, 10, 12)));
    }

    public List<ReceiverDetails> listReceivers() {
        receivers.forEach(receiver -> {receiver.setStreetName("*****");
            receiver.setPostalCode("*****");});
        return receivers;
    }

    public PackageResponse submitPackage(PackageRequest packageRequest) throws BadRequestException {
        validator.validate(packageRequest);
        ReceiverDetails details = receivers.stream().filter(receiver -> (receiver.getReceiverId()).equals(packageRequest.getReceiverId())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        String packageSize = SizeEnum.getSize(packageRequest.getWeight() / 1000.0);
        SubmitPackageRequest request = SubmitPackageRequest.builder()
                .packageName(packageRequest.getPackageName())
                .packageSize(packageSize)
                .postalCode(details.getPostalCode())
                .streetName(details.getStreetName())
                .receiverName(details.getReceiverName())
                .build();

        String packageId = UUID.randomUUID().toString();
        addPackageDetails(packageRequest, packageId);

        PackageResponse response = new PackageResponse();
        response.setPackageId(packageId);
        try {
            LOGGER.info("Calling ProductOrderService's submitPackage");
            /*
             Uncomment the below inorder to use the feign client to call the PackageOrderService's submitPackage
             */
//            feignClient.submitPackage(request);
            LOGGER.info("Package ordered for shipping: " + packageRequest.getPackageName() + "Package ID: " + packageId);
            return response;
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    private void addPackageDetails(PackageRequest packageRequest, String packageId){
        PackageDetails packageDetails = new PackageDetails(packageRequest.getSenderId(), packageRequest.getReceiverId(), packageId, packageRequest.getPackageName(),LocalDate.now(), StatusEnum.IN_PROGRESS, null);
        packageDetailsList.add(packageDetails);
    }

    public List<PackageDetails> listPackages(String senderId, String status){
        try {
            validator.validate(senderId,"senderId");

            LOGGER.info("Calling ProductOrderService's listOrders");
            /*
             Uncomment the below inorder to use the feign client to call the PackageOrderService's listOrders
             */
//            List<ShippingOrderDetails> details = feignClient.listOrders(status, 0 ,10);
            return packageDetailsList.stream()
                    .filter(pkg -> pkg.getSenderId().equals(senderId) && (status == null || pkg.getStatus().name().equalsIgnoreCase(status)))
                    .collect(Collectors.toList());
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    public PackageDetails getPackageDetails(String packageId) {
        try {
            validator.validate(packageId,"packageId");

            LOGGER.info("Calling ProductOrderService's getOrderDetails");
            /*
             Uncomment the below inorder to use the feign client to call the PackageOrderService's getOrderDetails
             */
//            ShippingOrderDetails details = feignClient.getOrderDetails(packageId);
            return packageDetailsList.stream()
                    .filter(pkg -> pkg.getPackageId().equals(packageId))
                    .findFirst()
                    .orElse(null);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }
}