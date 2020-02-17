package com.nxtlife.mgs.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;

public interface ActivityPerformedRepository extends JpaRepository<ActivityPerformed, Long> {

	List<ActivityPerformed> findAllByStudentCidAndActivityStatusAndActiveTrue(String studentCid , ActivityStatus activityStatus);
	
	List<ActivityPerformed> findAllByStudentCidAndActivityCidAndActivityStatusAndActiveTrue(String studentCid ,String activityCid, ActivityStatus activityStatus);
	
	List<ActivityPerformed> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(String studentCid ,FourS fourS, ActivityStatus activityStatus);
	
	List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(String studentCid ,String focusAreaCid, ActivityStatus activityStatus);
	
	List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(String studentCid ,PSDArea psdArea, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(String studentCid, String teacherCid ,ActivityStatus activityStatus);
	
	ActivityPerformed findByCidAndActiveTrue(String id);
	
	List<ActivityPerformed> findAllByTeacherCidAndActivityStatusAndActiveTrue(String teacherCid, ActivityStatus activityStatus);
	
	@Query(value = "SELECT * FROM mgs.activity_performed a where (select extract(year from a.date_of_activity)) = :yearOfActivity and a.student_id = :studentId",nativeQuery = true)
	List<ActivityPerformed> findAllByYearOfActivity(@Param("yearOfActivity") String yearOfActivity ,@Param("studentId") Long studentId);
}
