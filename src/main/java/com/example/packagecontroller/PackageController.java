package com.example.packagecontroller;

import com.example.dto.*;
import com.example.packageservice.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/packages")
public class PackageController {
    @Autowired
    private PackageService packageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PackageController.class);
    @Operation(summary = "List available receivers", description = "Retrieve a list of hardcoded available receivers with masked address details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of receivers returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceiverDetails.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/listReceivers")
    public List<ReceiverDetails> listReceivers(){
        try {
            LOGGER.info("Started Listing Receivers");
            return packageService.listReceivers();
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        finally {
            LOGGER.info("Ended Listing Receivers");
        }
    }
    @Operation(summary = "Submit a package for delivery", description = "Submit a package with details such as package name, weight, receiver, and sender.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Package submitted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PackageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/submitPackage")
    public PackageResponse submitPackage( @Parameter(description = "Package request details", required = true)
                                              @RequestBody PackageRequest packageRequest){
        try {
            LOGGER.info("Started Submitting Package");
            return packageService.submitPackage(packageRequest);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        finally {
            LOGGER.info("Ended Submitting Package");

        }
    }
    @Operation(summary = "List all packages for a sender", description = "List all package details for a given sender. Optionally filter by package status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of packages returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PackageDetails.class))),
            @ApiResponse(responseCode = "400", description = "Invalid sender ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/listPackages/{senderId}")
    public List<PackageDetails> listPackages(@Parameter(description = "Sender ID", required = true)
                                                 @PathVariable String senderId,@Parameter(description = "Filter by package status", required = false)
                                                                                @RequestParam(required = false) String status){
        try {
            LOGGER.info("Started Listing Packages");
            return packageService.listPackages(senderId, status);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        finally {
            LOGGER.info("Ended Listing Packages");

        }
    }
    @Operation(summary = "Get details of an individual package", description = "Retrieve the details of a specific package, including registration date, package status, and expected delivery date if applicable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Package details returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PackageDetails.class))),
            @ApiResponse(responseCode = "400", description = "Invalid package ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getPackageDetails/{packageId}")
    public PackageDetails getPackageDetails(@Parameter(description = "Package ID", required = true)
                                                @PathVariable String packageId){
        try {
            LOGGER.info("Started Getting Package Details");
            return packageService.getPackageDetails(packageId);
        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        finally {
            LOGGER.info("Ended Getting Package Details");

        }
    }
}
