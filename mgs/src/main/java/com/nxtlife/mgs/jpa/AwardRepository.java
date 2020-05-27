package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.school.Award;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.ApprovalStatus;
import com.nxtlife.mgs.view.PropertyCount;
import com.querydsl.core.types.Predicate;

public interface AwardRepository extends JpaRepository<Award, Long>, QuerydslPredicateExecutor<Award> {

	public List<Award> findByStudentIdAndStatus(Long studentId, ApprovalStatus status);
	
	public List<Award> findByStudentCidAndStatus(String studentId, ApprovalStatus status);

	public List<Award> findByActivityIn(Collection<Activity> activities);

	public List<Award> findByActivityInOrActivityNullAndTeacherCid(Collection<Activity> activities ,String teacherCid);

	public List<Award> findAll(Predicate predicate);

	public Award findByCidAndActiveTrue(String cid);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(a.activity.fourS, COUNT(DISTINCT aw.id)) " + "FROM "
			+ "   Award aw JOIN aw.awardActivityPerformed awap JOIN awap.activityPerformed a "
			+ "WHERE a.student.cid =:cid AND a.activityStatus =:status AND aw.status =:awardStatus AND a.active = TRUE AND aw.active = TRUE "
			+ "GROUP BY " + "   a.activity.fourS")
	List<PropertyCount> findFourSCount(@Param("cid") String cid, @Param("status") ActivityStatus status,
			@Param("awardStatus") ApprovalStatus awardStatus);

	@Query("SELECT  " + "new com.nxtlife.mgs.view.PropertyCount(f.name, COUNT(DISTINCT aw.id)) " + "FROM "
			+ "    Award aw JOIN aw.awardActivityPerformed awap JOIN awap.activityPerformed ap JOIN ap.activity a JOIN a.focusAreas f  "
			+ "WHERE ap.student.cid =:cid AND ap.activityStatus =:status AND aw.status =:awardStatus AND ap.active = TRUE AND aw.active = TRUE  "
			+ "GROUP BY " + "   f.name")
	List<PropertyCount> findFocusAreaCount(@Param("cid") String cid, @Param("status") ActivityStatus status,
			@Param("awardStatus") ApprovalStatus awardStatus);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(f.psdArea, COUNT(DISTINCT aw.id)) " + "FROM "
			+ "    Award aw JOIN aw.awardActivityPerformed awap JOIN awap.activityPerformed ap JOIN ap.activity a JOIN a.focusAreas f  "
			+ "WHERE ap.student.cid =:cid AND ap.activityStatus =:status AND aw.status =:awardStatus AND ap.active = TRUE AND aw.active = TRUE  "
			+ "GROUP BY " + "   f.psdArea")
	List<PropertyCount> findPsdAreaCount(@Param("cid") String cid, @Param("status") ActivityStatus status,
			@Param("awardStatus") ApprovalStatus awardStatus);

}
