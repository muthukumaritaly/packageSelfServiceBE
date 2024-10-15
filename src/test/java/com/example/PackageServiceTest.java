package com.example;

import com.example.dto.*;
import com.example.packageservice.PackageService;
import com.example.validator.Validator;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PackageServiceTest {

    @InjectMocks
    private PackageService packageService;

    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListReceivers_Success() {
        // When
        List<ReceiverDetails> receivers = packageService.listReceivers();

        // Then
        System.out.println(receivers);
        assertEquals(3, receivers.size());
        assertEquals("*****", receivers.get(0).getStreetName());
        assertEquals("*****", receivers.get(0).getPostalCode());
    }
    @Test
    void testListReceivers_Failure() {
        List<ReceiverDetails> receivers = packageService.listReceivers();
        assertFalse(receivers.isEmpty());
    }

    @Test
    void testSubmitPackage_Success() throws BadRequestException {
        // Given
        PackageRequest packageRequest = new PackageRequest();
        packageRequest.setSenderId("emp001");
        packageRequest.setReceiverId("emp002");
        packageRequest.setPackageName("package A");
        packageRequest.setWeight(1500);

        doNothing().when(validator).validate(packageRequest);

        // When
        PackageResponse response = packageService.submitPackage(packageRequest);

        // Then
        assertNotNull(response);
        assertNotNull(response.getPackageId());
        assertTrue(UUID.fromString(response.getPackageId()) instanceof UUID);
    }

    @Test
    void testSubmitPackage_ReceiverNotFound() throws BadRequestException {
        // Given
        PackageRequest packageRequest = new PackageRequest();
        packageRequest.setSenderId("emp001");
        packageRequest.setReceiverId("invalidId");
        packageRequest.setPackageName("package A");
        packageRequest.setWeight(1500);

        doNothing().when(validator).validate(packageRequest);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                packageService.submitPackage(packageRequest));
        assertEquals("Receiver not found", exception.getMessage());
    }

    @Test
    void testSubmitPackage_ValidationFailure() throws BadRequestException {
        // Given
        PackageRequest packageRequest = new PackageRequest();

        doThrow(new BadRequestException("Validation failed")).when(validator).validate(packageRequest);

        // When & Then
        assertThrows(BadRequestException.class, () -> packageService.submitPackage(packageRequest));
    }

    @Test
    void testListPackages_Success() throws BadRequestException {
        // Given
        String senderId = "emp001";

        doNothing().when(validator).validate(senderId, "senderId");

        // When
        List<PackageDetails> packages = packageService.listPackages(senderId, null);

        // Then
        assertFalse(packages.isEmpty());
        assertEquals(senderId, packages.get(0).getSenderId());
    }

    @Test
    void testListPackages_StatusFilter() throws BadRequestException {
        // Given
        String senderId = "emp001";
        String status = "IN_PROGRESS";

        doNothing().when(validator).validate(senderId, "senderId");

        // When
        List<PackageDetails> packages = packageService.listPackages(senderId, status);

        // Then
        assertFalse(packages.isEmpty());
        assertTrue(packages.stream().allMatch(pkg -> pkg.getStatus().name().equalsIgnoreCase(status)));
    }

    @Test
    void testListPackages_InvalidSender() throws BadRequestException {
        // Given
        String invalidSenderId = "unknown";

        doNothing().when(validator).validate(invalidSenderId, "senderId");

        // When
        List<PackageDetails> packages = packageService.listPackages(invalidSenderId, null);

        // Then
        assertTrue(packages.isEmpty());
    }

    @Test
    void testGetPackageDetails_Success() throws BadRequestException {
        // Given
        String packageId = "3f6c794b-2c96-491e-81fb-a2f9731d02a5";

        doNothing().when(validator).validate(packageId, "packageId");

        // When
        PackageDetails details = packageService.getPackageDetails(packageId);

        // Then
        assertNotNull(details);
        assertEquals(packageId, details.getPackageId());
    }

    @Test
    void testGetPackageDetails_InvalidPackageId() throws BadRequestException {
        // Given
        String invalidPackageId = "invalid-id";

        doNothing().when(validator).validate(invalidPackageId, "packageId");

        // When & Then
        assertNull(packageService.getPackageDetails(invalidPackageId));
    }
}
