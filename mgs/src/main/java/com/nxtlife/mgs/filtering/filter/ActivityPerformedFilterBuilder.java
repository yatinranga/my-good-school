package com.nxtlife.mgs.filtering.filter;

import com.nxtlife.mgs.entity.activity.QActivityPerformed;
import com.nxtlife.mgs.entity.school.QAwardActivityPerformed;
import com.querydsl.core.types.Predicate;

public class ActivityPerformedFilterBuilder implements FilterBuilder<ActivityPerformedFilter>{

private final QActivityPerformed ActivityPerformed = QActivityPerformed.activityPerformed;
	
	@Override
	public Predicate build(ActivityPerformedFilter filter) {
		return new OptionalBooleanBuilder(ActivityPerformed.isNotNull())
				.notEmptyAnd(ActivityPerformed.teacher.cid::contains, filter.getTeacherId())
				.notEmptyAnd(ActivityPerformed.activity.fourS.stringValue()::containsIgnoreCase, filter.getFourS())
				.notEmptyAnd(ActivityPerformed.activityStatus.stringValue()::containsIgnoreCase, filter.getStatus())
				.notEmptyAnd(ActivityPerformed.activity.cid::contains,filter.getActivityId())
				.notEmptyAnd(ActivityPerformed.activity.focusAreas.any().cid::contains, filter.getFocusAreaId())
				.notEmptyAnd(ActivityPerformed.activity.focusAreas.any().psdArea.stringValue()::containsIgnoreCase, filter.getPsdArea())
				.notEmptyAnd(ActivityPerformed.dateOfActivity.year().stringValue()::containsIgnoreCase, filter.getYear())
				.build();
//		return null;
	}
}
