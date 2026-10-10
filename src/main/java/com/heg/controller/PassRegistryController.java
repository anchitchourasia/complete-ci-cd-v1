package com.heg.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.heg.dto.PassExpiryResponseDTO;
import com.heg.dto.PassRegistryListDTO;
import com.heg.dto.PassRegistryRequestDTO;
import com.heg.dto.PassRegistryResponseDTO;
import com.heg.dto.StatusUpdateRequestDTO;
import com.heg.service.IPassRegistryService;
import com.heg.service.PassExpiryService;

@RestController
@RequestMapping("/api/passes")
public class PassRegistryController {

    @Autowired
    private IPassRegistryService service;
    
    @Autowired
    private PassExpiryService passExpiryService;

    
    
    private static final String API_KEY = "VPMS_SECRET_KEY_2026";

    @Value("${app.upload.dir}")
    private String uploadDir;
    
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hii VPMS");
    }

    @PostMapping(
        value = "/save",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PassRegistryResponseDTO> save(
            @RequestPart("request") PassRegistryRequestDTO request,
            MultipartHttpServletRequest multipartRequest)
            throws IOException {

        System.out.println(request);
        System.out.println("Status : " + request.getStatus());
        System.out.println("Contractor Code : " + request.getContractorCode());

        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        System.out.println("Files received : " + files.keySet());

        return ResponseEntity.ok(service.save(request, files));
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PassRegistryResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("request") PassRegistryRequestDTO request,
            MultipartHttpServletRequest multipartRequest)
            throws IOException {

        Map<String, MultipartFile> files = multipartRequest.getFileMap();

        System.out.println("========== FILES ==========");

        files.forEach((k, v) -> {
            System.out.println(k + " -> " + v.getOriginalFilename());
        });

        return ResponseEntity.ok(
                service.update(id, request, files)
        );
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllPasses(
            @RequestHeader(value = "x-api-key", required = false) String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).body("Invalid API Key");
        }

        List<PassRegistryResponseDTO> passes = service.getAll();
        return ResponseEntity.ok(passes);
    }
    
    
    
    @GetMapping("/listV1")
    public List<PassRegistryListDTO> getAllPasses() {
        return service.getPassRegistryDetails();
    }
    
    
    @GetMapping("/list/{id}")
    public ResponseEntity<?> getPassById(
            @PathVariable Long id,
            @RequestHeader(value = "x-api-key", required = false) String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity.status(401).body("Invalid API Key");
        }

        PassRegistryResponseDTO pass = service.getPassById(id);
        return ResponseEntity.ok(pass);
    }
    

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequestDTO request) {

        return ResponseEntity.ok(service.updateStatus(id, request));
    }
    
    
    
   
    
    @GetMapping("/{id}")
    public PassRegistryResponseDTO getPassById(
            @PathVariable Long id){

        return service.getPassById(id);
    }
    
    
    @GetMapping("/documents/download/{fileName:.+}")
    public ResponseEntity<?> downloadDocument(
            @PathVariable String fileName,
            @RequestHeader(value = "x-api-key", required = false) String apiKey) {

        if (!API_KEY.equals(apiKey)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid API Key");
        }

        try {
            // Network share folder from application.properties
            Path uploadDirectory = Paths.get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            // Resolve only within VPMS upload directory
            Path filePath = uploadDirectory
                    .resolve(fileName)
                    .normalize();

            // Security: blocks filename values such as ../../application.properties
            if (!filePath.startsWith(uploadDirectory)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Invalid file name");
            }

            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Document not found: " + fileName);
            }

            Resource resource = new UrlResource(filePath.toUri());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            String downloadedFileName = filePath.getFileName().toString();

            ContentDisposition disposition = ContentDisposition
                    .attachment()
                    .filename(downloadedFileName, StandardCharsets.UTF_8)
                    .build();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(Files.size(filePath))
                    .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                    .body(resource);

        } catch (IOException exception) {
            exception.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to download document");
        }
    }
    
    
    
    @GetMapping("/expiry-details")
    public ResponseEntity<List<PassExpiryResponseDTO>> getPassExpiryDetails() {

        List<PassExpiryResponseDTO> response =
                passExpiryService.getPassExpiryDetails();

        return ResponseEntity.ok(response);
    }
}