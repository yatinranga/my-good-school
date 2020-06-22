package com.nxtlife.mgs.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.mgs.entity.school.AwardType;

public interface AwardTypeRepository extends JpaRepository<AwardType, Long>{

	public AwardType getByNameAndActiveTrue(String name);
	
	@Query(value = "select aw.name from AwardType aw where aw.school.id = ?1 and aw.active = true")
	public List<String> findAllNameBySchoolIdAndActiveTrue(Long schoolId);

	public boolean existsByNameAndSchoolId(String name, Long gettSchoolId);

	public int deleteByNameAndSchoolId(String name, Long schoolId);
	
}
