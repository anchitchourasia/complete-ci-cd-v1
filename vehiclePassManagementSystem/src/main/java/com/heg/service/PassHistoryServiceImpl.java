package com.heg.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.heg.dto.PassHistoryResponseDTO;
import com.heg.mapper.PassHistoryMapper;
import com.heg.model.PassHistory;
import com.heg.model.PassRegistry;
import com.heg.repository.PassHistoryRepository;

@Service
public class PassHistoryServiceImpl implements IPassHistoryService {


    private final PassHistoryRepository repository;
    private final PassHistoryMapper mapper;

    public PassHistoryServiceImpl(
            PassHistoryRepository repository,
            PassHistoryMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }
    
    
	@Override
	public void saveHistory(	
								PassRegistry passRegistry,
								String action, 
								String remark, 
								String loginUser) {
	
		PassHistory history = new PassHistory();
		history.setPassNo(passRegistry.getId());
		
		 history.setEmpCode(loginUser);

	        history.setAction(action);

	        history.setRemark(remark);
        

	        repository.save(history);
		
		
	}


	@Override
	public List<PassHistoryResponseDTO> findHistoryByPassNo(Long passNo) {
		  List<PassHistory> historyList =
	                repository.findByPassNoOrderByDateOfEntryDesc(passNo);

	        List<PassHistoryResponseDTO> response = new ArrayList<>();

	        for (PassHistory history : historyList) {

	            response.add(mapper.toDTO(history));

	        }

	        return response;
	    }
	

}
