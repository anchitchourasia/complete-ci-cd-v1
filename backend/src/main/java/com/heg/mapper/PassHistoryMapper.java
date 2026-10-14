package com.heg.mapper;

import org.springframework.stereotype.Component;

import com.heg.dto.PassHistoryResponseDTO;
import com.heg.model.PassHistory;

@Component
public class PassHistoryMapper {
	public PassHistoryResponseDTO toDTO(PassHistory entity) {

        PassHistoryResponseDTO dto = new PassHistoryResponseDTO();

        dto.setId(entity.getId());
        dto.setPassNo(entity.getPassNo());
        dto.setDateOfEntry(entity.getDateOfEntry());
        dto.setEmpCode(entity.getEmpCode());
        dto.setAction(entity.getAction());
        dto.setRemark(entity.getRemark());

        return dto;
    }

}
