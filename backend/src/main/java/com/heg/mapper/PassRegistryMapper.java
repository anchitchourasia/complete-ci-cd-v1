package com.heg.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Component;

import com.heg.dto.DocumentRequestDTO;
import com.heg.dto.DocumentResponseDTO;
import com.heg.dto.PassRegistryRequestDTO;
import com.heg.dto.PassRegistryResponseDTO;
import com.heg.model.Document;
import com.heg.model.PassRegistry;

@Component
public class PassRegistryMapper {
	
	
	public PassRegistry toEntity(PassRegistryRequestDTO dto) {

		
		PassRegistry entity = new PassRegistry();

		entity.setVehicleNo(dto.getVehicleNo());
		entity.setVehicleType(dto.getVehicleType());
		entity.setBrandModel(dto.getBrandModel());		
		entity.setEmployeeNo(dto.getEmployeeNo());
		entity.setEmpType(dto.getEmpType());
		entity.setContractorCode(dto.getContractorCode());
		entity.setGateNo(dto.getGateNo());
		entity.setParkingToBeUsed(dto.getParkingToBeUsed());
		entity.setReqStatus(dto.getStatus());
		entity.setEnterBy(dto.getEnterBy());
		entity.setEnterDate(LocalDate.now());
		entity.setPassNo(dto.getPassNo());
		

		return entity;
	}

	public List<Document> toDocumentEntityList(List<DocumentRequestDTO> dtos, PassRegistry passRegistry) {

		List<Document> documents = new ArrayList<>();

		if (dtos == null) {
			return documents;
		}

		for (DocumentRequestDTO dto : dtos) {

			Document document = new Document();

			document.setDocumentType(dto.getDocumentType());
			document.setDocumentNo(dto.getDocumentNo());			
			document.setExpiryDate(dto.getExpiryDate());

			document.setPassRegistry(passRegistry);

			documents.add(document);
		}

		return documents;
	}

	public PassRegistryResponseDTO toResponseDTO(PassRegistry entity) {

		PassRegistryResponseDTO dto = new PassRegistryResponseDTO();

		dto.setId(entity.getId());
		dto.setVehicleNo(entity.getVehicleNo());
		dto.setVehicleType(entity.getVehicleType());
		dto.setBrandModel(entity.getBrandModel());		
		dto.setEmployeeNo(entity.getEmployeeNo());
		dto.setEmpType(entity.getEmpType());
		dto.setContractorCode(entity.getContractorCode());
		dto.setGateNo(entity.getGateNo());
		dto.setParkingToBeUsed(entity.getParkingToBeUsed());
		dto.setReqStatus(entity.getReqStatus());
		dto.setPassNo(entity.getPassNo());
		dto.setEnterBy(entity.getEnterBy());
		dto.setEnterDate(entity.getEnterDate());
		
		

		List<DocumentResponseDTO> documentResponses = new ArrayList<>();

		for (Document document : entity.getDocuments()) {

			DocumentResponseDTO documentDTO = new DocumentResponseDTO();

			documentDTO.setId(document.getId());
			documentDTO.setDocumentType(document.getDocumentType());
			documentDTO.setDocumentNo(document.getDocumentNo());
						documentDTO.setExpiryDate(document.getExpiryDate());
			documentDTO.setFileName(document.getFileName());

			documentResponses.add(documentDTO);
		}

		dto.setDocuments(documentResponses);

		return dto;
	}

	public void updateEntity(PassRegistry entity, PassRegistryRequestDTO dto) {

		entity.setVehicleNo(dto.getVehicleNo());
		entity.setVehicleType(dto.getVehicleType());
		entity.setBrandModel(dto.getBrandModel());		
		entity.setEmployeeNo(dto.getEmployeeNo());
		entity.setEmpType(dto.getEmpType());
		entity.setContractorCode(dto.getContractorCode());
		entity.setGateNo(dto.getGateNo());
		entity.setParkingToBeUsed(dto.getParkingToBeUsed());
		entity.setReqStatus(dto.getStatus());
		entity.setPassNo(dto.getPassNo());
	}

}
