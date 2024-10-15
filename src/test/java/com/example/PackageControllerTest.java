package com.example;

import com.example.dto.*;
import com.example.packagecontroller.PackageController;
import com.example.packageservice.PackageService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PackageControllerTest {

    @InjectMocks
    private PackageController packageController;

    @Mock
    private PackageService packageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListReceivers_Success() {
        // Given
        List<ReceiverDetails> mockReceivers = Arrays.asList(
                new ReceiverDetails("emp001", "Alice Smith", "123 Main St, Amsterdam", "1082PP"),
                new ReceiverDetails("emp002", "Bob Johnson", "456 High St, Amsterdam", "1082QQ")
        );
        when(packageService.listReceivers()).thenReturn(mockReceivers);

        // When
        List<ReceiverDetails> receivers = packageController.listReceivers();

        // Then
        assertEquals(2, receivers.size());
        verify(packageService, times(1)).listReceivers();
    }

    @Test
    void testListReceivers_EmptyList() {
        // Given
        when(packageService.listReceivers()).thenReturn(Collections.emptyList());

        // When
        List<ReceiverDetails> receivers = packageController.listReceivers();

        // Then
        assertTrue(receivers.isEmpty());
        verify(packageService, times(1)).listReceivers();
    }

    @Test
    void testListReceivers_Exception() {
        // Given
        when(packageService.listReceivers()).thenThrow(new RuntimeException("Service Error"));

        // When / Then
        Exception exception = assertThrows(RuntimeException.class, () -> packageController.listReceivers());
        assertEquals("Service Error", exception.getMessage());
        verify(packageService, times(1)).listReceivers();
    }

    @Test
    void testSubmitPackage_Success() throws BadRequestException {
        // Given
        PackageRequest packageRequest = new PackageRequest();
        packageRequest.setSenderId("emp001");
        packageRequest.setReceiverId("emp002");
        packageRequest.setPackageName("package A");

        String generatedUUID = UUID.randomUUID().toString();
        PackageResponse mockResponse = new PackageResponse();
        mockResponse.setPackageId(generatedUUID);

        when(packageService.submitPackage(any(PackageRequest.class))).thenReturn(mockResponse);

        // When
        PackageResponse response = packageController.submitPackage(packageRequest);

        // Then
        assertNotNull(response);
        assertEquals(generatedUUID, response.getPackageId());
        verify(packageService, times(1)).submitPackage(any(PackageRequest.class));
    }

    @Test
    void testSubmitPackage_RuntimeException() throws BadRequestException {
        // Given
        PackageRequest packageRequest = new PackageRequest();
        packageRequest.setSenderId("emp001");
        packageRequest.setReceiverId("emp002");
        packageRequest.setPackageName("package A");

        when(packageService.submitPackage(any(PackageRequest.class))).thenThrow(new BadRequestException("Invalid package data"));

        // When / Then
        Exception exception = assertThrows(RuntimeException.class, () -> packageController.submitPackage(packageRequest));
        assertEquals("Invalid package data", exception.getMessage());
        verify(packageService, times(1)).submitPackage(any(PackageRequest.class));
    }

    @Test
    void testListPackages_Success() {
        // Given
        String packageId = UUID.randomUUID().toString();
        PackageDetails mockDetails = new PackageDetails("emp001", "emp002", packageId, "package A", LocalDate.now(), StatusEnum.IN_PROGRESS, null);
        when(packageService.getPackageDetails(packageId)).thenReturn(mockDetails);

        // When
        PackageDetails details = packageController.getPackageDetails(packageId);

        // Then
        assertNotNull(details);
        assertEquals(packageId, details.getPackageId());
        verify(packageService, times(1)).getPackageDetails(packageId);
    }

    @Test
    void testListPackages_Exception() {
        // Given
        String senderId = "emp001";
        String status = "IN_PROGRESS";
        when(packageService.listPackages(senderId, status)).thenThrow(new RuntimeException("Service Error"));

        // When / Then
        Exception exception = assertThrows(RuntimeException.class, () -> packageController.listPackages(senderId, status));
        assertEquals("Service Error", exception.getMessage());
        verify(packageService, times(1)).listPackages(senderId, status);
    }

    @Test
    void testGetPackageDetails_Success() {
        // Given
        String packageId = UUID.randomUUID().toString();
        PackageDetails mockDetails = new PackageDetails("emp001", "emp002", packageId, "package A", LocalDate.now(), StatusEnum.IN_PROGRESS, null);
        when(packageService.getPackageDetails(packageId)).thenReturn(mockDetails);

        // When
        PackageDetails details = packageController.getPackageDetails(packageId);

        // Then
        assertNotNull(details);
        assertEquals(packageId, details.getPackageId());
        verify(packageService, times(1)).getPackageDetails(packageId);
    }

    @Test
    void testGetPackageDetails_InvalidPackageId() {
        // Given
        String packageId = "invalid123";
        when(packageService.getPackageDetails(packageId)).thenThrow(new RuntimeException("Package not found"));

        // When / Then
        Exception exception = assertThrows(RuntimeException.class, () -> packageController.getPackageDetails(packageId));
        assertEquals("Package not found", exception.getMessage());
        verify(packageService, times(1)).getPackageDetails(packageId);
    }
}
