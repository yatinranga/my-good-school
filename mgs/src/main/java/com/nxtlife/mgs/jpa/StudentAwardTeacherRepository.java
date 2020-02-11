package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.school.StudentAward;

public interface StudentAwardTeacherRepository extends JpaRepository<StudentAward, Long>{

}
