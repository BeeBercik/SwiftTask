package com.task.repositories;

import com.task.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
     SwiftCode findSwiftCodeBySwiftCode(String swiftCode);
     List<SwiftCode> findBySwiftCodeStartingWith(String swiftCode);
     List<SwiftCode> findByCountryISO2(String isoCode);
     boolean existsBySwiftCode(String swiftCode);
}
