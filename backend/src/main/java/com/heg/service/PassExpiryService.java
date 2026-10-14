package com.heg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.heg.dto.PassExpiryResponseDTO;
import com.heg.repository.PassExpiryRepository;

@Service
public class PassExpiryService {
	 private final PassExpiryRepository passExpiryRepository;

	    public PassExpiryService(PassExpiryRepository passExpiryRepository) {
	        this.passExpiryRepository = passExpiryRepository;
	    }

	    public List<PassExpiryResponseDTO> getPassExpiryDetails() {
	        return passExpiryRepository.getPassExpiryDetails();
	    }
}
