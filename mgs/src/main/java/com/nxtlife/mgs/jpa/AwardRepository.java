package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.school.Award;

public interface AwardRepository extends JpaRepository<Award, Long> {

	Award getOneByCidAndActiveTrue(String cid);

	Award getOneByNameAndActiveTrue(String name);

	List<Award> findByTeacherSchoolCidAndActiveTrue(String schoolCid);
}
