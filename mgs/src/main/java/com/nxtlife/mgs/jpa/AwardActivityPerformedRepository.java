package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.nxtlife.mgs.entity.common.AwardActivityPerformedId;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;

public interface AwardActivityPerformedRepository
		extends JpaRepository<AwardActivityPerformed, AwardActivityPerformedId>,
		QuerydslPredicateExecutor<AwardActivityPerformed> {

}
