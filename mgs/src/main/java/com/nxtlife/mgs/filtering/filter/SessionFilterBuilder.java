package com.nxtlife.mgs.filtering.filter;

import org.springframework.stereotype.Component;

import com.nxtlife.mgs.entity.session.QEvent;
import com.nxtlife.mgs.util.DateUtil;
import com.querydsl.core.types.Predicate;

@Component
public class SessionFilterBuilder {

	private final QEvent qEvent = QEvent.event;

	public Predicate build(SessionFilter filter) {
		return new OptionalBooleanBuilder(qEvent.isNotNull())
				.notEmptyAnd(qEvent.club.cid.stringValue()::contains, filter.getClubId())
				.notEmptyAnd(qEvent.startDate.stringValue()::contains, filter.getStartDate())
				.notEmptyAnd(qEvent.endDate.stringValue()::contains, filter.getEndDate())
				.notEmptyAnd(qEvent.startDate::goe, DateUtil.convertStringToDate(filter.getFromDate()))
				.notEmptyAnd(qEvent.endDate::loe, DateUtil.convertStringToDate(filter.getUntilDate()))
				.notEmptyAnd(qEvent.grades.any().cid.stringValue()::eq, filter.getGradeId())
				.notEmptyAnd(qEvent.teacher.cid.stringValue()::eq, filter.getTeacherId()).build();
	}
}
