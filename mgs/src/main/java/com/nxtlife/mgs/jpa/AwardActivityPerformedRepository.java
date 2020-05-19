package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.nxtlife.mgs.entity.common.AwardActivityPerformedId;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;

public interface AwardActivityPerformedRepository
		extends JpaRepository<AwardActivityPerformed, AwardActivityPerformedId>,
		QueryDslPredicateExecutor<AwardActivityPerformed> {


}
