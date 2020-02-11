package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.school.StudentSchoolGrade;
import com.nxtlife.mgs.util.StudentSchoolGradeId;

@Repository
public interface StudentSchoolGradeRepository extends JpaRepository<StudentSchoolGrade, StudentSchoolGradeId>{

	
}
