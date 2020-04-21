package com.nxtlife.mgs.jpa;

import java.util.List;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.util.StudentActivityId;

public interface StudentClubRepository extends JpaRepository<StudentClub, StudentActivityId> {

	boolean existsByStudentIdAndMembershipStatusAndActiveTrue(Long studentId , ApprovalStatus membershipStatus);
	
	boolean existsByStudentIdAndActivityIdAndMembershipStatusAndActiveTrue(Long studentId ,Long activityId, ApprovalStatus membershipStatus);
	
	boolean existsByStudentIdAndActivityCidAndMembershipStatusAndActiveTrue(Long studentId ,String activityCid, ApprovalStatus membershipStatus);
	
	boolean existsByStudentIdAndActivityCidAndMembershipStatusNotAndActiveTrue(Long studentId ,String activityCid, ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.activity from StudentClub sc where sc.student.id = :studentId and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Activity> findActivityByStudentIdAndMembershipStatusAndActiveTrue(@Param("studentId") Long studentId , @Param("membershipStatus") ApprovalStatus membershipStatus );
	
	//@Query(value = "select sc.activity from StudentClub sc where sc.teacher.id = :teacherId and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<StudentClub> findAllByTeacherIdAndMembershipStatusAndActiveTrue(@Param("teacherId") Long teacherId , @Param("membershipStatus") ApprovalStatus membershipStatus );
}
