package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.enums.AwardStatus;

public interface AwardRepository extends JpaRepository<Award, Long> {
	
	List<Award> findByStudentIdAndStatus(Long studentId, AwardStatus status);
	
	List<Award> findByActivity(List<Activity> activities);
}
