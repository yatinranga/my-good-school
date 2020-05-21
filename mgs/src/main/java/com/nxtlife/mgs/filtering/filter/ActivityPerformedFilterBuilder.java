package com.nxtlife.mgs.filtering.filter;

import org.springframework.stereotype.Component;

import com.nxtlife.mgs.entity.activity.QActivityPerformed;
import com.querydsl.core.types.Predicate;

@Component
public class ActivityPerformedFilterBuilder implements FilterBuilder<ActivityPerformedFilter> {

	private final QActivityPerformed ActivityPerformed = QActivityPerformed.activityPerformed;

	// @Override
	// public Predicate build(ActivityPerformedFilter filter) {
	// return new OptionalBooleanBuilder(ActivityPerformed.isNotNull())
	// .notEmptyAnd(ActivityPerformed.teacher.cid::contains,
	// filter.getTeacherId())
	// .notEmptyAnd(ActivityPerformed.activity.fourS.stringValue()::containsIgnoreCase,
	// filter.getFourS())
	// .notEmptyAnd(ActivityPerformed.activityStatus.stringValue()::containsIgnoreCase,
	// filter.getStatus())
	// .notEmptyAnd(ActivityPerformed.activity.cid::contains,
	// filter.getActivityId())
	// .notEmptyAnd(ActivityPerformed.activity.focusAreas.any().cid::contains,
	// filter.getFocusAreaId())
	// .notEmptyAnd(ActivityPerformed.activity.focusAreas.any().psdArea.stringValue()::containsIgnoreCase,
	// filter.getPsdArea())
	// .notEmptyAnd(ActivityPerformed.dateOfActivity.year().stringValue()::containsIgnoreCase,
	// filter.getYear())
	// .build();
	// }

	@Override
	public Predicate build(ActivityPerformedFilter filter) {
		return new OptionalBooleanBuilder(ActivityPerformed.isNotNull())
				.notEmptyAnd(ActivityPerformed.student.cid::eq, filter.getStudentId())
				.notEmptyAnd(ActivityPerformed.teacher.cid::eq, filter.getTeacherId())
				.notEmptyAnd(ActivityPerformed.activity.fourS.stringValue()::containsIgnoreCase, filter.getFourS())
				.notEmptyAnd(ActivityPerformed.activityStatus.stringValue()::containsIgnoreCase, filter.getStatus())
				.notEmptyAnd(ActivityPerformed.activity.cid::contains, filter.getActivityId())
				.notEmptyAnd(ActivityPerformed.activity.focusAreas.any().name::containsIgnoreCase,
						filter.getFocusArea())
				.notEmptyAnd(ActivityPerformed.activity.focusAreas.any().psdArea.stringValue()::containsIgnoreCase,
						filter.getPsdArea())
				.notEmptyAnd(ActivityPerformed.dateOfActivity.year().stringValue()::containsIgnoreCase,
						filter.getYear())
				.notEmptyAnd(ActivityPerformed.student.school.cid::eq, filter.getSchoolId())
				.notEmptyAnd(ActivityPerformed.student.grade.name::equalsIgnoreCase, filter.getGrade())
				.notEmptyAnd(ActivityPerformed.student.grade.section::equalsIgnoreCase, filter.getSection())
				.notEmptyAnd(ActivityPerformed.active.stringValue()::containsIgnoreCase, "TRUE").build();
	}
}
