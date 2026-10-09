package com.heg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.heg.model.AppUser;



@Repository
public interface AppUserRepository
        extends JpaRepository<AppUser, String> {

    @Query(
        value = """
                SELECT *
                FROM VPMS_AUTHORITY
                WHERE USERNAME = :username
                  AND ROWNUM = 1
                """,
        nativeQuery = true
    )
    List<AppUser> findUsersByUsername(
            @Param("username") String username
    );
}