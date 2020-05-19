package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.TeacherActivityGrade;
import com.nxtlife.mgs.entity.common.TeacherActivityGradeId;
import com.nxtlife.mgs.entity.user.Teacher;

public interface TeacherActivityGradeRepository extends JpaRepository<TeacherActivityGrade, TeacherActivityGradeId>{

	boolean existsByTeacherActivityGradeIdAndActiveTrue(TeacherActivityGradeId id);
	
	boolean findByTeacherCid(String teacherCid);
	
	@Query(value = "select tag.teacher from TeacherActivityGrade tag where tag.activity.cid = :activityCid and tag.teacher.school.cid = :schoolCid and tag.active = true")
	List<Teacher> findAllTeacherByActivityCidAndTeacherSchoolCidActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid );
	
	@Query(value = "select tag.teacher from TeacherActivityGrade tag where tag.activity.name = :activityName and tag.teacher.school.cid = :schoolCid and tag.active = true")
	List<Teacher> findAllTeacherByActivityNameAndTeacherSchoolCidActiveTrue(@Param("activityName") String activityName ,@Param("schoolCid") String schoolCid );
	
	@Query(value = "select tag.teacher from TeacherActivityGrade tag where tag.activity.cid = :activityCid and tag.teacher.school.cid = :schoolCid and tag.grade.cid = :gradeCid and tag.active = true")
	List<Teacher> findAllTeacherByActivityCidAndTeacherSchoolCidAndGradeCidAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("gradeCid") String gradeCid);
	
	@Query(value = "select tag.activity from TeacherActivityGrade tag where tag.teacher.cid = :teacherCid and tag.active = true")
	List<Activity> findAllActivityByTeacherCidAndActiveTrue(@Param("teacherCid") String teacherCid );
	
	@Query(value = "select tag.activity from TeacherActivityGrade tag where tag.teacher.id = :teacherId and tag.active = true")
	List<Activity> findAllActivityByTeacherIdAndActiveTrue(@Param("teacherId") Long teacherId);
	
	@Query(value = "select tag.teacher from TeacherActivityGrade tag where  tag.active = true")
	List<Teacher> findAllTeacherByActiveTrue();
	
	@Query(value = "select tag.teacher from TeacherActivityGrade tag where tag.teacher.school.cid = :schoolCid and tag.active = true")
	List<Teacher> findAllTeacherByTeacherSchoolCidActiveTrue(@Param("schoolCid") String schoolCid );

	boolean existsByTeacherIdAndActiveTrue(Long teacherId);
}
