package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Student getOneBycId(String cId);

	int countByEmail(String email);

	int countByUsername(String username);

	List<Student> findByName(String name);

	Student findBycId(String cId);

	Student findByMobileNumber(String mobileNumber);

	Student findByUsername(String username);
}
