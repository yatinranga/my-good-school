package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.entity.session.Event;
import com.querydsl.core.types.Predicate;

public interface SessionRepository extends JpaRepository<Event, Long> , QueryDslPredicateExecutor<Event>{

	public List<Event> findAll(Predicate predicate);

	public Event findByCidAndActiveTrue(String cid);
	
	public List<Event> findAllByGradesCidAndClubCidAndActiveTrue(String gradeCid , String clubCid , Pageable pageable);
	
	public List<Event> findAllByGradesCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(String gradeCid , String clubCid,Date rangeBegin ,Date rangeEnd , Sort sort);
	
	@Query(value = "select e from Event e join e.grades g where g.cid =:gradeCid and e.club in :clubs and e.active = true group by e.club , e.startDate ,e.teacher")
	public List<Event> findAllByGradesCidAndClubInAndActiveTrueGroupByClubId(@Param("gradeCid") String gradeCid  ,@Param("clubs") Collection<Activity> clubs , Pageable pageable);
	
	@Query(value = "select e from Event e join e.grades g where g.cid =:gradeCid and e.club in :clubs and e.startDate > :rangeBegin and e.startDate < :rangeEnd and e.active = true group by e.club , e.startDate, e.teacher")
	public List<Event> findAllByGradesCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(@Param("gradeCid") String gradeCid ,@Param("clubs") Collection<Activity> clubs ,@Param("rangeBegin") Date rangeBegin ,@Param("rangeEnd") Date rangeEnd , Sort sort);

    public List<Event> findAllByTeacherCidAndClubCidAndActiveTrue(String teacherCid , String clubCid , Pageable pageable);
	
	public List<Event> findAllByTeacherCidAndClubCidAndStartDateGreaterThanAndStartDateLessThanAndActiveTrue(String teacherCid , String clubCid,Date rangeBegin ,Date rangeEnd , Sort sort);
	
	@Query(value = "select e from Event e  where e.teacher.cid =:teacherCid and e.club in :clubs and e.active = true group by e.club , e.startDate ,e.teacher")
	public List<Event> findAllByTeacherCidAndClubInAndActiveTrueGroupByClubId(@Param("teacherCid") String teacherCid,@Param("clubs") List<Activity> clubs,
			Pageable pageable);
	
	@Query(value = "select e from Event e where e.teacher.cid =:teacherCid and e.club in :clubs and e.startDate > :rangeBegin and e.startDate < :rangeEnd and e.active = true group by e.club , e.startDate ,e.teacher")
	public List<Event> findAllByTeacherCidAndClubInAndStartDateGreaterThanAndStartDateLessThanAndActiveTrueGroupByClubId(@Param("teacherCid") String teacherCid ,@Param("clubs") Collection<Activity> clubs ,@Param("rangeBegin") Date rangeBegin ,@Param("rangeEnd") Date rangeEnd , Sort sort);
}
