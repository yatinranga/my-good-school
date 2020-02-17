package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	Student getOneByCid(String cid);

	int countByEmail(String email);

	int countByUsername(String username);

	List<Student> findAllBySchoolCid(String schooolCid);

	List<Student> findAllBySchoolCidAndGradeCid(String schooolCid, String gradeCid);

	Student findById(Long id);

	void deleteByCid(String cid);

	Student findByCidAndActiveTrue(String cid);

	List<Student> findAllBySchoolCidAndActiveTrue(String schoolCid);

	List<Student> findAllByActiveTrue();

	Student findByUsernameAndActiveTrue(String username);

	Student findByMobileNumberAndActiveTrue(String mobileNumber);

	Student findByIdAndActiveTrue(Long id);

	List<Student> findByNameAndActiveTrue(String name);

	Student findByCid(String cid);

}
