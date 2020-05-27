package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.enums.ActivityStatus;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.view.ActivityPerformedResponse;
import com.nxtlife.mgs.view.GroupResponseBy;
import com.nxtlife.mgs.view.PropertyCount;
import com.querydsl.core.types.Predicate;

@Repository
public interface ActivityPerformedRepository
		extends JpaRepository<ActivityPerformed, Long>, QuerydslPredicateExecutor<ActivityPerformed> {

	List<ActivityPerformed> findAllByStudentCidAndActivityStatusAndActiveTrue(String studentCid,
			ActivityStatus activityStatus, Pageable pageable);

	List<ActivityPerformed> findAllByStudentCidAndActivityCidAndActivityStatusAndActiveTrue(String studentCid,
			String activityCid, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(String studentCid,
			FourS fourS, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(String studentCid,
			String focusAreaCid, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
			String studentCid, PSDArea psdArea, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(String studentCid,
			String teacherCid, ActivityStatus activityStatus);

	ActivityPerformed findByCidAndActiveTrue(String id);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:teacherCid and (ap.activityStatus =:activityStatus or ap.activityStatus =:status2 ) and ap.active = true")
	List<ActivityPerformed> findAllByTeacherCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("teacherCid") String teacherCid, @Param("activityStatus") ActivityStatus activityStatus,
			@Param("status2") ActivityStatus status2);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:teacherCid and ap.student.grade.cid = :gradeCid and (ap.activityStatus =:activityStatus or ap.activityStatus =:status2 ) and ap.active = true")
	List<ActivityPerformed> findAllByTeacherCidAndStudentGradeCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("teacherCid") String teacherCid, @Param("gradeCid") String gradeCid,
			@Param("activityStatus") ActivityStatus activityStatus, @Param("status2") ActivityStatus status2);

	@Query(value = "SELECT * FROM mgs.activity_performed a where (select extract(year from a.date_of_activity)) = :yearOfActivity and a.student_id = :studentId", nativeQuery = true)
	List<ActivityPerformed> findAllByYearOfActivity(@Param("yearOfActivity") String yearOfActivity,
			@Param("studentId") Long studentId);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:coachCid and  ap.activity.cid =:activityCid and (ap.activityStatus =:status1 or ap.activityStatus =:status2 ) and ap.active = true")
	List<ActivityPerformed> findAllByTeacherCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("coachCid") String coachCid, @Param("activityCid") String activityCid,
			@Param("status1") ActivityStatus status1, @Param("status2") ActivityStatus status2);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:coachCid and ap.student.grade.cid = :gradeCid and ap.activity.cid =:activityCid and (ap.activityStatus =:status1 or ap.activityStatus =:status2 ) and ap.active = true")
	List<ActivityPerformed> findAllByTeacherCidAndStudentGradeCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("coachCid") String coachCid, @Param("gradeCid") String gradeCid,
			@Param("activityCid") String activityCid, @Param("status1") ActivityStatus status1,
			@Param("status2") ActivityStatus status2);

	List<ActivityPerformed> findAllByStudentCidAndStudentActiveTrueAndActivityCidAndActivityActiveTrueAndActivityStatusAndActiveTrue(
			String studentCid, String activityCid, ActivityStatus activityStatus);

	List<ActivityPerformed> findAllByActiveTrue();

	List<ActivityPerformed> findAllByStudentSchoolCidAndStudentSchoolActiveTrueAndActiveTrue(String schoolCid);

	List<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityCidAndAndActivityStatusAndStudentSchoolActiveTrueAndStudentGradeActiveTrueAndActivityActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String ActivityCid, ActivityStatus status);

	List<ActivityPerformed> findAllByStudentCidAndActiveTrue(String studentCid, Pageable pageable);

	List<ActivityPerformed> findAllByTeacherCidAndActivityStatusAndActiveTrue(String coachCid, ActivityStatus reviewed);

	List<ActivityPerformed> findAll(Predicate build);

	List<ActivityPerformed> findAllByStudentCidAndActiveTrue(String studentCid);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:coachCid and (ap.activityStatus =:submittedbystudent or ap.activityStatus =:savedbyteacher or ap.activityStatus =:reviewed) and ap.active = true")
	List<ActivityPerformed> findAllByTeacherCidAndActivityStatusOrActivityStatusOrActivityStatusAndActiveTrue(
			@Param("coachCid") String coachCid, @Param("submittedbystudent") ActivityStatus submittedbystudent,
			@Param("savedbyteacher") ActivityStatus savedbyteacher, @Param("reviewed") ActivityStatus reviewed);

	@Query(value = "select ap.id from ActivityPerformed  ap where ap.cid =:activityPerformedCid and ap.active = true")
	Long findIdByCidAndActiveTrue(@Param("activityPerformedCid") String activityPerformedCid);

	boolean existsByCidAndActiveTrue(String activityCid);

	boolean existsByTeacherCidAndActivityCidAndActiveTrue(String teacherCid, String activityCid);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(a.activity.fourS, COUNT(DISTINCT a.id)) " + "FROM "
			+ "    ActivityPerformed a "
			+ "WHERE a.student.cid =:cid AND a.activityStatus =:status AND a.active = TRUE " + "GROUP BY "
			+ "   a.activity.fourS")
	List<PropertyCount> findFourSCount(@Param("cid") String cid, @Param("status") ActivityStatus status);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(f.name, COUNT(DISTINCT ap.id)) " + "FROM "
			+ "    ActivityPerformed ap JOIN ap.activity a JOIN a.focusAreas f  "
			+ "WHERE ap.student.cid =:cid AND ap.activityStatus =:status AND ap.active = TRUE " + "GROUP BY "
			+ "   f.name")
	List<PropertyCount> findFocusAreaCount(@Param("cid") String cid, @Param("status") ActivityStatus status);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(f.psdArea, COUNT(DISTINCT ap.id)) " + "FROM "
			+ "    ActivityPerformed ap JOIN ap.activity a JOIN a.focusAreas f  "
			+ "WHERE ap.student.cid =:cid AND ap.activityStatus =:status AND ap.active = TRUE " + "GROUP BY "
			+ "   f.psdArea")
	List<PropertyCount> findPsdAreaCount(@Param("cid") String cid, @Param("status") ActivityStatus status);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, PSDArea psdArea, ActivityStatus status, Date startDate, Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, PSDArea psdArea, ActivityStatus status, Date startDate, Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, FourS fourS, ActivityStatus status, Date startDate, Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, FourS fourS, ActivityStatus status, Date startDate, Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, String focusAreaName, ActivityStatus status, Date startDate,
			Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String focusAreaName, ActivityStatus status, Date startDate, Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, String activityName, ActivityStatus status, Date startDate,
			Date endDate);

	Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String activityName, ActivityStatus status, Date startDate, Date endDate);
	
	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.id =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name")
	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolIdAndGradesIdInAndActivityStatusIn(Long schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses );
	
	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name")
	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolCidAndGradesIdInAndActivityStatusIn(String schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses );
	
	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.id =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name having ap.activity.cid = ?4")
	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolIdAndGradesIdInAndActivityStatusInAndClubId(Long schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses ,String activityCid );
	
	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name having ap.activity.cid = ?4")
	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolCidAndGradesIdInAndActivityStatusInAndClubId(String schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses ,String activityCid );
	
//	@Query("select" +" new com.nxtlife.mgs.view.ActivityPerformedResponse( ap )  , " + " ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count " +  "from " +" ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name having ap.activity.cid = ?4")
//	Set<GroupResponseBy<ActivityPerformedResponse>> findAlllBySchoolCidAndGradesIdInAndActivityStatusInAndClubId(String schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses ,String activityCid );
}
