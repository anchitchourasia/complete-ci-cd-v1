package com.heg.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.heg.model.PassRegistry;

@Repository
public interface PassRegistryRepository extends JpaRepository<PassRegistry, Long> {
    
	  @Query(value = """
		        SELECT
		            a.ID,
		            a.PASS_NO,
		            a.VEHICLE_NO,
		            a.VEHICLE_TYPE,
		            a.EMP_TYPE,
		            a.EMPLOYEE_NO,
		            a.STATUS,
		            b.NAME,
		            b.DEPT_CODE,
		            b.DEPT_NAME,
		            b.CONTRACTOR_CODE,
		            b.CONTRACTOR_NAME,
		            b.AADHAAR_NO
		        FROM CW.VPMS_PASS_REGISTRY a
		        JOIN ATTD.VPMS_EMPLOYEE b
		          ON a.EMPLOYEE_NO = b.ID
		         AND a.EMP_TYPE = b.EMP_TYPE
		        """, nativeQuery = true)
	  
		    List<Object[]> getPassRegistryDetails();
    
}