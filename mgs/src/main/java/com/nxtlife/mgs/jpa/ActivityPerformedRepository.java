package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;

public interface ActivityPerformedRepository extends JpaRepository<ActivityPerformed, Long> {

	List<ActivityPerformed> findAllByStudentCidAndActivityStatusAndActiveTrue(String studentCid,
			ActivityStatus activityStatus,Pageable pageable);

	List<ActivityPerformed> findAllByStudentCidAndActivityCidAndActivityStatusAndActiveTrue(String studentCid,
			String activityCid, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(String studentCid,
			FourS fourS, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(String studentCid,
			String focusAreaCid, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
			String studentCid, PSDArea psdArea, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(String studentCid,
			String teacherCid, ActivityStatus activityStatus);

	ActivityPerformed findByCidAndActiveTrue(String id);

	List<ActivityPerformed> findAllByTeacherCidAndActivityStatusOrActivityStatusAndActiveTrue(String teacherCid,
			ActivityStatus activityStatus, ActivityStatus status2);

	List<ActivityPerformed> findAllByTeacherCidAndStudentGradeCidAndActivityStatusOrActivityStatusAndActiveTrue(
			String teacherCid, String gradeCid, ActivityStatus activityStatus, ActivityStatus status2);

	@Query(value = "SELECT * FROM mgs.activity_performed a where (select extract(year from a.date_of_activity)) = :yearOfActivity and a.student_id = :studentId", nativeQuery = true)
	List<ActivityPerformed> findAllByYearOfActivity(@Param("yearOfActivity") String yearOfActivity,
			@Param("studentId") Long studentId);

	List<ActivityPerformed> findAllByTeacherCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
			String coachCid, String activityCid, ActivityStatus status1, ActivityStatus status2);

	List<ActivityPerformed> findAllByTeacherCidAndStudentGradeCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
			String coachCid, String gradeCid, String activityCid, ActivityStatus status1, ActivityStatus status2);

	List<ActivityPerformed> findAllByStudentCidAndStudentActiveTrueAndActivityCidAndActivityActiveTrueAndActivityStatusAndActiveTrue(
			String studentCid, String activityCid, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByActiveTrue();

	List<ActivityPerformed> findAllByStudentSchoolCidAndStudentSchoolActiveTrueAndActiveTrue(String schoolCid);

	List<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityCidAndAndActivityStatusAndStudentSchoolActiveTrueAndStudentGradeActiveTrueAndActivityActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String ActivityCid, ActivityStatus status);

	List<ActivityPerformed> findAllByStudentCidAndActiveTrue(String studentCid, Pageable pageable );
}
