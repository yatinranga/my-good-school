package com.nxtlife.mgs.jpa;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.mgs.util.SequenceGenerator;

public interface SequenceGeneratorRepo extends JpaRepository<SequenceGenerator, Long> {

	@Query(value="select s.sequence from SequenceGenerator s where s.userType = :userType ")
	Long findSequenceByUserType(String userType);
	
	@Modifying
	@Query(value = "update SequenceGenerator s set s.sequence = :sequence where s.userType = :userType")
	int updateSequenceByUserType(Long sequence , String userType);
}
