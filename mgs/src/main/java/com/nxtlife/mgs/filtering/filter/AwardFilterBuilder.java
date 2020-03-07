package com.nxtlife.mgs.filtering.filter;

import org.springframework.stereotype.Component;

import com.nxtlife.mgs.entity.school.QAwardActivityPerformed;
import com.querydsl.core.types.Predicate;

@Component
public class AwardFilterBuilder implements FilterBuilder<AwardFilter> {

	private final QAwardActivityPerformed AwardActivityPerformed = QAwardActivityPerformed.awardActivityPerformed;

	@Override
	public Predicate build(AwardFilter filter) {
		return new OptionalBooleanBuilder(AwardActivityPerformed.isNotNull())
				// .notEmptyAnd(AwardActivityPerformed.assignerCid::contains,
				// filter.getAssignerId())
				.notEmptyAnd(AwardActivityPerformed.activityPerformed.activity.fourS.stringValue()::containsIgnoreCase,
						filter.getFourS())
				.notEmptyAnd(AwardActivityPerformed.activityPerformed.activityStatus.stringValue()::containsIgnoreCase,
						filter.getStatus())
				.notEmptyAnd(AwardActivityPerformed.activityPerformed.activity.cid::contains, filter.getActivityId())
				.notEmptyAnd(AwardActivityPerformed.activityPerformed.activity.focusAreas.any().cid::contains,
						filter.getFocusAreaId())
				.notEmptyAnd(AwardActivityPerformed.activityPerformed.activity.focusAreas.any().psdArea
						.stringValue()::containsIgnoreCase, filter.getPsdArea())
				// .notEmptyAnd(AwardActivityPerformed.dateOfReceipt.year().stringValue()::containsIgnoreCase,
				// filter.getYear())
				.build();
		// return null;
	}

}
