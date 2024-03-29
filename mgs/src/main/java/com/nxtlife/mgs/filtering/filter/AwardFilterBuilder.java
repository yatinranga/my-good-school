package com.nxtlife.mgs.filtering.filter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.QAward;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.querydsl.core.types.Predicate;

@Component
public class AwardFilterBuilder {

	private final QAward qAward = QAward.award;

	public Predicate build(AwardFilter filter) {
		return new OptionalBooleanBuilder(qAward.isNotNull())
				.notEmptyAnd(qAward.activity.cid.stringValue()::contains, filter.getActivityId())
				.notEmptyAnd(qAward.dateOfReceipt.year().stringValue()::contains, filter.getYear())
				.notEmptyAnd(qAward.student.cid.stringValue()::contains, filter.getStudentId())
				.notEmptyAnd(qAward.teacher.cid.stringValue()::contains, filter.getTeacherId())
				.notEmptyAnd(qAward.activity.focusAreas.any().cid::contains, filter.getFocusArea())
				.notEmptyAnd(qAward.activity.focusAreas.any().psdArea.stringValue()::containsIgnoreCase,
						filter.getPsdArea())
				.notEmptyAnd(qAward.activity.fourS.stringValue()::containsIgnoreCase, filter.getFourS()).build();
	}

	public Predicate build(AwardFilter filter, String studentId, ApprovalStatus status) {
		return new OptionalBooleanBuilder(qAward.isNotNull())
				.notEmptyAnd(qAward.activity.cid.stringValue()::contains, filter.getActivityId())
				.notEmptyAnd(qAward.dateOfReceipt.year().stringValue()::containsIgnoreCase, filter.getYear())
				.notEmptyAnd(qAward.student.cid.stringValue()::contains, filter.getStudentId())
				.notEmptyAnd(qAward.teacher.cid.stringValue()::contains, filter.getTeacherId())
				.notEmptyAnd(qAward.activity.focusAreas.any().name::containsIgnoreCase, filter.getFocusArea())
				.notEmptyAnd(qAward.activity.focusAreas.any().psdArea.stringValue()::containsIgnoreCase,
						filter.getPsdArea())
				.notEmptyAnd(qAward.student.cid.stringValue()::contains, studentId)
				.notEmptyAnd(qAward.active.stringValue()::containsIgnoreCase, "TRUE")
				.notEmptyAnd(qAward.status.stringValue()::containsIgnoreCase, status.name())
				.notEmptyAnd(qAward.activity.fourS.stringValue()::containsIgnoreCase, filter.getFourS()).build();
	}

	public Predicate build(AwardFilter filter, List<Activity> activities) {
		return new OptionalBooleanBuilder(qAward.isNotNull())
				.notEmptyAnd(qAward.activity.cid.stringValue()::contains, filter.getActivityId())
				.notEmptyAnd(qAward.dateOfReceipt.year().stringValue()::containsIgnoreCase, filter.getYear())
				.notEmptyAnd(qAward.student.cid.stringValue()::eq, filter.getStudentId())
				.notEmptyAnd(qAward.teacher.cid.stringValue()::eq, filter.getTeacherId())
				.notEmptyAnd(qAward.activity.focusAreas.any().name::containsIgnoreCase, filter.getFocusArea())
				.notEmptyAnd(qAward.activity.focusAreas.any().psdArea.stringValue()::containsIgnoreCase,
						filter.getPsdArea())
				.notEmptyAnd(qAward.activity::in, activities)
				.notEmptyAnd(qAward.active.stringValue()::containsIgnoreCase, "TRUE")
				.notEmptyAnd(qAward.activity.fourS.stringValue()::containsIgnoreCase, filter.getFourS()).build();
	}

}
