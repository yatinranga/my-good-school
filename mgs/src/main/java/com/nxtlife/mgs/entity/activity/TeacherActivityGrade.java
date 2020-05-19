package com.nxtlife.mgs.entity.activity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.nxtlife.mgs.entity.common.TeacherActivityGradeId;
import com.nxtlife.mgs.entity.school.Grade;
import com.nxtlife.mgs.entity.user.Teacher;

@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id","activity_id","grade_id"}))
public class TeacherActivityGrade implements Serializable{

	@EmbeddedId
	TeacherActivityGradeId teacherActivityGradeId;

	@MapsId(value = "gradeId")
	@ManyToOne
	private Grade grade;
	
	@MapsId(value = "teacherId")
	@ManyToOne
	private Teacher teacher;

	@MapsId(value = "activityId")
	@ManyToOne
	private Activity activity;
	
	Boolean active = true;

	public TeacherActivityGradeId getTeacherActivityGradeId() {
		return teacherActivityGradeId;
	}

	public void setTeacherActivityGradeId(TeacherActivityGradeId teacherActivityGradeId) {
		this.teacherActivityGradeId = teacherActivityGradeId;
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public TeacherActivityGrade(TeacherActivityGradeId teacherActivityGradeId, Grade grade, Teacher teacher,
			Activity activity, Boolean active) {
		super();
		this.teacherActivityGradeId = teacherActivityGradeId;
		this.grade = grade;
		this.teacher = teacher;
		this.activity = activity;
		this.active = active;
	}

	public TeacherActivityGrade() {
		super();
	}
	
	public TeacherActivityGrade(Long teacherId , Long activityId , Long gradeId) {
		if (gradeId != null) {
			this.grade = new Grade();
			this.grade.setId(gradeId);
		}

		if (activityId != null) {
			this.activity = new Activity();
			this.activity.setId(activityId);
		}
		
		if(teacherId != null) {
			this.teacher = new Teacher();
			this.teacher.setId(teacherId);
		}

		this.teacherActivityGradeId = new TeacherActivityGradeId( teacherId, activityId ,gradeId);
	}
	
	
	
	
}
