package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.common.StudentSchoolGradeId;
import com.nxtlife.mgs.entity.school.StudentSchoolGrade;

@Repository
public interface StudentSchoolGradeRepository extends JpaRepository<StudentSchoolGrade, StudentSchoolGradeId>{

	
}
