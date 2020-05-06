package com.nxtlife.mgs.jpa;

import java.util.List;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.util.StudentActivityId;

public interface StudentClubRepository extends JpaRepository<StudentClub, StudentActivityId> {

	boolean existsByStudentIdAndMembershipStatusAndActiveTrue(Long studentId , ApprovalStatus membershipStatus);
	
	boolean existsByStudentIdAndActivityIdAndMembershipStatusAndActiveTrue(Long studentId ,Long activityId, ApprovalStatus membershipStatus);
	
	boolean existsByStudentIdAndActivityCidAndMembershipStatusAndActiveTrue(Long studentId ,String activityCid, ApprovalStatus membershipStatus);
	
	boolean existsByStudentIdAndActivityCidAndMembershipStatusNotAndActiveTrue(Long studentId ,String activityCid, ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid = :gradeCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Student> findAllStudentByActivityCidAndTeacherCidAndStudentSchoolCidAndStudentGradeCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("gradeCid") String gradeCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.activity from StudentClub sc where sc.student.id = :studentId and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Activity> findActivityByStudentIdAndMembershipStatusAndActiveTrue(@Param("studentId") Long studentId , @Param("membershipStatus") ApprovalStatus membershipStatus );
	
	//@Query(value = "select sc.activity from StudentClub sc where sc.teacher.id = :teacherId and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<StudentClub> findAllByTeacherIdAndActiveTrue(Long teacherId);
	
	List<StudentClub> findAllByStudentIdAndActiveTrue(Long studentId);

	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.teacher.cid = :teacherCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndTeacherCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("teacherCid") String teacherCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid = :gradeCid and sc.teacher.cid = :teacherCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndGradeCidAndTeacherCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid , @Param("gradeCid") String gradeCid ,@Param("teacherCid") String teacherCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid = :gradeCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndGradeCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid , @Param("gradeCid") String gradeCid,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);

	
}
