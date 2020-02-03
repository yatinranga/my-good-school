package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.user.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

}
