package com.heg.service;

import java.util.List;

import com.heg.dto.PassHistoryResponseDTO;
import com.heg.model.PassRegistry;


public interface IPassHistoryService {
	
	
	void saveHistory(
            PassRegistry passRegistry,
            String action,
            String remark,
            String loginUser);
	
	
	 List<PassHistoryResponseDTO> findHistoryByPassNo(Long passNo);	
}
