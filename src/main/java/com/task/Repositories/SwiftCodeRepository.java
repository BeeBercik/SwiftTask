package com.task.Repositories;

import com.task.Model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
     List<SwiftCode> findBySwiftCodeStartingWith(String code);
}
