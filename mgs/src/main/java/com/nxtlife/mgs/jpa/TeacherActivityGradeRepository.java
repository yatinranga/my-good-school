package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.activity.TeacherActivityGrade;
import com.nxtlife.mgs.entity.common.TeacherActivityGradeId;
import com.nxtlife.mgs.entity.user.Teacher;

public interface TeacherActivityGradeRepository extends JpaRepository<TeacherActivityGrade, TeacherActivityGradeId>{

	public boolean existsByTeacherActivityGradeIdAndActiveTrue(TeacherActivityGradeId id);
	
	public boolean findByTeacherCid(String teacherCid);
	
	@Query(value = "select distinct tag.teacher from TeacherActivityGrade tag where tag.activity.cid = :activityCid and tag.teacher.school.cid = :schoolCid and tag.grade.cid in :gradeIds and tag.active = true")
	public List<Teacher> findAllTeacherByActivityCidAndTeacherSchoolCidAndGradeCidInAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("gradeIds") Collection<String> gradeIds );
	
	@Query(value = "select distinct tag.teacher from TeacherActivityGrade tag where tag.activity.name = :activityName and tag.teacher.school.cid = :schoolCid and tag.active = true")
	public List<Teacher> findAllTeacherByActivityNameAndTeacherSchoolCidActiveTrue(@Param("activityName") String activityName ,@Param("schoolCid") String schoolCid );
	
	@Query(value = "select distinct tag.teacher from TeacherActivityGrade tag where tag.activity.cid = :activityCid and tag.teacher.school.cid = :schoolCid and tag.grade.cid = :gradeCid and tag.active = true")
	public List<Teacher> findAllTeacherByActivityCidAndTeacherSchoolCidAndGradeCidAndActiveTrue(@Param("activityCid") String activityCid ,@Param("schoolCid") String schoolCid ,@Param("gradeCid") String gradeCid);
	
	@Query(value = "select distinct tag.activity from TeacherActivityGrade tag where tag.teacher.cid = :teacherCid and tag.active = true")
	public List<Activity> findAllActivityByTeacherCidAndActiveTrue(@Param("teacherCid") String teacherCid );
	
	@Query(value = "select distinct tag.activity from TeacherActivityGrade tag where tag.teacher.id = :teacherId and tag.active = true")
	public List<Activity> findAllActivityByTeacherIdAndActiveTrue(@Param("teacherId") Long teacherId);
	
	@Query(value = "select distinct tag.teacher from TeacherActivityGrade tag where  tag.active = true")
	public List<Teacher> findAllTeacherByActiveTrue();
	
	@Query(value = "select  distinct tag.teacher from TeacherActivityGrade tag where tag.teacher.school.cid = :schoolCid and tag.active = true")
	public List<Teacher> findAllTeacherByTeacherSchoolCidActiveTrue(@Param("schoolCid") String schoolCid );
	
	@Query(value = "select distinct tag.teacher from TeacherActivityGrade tag where tag.teacher.school.cid = ?1 and tag.grade.id in ?2 and tag.active = true")
	public List<Teacher> findAllTeacherByTeacherSchoolCidAndGradeIdsInAndActiveTrue( String schoolCid , Collection<Long> gradeIds);
	//tag.teacher.school.cid = ?1 and String schoolCid ,
	@Query(value = "select distinct tag.teacher from TeacherActivityGrade tag where tag.teacher.school.cid = ?1 and tag.grade.cid in ?2 and tag.active = true")
	public List<Teacher> findAllTeacherByTeacherSchoolCidAndGradeCidInAndActiveTrue(String schoolCid ,  Collection<String> gradeIds);

//	@Query(value = "select tag.teacher from TeacherActivityGrade tag where tag.teacher.school.cid = ?1 and tag.grade.id in ?2 and tag.activity.cid = ?3 and tag.active = true")
//	List<Teacher> findAllTeacherByTeacherSchoolCidAndGradeIdsInAndActiveTrue( String schoolCid , Collection<Long> gradeIds , String activityCid);
	
	public boolean existsByTeacherIdAndActiveTrue(Long teacherId);
	
	public List<TeacherActivityGrade> findAllByTeacherIdAndActiveTrue(Long teacherId);

	public List<TeacherActivityGrade> findAllByTeacherIdAndGradeCidInAndActiveTrue(Long teacherId,
			List<String> gradeIds);
	
	@Query(value = "select distinct tag.activity from TeacherActivityGrade tag where tag.teacher.school.cid = ?1 and  tag.grade.cid in ?2 and tag.active = true")
	public List<Activity> findAllActivityBySchoolCidAndGradeCidsInActiveTrue(String schoolCId , Collection<String> gradeIds);
}
