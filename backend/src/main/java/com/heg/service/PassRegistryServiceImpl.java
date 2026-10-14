












//
//
//
//
//package com.heg.service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.heg.dto.DocumentRequestDTO;
//import com.heg.dto.PassRegistryRequestDTO;
//import com.heg.dto.PassRegistryResponseDTO;
//import com.heg.dto.StatusUpdateRequestDTO;
//import com.heg.mapper.PassRegistryMapper;
//import com.heg.model.Document;
//import com.heg.model.PassRegistry;
//import com.heg.repository.DocumentRepository;
//import com.heg.repository.PassRegistryRepository;
//
//@Service
//public class PassRegistryServiceImpl implements IPassRegistryService {
//
//    private final PassRegistryRepository repository;
//    private final DocumentRepository documentRepository;
//    private final IFileStorageService fileStorageService;
//    private final PassRegistryMapper mapper;
//    private final IPassHistoryService historyService;
//
//    public PassRegistryServiceImpl(
//            PassRegistryRepository repository,
//            DocumentRepository documentRepository,
//            PassRegistryMapper mapper,
//            IPassHistoryService historyService,
//            IFileStorageService fileStorageService) {
//
//        this.repository = repository;
//        this.documentRepository = documentRepository;
//        this.mapper = mapper;
//        this.historyService = historyService;
//        this.fileStorageService = fileStorageService;
//    }
//
//    @Override
//    public PassRegistryResponseDTO save(
//            PassRegistryRequestDTO request,
//            Map<String, MultipartFile> files) throws IOException {
//
//        PassRegistry entity = mapper.toEntity(request);
//
//        List<Document> documents =
//                mapper.toDocumentEntityList(request.getDocuments(), entity);
//
//        for (int i = 0; i < documents.size(); i++) {
//
//            Document document = documents.get(i);
//            DocumentRequestDTO dto = request.getDocuments().get(i);
//
//            MultipartFile file = files.get(dto.getFileKey());
//
//            if (file != null && !file.isEmpty()) {
//                String generatedName = fileStorageService.upload(file);
//                document.setFileName(generatedName);
//            }
//        }
//
//        entity.setDocuments(documents);
//
//        PassRegistry saved = repository.save(entity);
//
//        historyService.saveHistory(
//                saved,
//                request.getStatus(),
//                "Pass created successfully",
//                saved.getEmployeeNo());
//
//        System.out.println(entity);
//
//        return mapper.toResponseDTO(saved);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<PassRegistryResponseDTO> getAll() {
//
//        List<PassRegistry> entities = repository.findAll();
//        List<PassRegistryResponseDTO> response = new ArrayList<>();
//
//        for (PassRegistry entity : entities) {
//            response.add(mapper.toResponseDTO(entity));
//        }
//
//        return response;
//    }
//
//    private boolean isEditableStatus(String status) {
//
//        if (status == null) {
//            return false;
//        }
//
//        return status.equalsIgnoreCase("DRAFT")
//                || status.equalsIgnoreCase("SAVE")
//                || status.equalsIgnoreCase("SAVED")
//                || status.equalsIgnoreCase("SUBMITTED");
//    }
//
//    @Override
//    @Transactional
//    public PassRegistryResponseDTO update(
//            Long id,
//            PassRegistryRequestDTO request,
//            Map<String, MultipartFile> files) throws IOException {
//
//        PassRegistry entity = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Pass Registry not found"));
//
//        mapper.updateEntity(entity, request);
//
//        entity.getDocuments().clear();
//
//        if (request.getDocuments() != null) {
//
//            for (DocumentRequestDTO dto : request.getDocuments()) {
//
//                Document document = new Document();
//
//                document.setDocumentType(dto.getDocumentType());
//                document.setDocumentNo(dto.getDocumentNo());
//                document.setExpiryDate(dto.getExpiryDate());
//
//                MultipartFile file = files != null
//                        ? files.get(dto.getFileKey())
//                        : null;
//
//                if (file != null && !file.isEmpty()) {
//                    String generatedName = fileStorageService.upload(file);
//                    document.setFileName(generatedName);
//                } else if (dto.getFileName() != null && !dto.getFileName().trim().isEmpty()) {
//                    document.setFileName(dto.getFileName().trim());
//                }
//
//                document.setPassRegistry(entity);
//                entity.getDocuments().add(document);
//            }
//        }
//
//        PassRegistry saved = repository.save(entity);
//
//        historyService.saveHistory(
//                saved,
//                request.getStatus(),
//                request.getRemark() != null
//                        ? request.getRemark()
//                        : "Request Updated Successfully",
//                saved.getEmployeeNo()
//        );
//
//        return mapper.toResponseDTO(saved);
//    }
//
//    @Override
//    @Transactional
//    public PassRegistryResponseDTO updateStatus(
//            Long id,
//            StatusUpdateRequestDTO request) {
//
//        PassRegistry entity =
//                repository.findById(id)
//                        .orElseThrow(() -> new RuntimeException("Pass Registry not found"));
//
//        entity.setReqStatus(request.getStatus());
//
//        PassRegistry saved = repository.save(entity);
//
//        historyService.saveHistory(
//                saved,
//                request.getStatus(),
//                request.getRemark(),
//                request.getEnterBy()
//        );
//
//        return mapper.toResponseDTO(saved);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public byte[] getDocumentBytes(Long documentId) throws IOException {
//        Document document = documentRepository.findById(documentId)
//                .orElseThrow(() -> new RuntimeException("Document not found"));
//        return fileStorageService.download(document.getFileName());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public String getDocumentFileName(Long documentId) {
//        Document document = documentRepository.findById(documentId)
//                .orElseThrow(() -> new RuntimeException("Document not found"));
//        return document.getFileName();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public String getDocumentContentType(Long documentId) throws IOException {
//        Document document = documentRepository.findById(documentId)
//                .orElseThrow(() -> new RuntimeException("Document not found"));
//        return fileStorageService.getContentType(document.getFileName());
//    }
//}





package com.heg.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.heg.dto.DocumentRequestDTO;
import com.heg.dto.PassRegistryListDTO;
import com.heg.dto.PassRegistryRequestDTO;
import com.heg.dto.PassRegistryResponseDTO;
import com.heg.dto.StatusUpdateRequestDTO;
import com.heg.mapper.PassRegistryMapper;
import com.heg.model.Document;
import com.heg.model.PassRegistry;
import com.heg.repository.DocumentRepository;
import com.heg.repository.PassRegistryRepository;

@Service
public class PassRegistryServiceImpl implements IPassRegistryService {

    private final PassRegistryRepository repository;
    private final DocumentRepository documentRepository;
    private final IFileStorageService fileStorageService;
    private final PassRegistryMapper mapper;
    private final IPassHistoryService historyService;

    public PassRegistryServiceImpl(
            PassRegistryRepository repository,
            DocumentRepository documentRepository,
            PassRegistryMapper mapper,
            IPassHistoryService historyService,
            IFileStorageService fileStorageService) {

        this.repository = repository;
        this.documentRepository = documentRepository;
        this.mapper = mapper;
        this.historyService = historyService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public PassRegistryResponseDTO save(
            PassRegistryRequestDTO request,
            Map<String, MultipartFile> files) throws IOException {

        PassRegistry entity = mapper.toEntity(request);

        List<Document> documents =
                mapper.toDocumentEntityList(request.getDocuments(), entity);

        for (int i = 0; i < documents.size(); i++) {

            Document document = documents.get(i);
            DocumentRequestDTO dto = request.getDocuments().get(i);

            MultipartFile file = files.get(dto.getFileKey());

            if (file != null && !file.isEmpty()) {
                String generatedName = fileStorageService.upload(file);
                document.setFileName(generatedName);
            }
        }

        entity.setDocuments(documents);

        PassRegistry saved = repository.save(entity);

        historyService.saveHistory(
                saved,
                request.getStatus(),
                "Pass created successfully",
                saved.getEnterBy());

        System.out.println(entity);

        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassRegistryResponseDTO> getAll() {

        List<PassRegistry> entities = repository.findAll();
        List<PassRegistryResponseDTO> response = new ArrayList<>();

        for (PassRegistry entity : entities) {
            response.add(mapper.toResponseDTO(entity));
        }

        return response;
    }

    private boolean isEditableStatus(String status) {

        if (status == null) {
            return false;
        }

        return status.equalsIgnoreCase("DRAFT")
                || status.equalsIgnoreCase("SAVE")
                || status.equalsIgnoreCase("SAVED")
                || status.equalsIgnoreCase("SUBMITTED");
    }

    @Override    
    @Transactional
    public PassRegistryResponseDTO update(
            Long id,
            PassRegistryRequestDTO request,
            Map<String, MultipartFile> files) throws IOException {

        //=====================================================
        // Load Existing Pass
        //=====================================================
        PassRegistry entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pass Registry not found"));

        //=====================================================
        // Update Parent Fields
        //=====================================================
        mapper.updateEntity(entity, request);

        //=====================================================
        // Backup Existing File Names
        //=====================================================
        Map<String, String> oldFiles = entity.getDocuments()
                .stream()
                .collect(Collectors.toMap(
                        Document::getDocumentType,
                        Document::getFileName,
                        (oldValue, newValue) -> oldValue
                ));

        //=====================================================
        // Remove Existing Documents
        //=====================================================
        entity.getDocuments().clear();

        //=====================================================
        // Add New Documents
        //=====================================================
        if (request.getDocuments() != null) {

            for (DocumentRequestDTO dto : request.getDocuments()) {

                Document document = new Document();

                document.setDocumentType(dto.getDocumentType());
                document.setDocumentNo(dto.getDocumentNo());                
                document.setExpiryDate(dto.getExpiryDate());

                MultipartFile file = null;

                if (files != null
                        && dto.getFileKey() != null
                        && !dto.getFileKey().isBlank()) {

                    file = files.get(dto.getFileKey());
                }

                //=============================================
                // New File Uploaded
                //=============================================
                if (file != null && !file.isEmpty()) {

                    String generatedFileName = fileStorageService.upload(file);
                    document.setFileName(generatedFileName);

                }

                //=============================================
                // Keep Existing File
                //=============================================
                else {

                    String existingFile = oldFiles.get(dto.getDocumentType());

                    if (existingFile != null && !existingFile.isBlank()) {

                        document.setFileName(existingFile);

                    } else {

                        throw new RuntimeException(
                                "File is required for document type : "
                                + dto.getDocumentType());

                    }
                }

                document.setPassRegistry(entity);
                entity.getDocuments().add(document);
            }
        }

        //=====================================================
        // Save Pass
        //=====================================================
        PassRegistry saved = repository.save(entity);

        //=====================================================
        // Save History
        //=====================================================
        historyService.saveHistory(
                saved,
                request.getStatus(),
                request.getRemark() != null
                        ? request.getRemark()
                        : "Request Updated Successfully",
                saved.getEnterBy());

        //=====================================================
        // Response
        //=====================================================
        return mapper.toResponseDTO(saved);
    }
    @Override
    @Transactional
    public PassRegistryResponseDTO updateStatus(
            Long id,
            StatusUpdateRequestDTO request) {

        PassRegistry entity =
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pass Registry not found"));

        entity.setReqStatus(request.getStatus());

        PassRegistry saved = repository.save(entity);

        historyService.saveHistory(
                saved,
                request.getStatus(),
                request.getRemark(),
                request.getEnterBy()
        );

        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getDocumentBytes(Long documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return fileStorageService.download(document.getFileName());
    }

    @Override
    @Transactional(readOnly = true)
    public String getDocumentFileName(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return document.getFileName();
    }

    @Override
    @Transactional(readOnly = true)
    public String getDocumentContentType(Long documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        return fileStorageService.getContentType(document.getFileName());
    }
    
    
    
    
    @Override
    public List<PassRegistryListDTO> getPassRegistryDetails() {

        List<Object[]> list = repository.getPassRegistryDetails();

        List<PassRegistryListDTO> dtoList = new ArrayList<>();

        for (Object[] obj : list) {

        	
        	    
        	PassRegistryListDTO dto = new PassRegistryListDTO();

            dto.setId(obj[0] == null ? null : ((Number) obj[0]).longValue());
            dto.setPassNo(obj[1] == null ? null : obj[1].toString());
            dto.setVehicleNo(obj[2] == null ? null : obj[2].toString());
            dto.setVehicleType(obj[3] == null ? null : obj[3].toString());
            dto.setEmpType(obj[4] == null ? null : obj[4].toString());
            dto.setEmployeeNo(obj[5] == null ? null : obj[5].toString());
            dto.setStatus(obj[6] == null ? null : obj[6].toString());

            dto.setName(obj[7] == null ? null : obj[7].toString());
            dto.setDeptCode(obj[8] == null ? null : obj[8].toString());
            dto.setDeptName(obj[9] == null ? null : obj[9].toString());
            dto.setContractorCode(obj[10] == null ? null : obj[10].toString());
            dto.setContractorName(obj[11] == null ? null : obj[11].toString());
            dto.setAadhaarNo(obj[12] == null ? null : obj[12].toString());
            dto.setMobileNo(obj[13] == null ? null : obj[13].toString());
            dto.setEntryDate(obj[14] == null ? null : obj[14].toString());

            dtoList.add(dto);
        }

        return dtoList;
    }
    
    
    public PassRegistryResponseDTO getPassById(Long id){

        PassRegistry entity = repository.findById(id)
                .orElseThrow(
                  ()->new RuntimeException("Pass not found")
                );


        return mapper.toResponseDTO(entity);
    }
}