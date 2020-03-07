package com.nxtlife.mgs.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.util.AwardActivityPerformedId;

public interface AwardActivityPerformedRepository
		extends JpaRepository<AwardActivityPerformed, AwardActivityPerformedId>,
		QueryDslPredicateExecutor<AwardActivityPerformed> {

}
