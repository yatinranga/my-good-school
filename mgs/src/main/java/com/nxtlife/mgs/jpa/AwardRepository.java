package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.enums.AwardStatus;
import com.querydsl.core.types.Predicate;

public interface AwardRepository extends JpaRepository<Award, Long>, QueryDslPredicateExecutor<Award> {

	public List<Award> findByStudentIdAndStatus(Long studentId, AwardStatus status);

	public List<Award> findByActivityIn(List<Activity> activities);

	public List<Award> findAll(Predicate predicate);

	public Award findByCidAndActiveTrue(String cid);

}
