package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.enums.ActivityStatus;

public interface ActivityPerformedRepository extends JpaRepository<ActivityPerformed, Long> {

	List<ActivityPerformed> findAllByStudentCidAndActivityStatus(String studentCid , ActivityStatus activityStatus);
}
