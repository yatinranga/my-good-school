package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.common.StudentActivityId;
import com.nxtlife.mgs.entity.school.StudentClub;
import com.nxtlife.mgs.entity.user.Student;
import com.nxtlife.mgs.enums.ApprovalStatus;

public interface StudentClubRepository extends JpaRepository<StudentClub, StudentActivityId> {

	public boolean existsByStudentIdAndMembershipStatusAndActiveTrue(Long studentId , ApprovalStatus membershipStatus);
	
	public boolean existsByStudentIdAndActivityIdAndMembershipStatusAndActiveTrue(Long studentId ,Long activityId, ApprovalStatus membershipStatus);
	
	public boolean existsByStudentIdAndActivityCidAndMembershipStatusAndActiveTrue(Long studentId ,String activityCid, ApprovalStatus membershipStatus);
	
	public boolean existsByStudentIdAndActivityCidAndMembershipStatusNotAndActiveTrue(Long studentId ,String activityCid, ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid = :gradeCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Student> findAllStudentByActivityCidAndTeacherCidAndStudentSchoolCidAndStudentGradeCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("gradeCid") String gradeCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.activity from StudentClub sc where sc.student.id = :studentId and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Activity> findActivityByStudentIdAndMembershipStatusAndActiveTrue(@Param("studentId") Long studentId , @Param("membershipStatus") ApprovalStatus membershipStatus );
	
	@Query(value = "select sc.activity from StudentClub sc where sc.student.cid = :studentId and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Activity> findActivityByStudentCidAndMembershipStatusAndActiveTrue(@Param("studentId") String studentId , @Param("membershipStatus") ApprovalStatus membershipStatus );
	
	//@Query(value = "select sc.activity from StudentClub sc where sc.teacher.id = :teacherId and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<StudentClub> findAllByTeacherIdAndActiveTrue(Long teacherId);
	
	public List<StudentClub> findAllByStudentIdAndActiveTrue(Long studentId);

	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.teacher.cid = :teacherCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndTeacherCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("teacherCid") String teacherCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid = :gradeCid and sc.teacher.cid = :teacherCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndGradeCidAndTeacherCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid , @Param("gradeCid") String gradeCid ,@Param("teacherCid") String teacherCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid = :gradeCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndGradeCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid , @Param("gradeCid") String gradeCid,@Param("membershipStatus") ApprovalStatus membershipStatus);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("membershipStatus") ApprovalStatus membershipStatus);

	public List<StudentClub> findAllByTeacherIdAndActivityCidAndActiveTrue(Long teacherId, String activityId);

	public List<StudentClub> findAllByTeacherIdAndStudentGradeCidInAndActiveTrue(Long teacherId ,Collection<String> gradeIds);

	public List<StudentClub> findAllByTeacherIdAndActivityCidAndStudentGradeCidInAndActiveTrue(Long teacherId,
			String clubId, List<String> gradeIds);
	//needGrade
	@Query("select sc.student from StudentClub sc join com.nxtlife.mgs.entity.activity.TeacherActivityGrade tag on sc.activity.id = tag.activity.id where sc.student.grade.id = tag.grade.id and tag.teacher.cid =?1 and sc.activity.cid = ?2 and sc.membershipStatus =?3 and sc.active = true")
	public List<Student> getAllStudentsOfSupervisorOfparticularClubGrade(String teacherCid ,String activityCid ,ApprovalStatus status);
	
	@Query(value = "select sc.student from StudentClub sc where sc.activity.cid = :activityCid and sc.student.school.cid = :schoolCid and sc.student.grade.cid in :gradeCids and sc.membershipStatus = :membershipStatus and sc.active = true")
	public List<Student> findAllStudentByActivityCidAndStudentSchoolCidAndGradeCidsInAndMembershipStatusAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid , @Param("gradeCids") Collection<String> gradeCids,@Param("membershipStatus") ApprovalStatus membershipStatus);

	@Query(value = "select sc from StudentClub sc where sc.student.id = :studentId and sc.membershipStatus = :membershipStatus and sc.active = true")
	public Set<StudentClub> findActivityAndTeacherByStudentIdAndMembershipStatusAndActiveTrue(@Param("studentId") Long studentId , @Param("membershipStatus") ApprovalStatus membershipStatus );
}
