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
import com.nxtlife.mgs.view.PropertyCount;
import com.querydsl.core.types.Predicate;

@Repository
public interface ActivityPerformedRepository
		extends JpaRepository<ActivityPerformed, Long>, QuerydslPredicateExecutor<ActivityPerformed> {

	public List<ActivityPerformed> findAllByStudentCidAndActivityStatusAndActiveTrue(String studentCid,
			ActivityStatus activityStatus, Pageable pageable);

	public List<ActivityPerformed> findAllByStudentCidAndActivityCidAndActivityStatusAndActiveTrue(String studentCid,
			String activityCid, ActivityStatus activityStatus);

	public List<ActivityPerformed> findAllByStudentCidAndActivityFourSAndActivityStatusAndActiveTrue(String studentCid,
			FourS fourS, ActivityStatus activityStatus);

	public List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasCidAndActivityStatusAndActiveTrue(
			String studentCid, String focusAreaCid, ActivityStatus activityStatus);

	public List<ActivityPerformed> findAllByStudentCidAndActivityFocusAreasPsdAreaAndActivityStatusAndActiveTrue(
			String studentCid, PSDArea psdArea, ActivityStatus activityStatus);

	public List<ActivityPerformed> findAllByStudentCidAndTeacherCidAndActivityStatusAndActiveTrue(String studentCid,
			String teacherCid, ActivityStatus activityStatus);

	public ActivityPerformed findByCidAndActiveTrue(String id);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:teacherCid and (ap.activityStatus =:activityStatus or ap.activityStatus =:status2 ) and ap.active = true")
	public List<ActivityPerformed> findAllByTeacherCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("teacherCid") String teacherCid, @Param("activityStatus") ActivityStatus activityStatus,
			@Param("status2") ActivityStatus status2);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:teacherCid and ap.student.grade.cid = :gradeCid and (ap.activityStatus =:activityStatus or ap.activityStatus =:status2 ) and ap.active = true")
	public List<ActivityPerformed> findAllByTeacherCidAndStudentGradeCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("teacherCid") String teacherCid, @Param("gradeCid") String gradeCid,
			@Param("activityStatus") ActivityStatus activityStatus, @Param("status2") ActivityStatus status2);

	@Query(value = "SELECT * FROM mgs.activity_performed a where (select extract(year from a.date_of_activity)) = :yearOfActivity and a.student_id = :studentId", nativeQuery = true)
	public List<ActivityPerformed> findAllByYearOfActivity(@Param("yearOfActivity") String yearOfActivity,
			@Param("studentId") Long studentId);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:coachCid and  ap.activity.cid =:activityCid and (ap.activityStatus =:status1 or ap.activityStatus =:status2 ) and ap.active = true")
	public List<ActivityPerformed> findAllByTeacherCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("coachCid") String coachCid, @Param("activityCid") String activityCid,
			@Param("status1") ActivityStatus status1, @Param("status2") ActivityStatus status2);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:coachCid and ap.student.grade.cid = :gradeCid and ap.activity.cid =:activityCid and (ap.activityStatus =:status1 or ap.activityStatus =:status2 ) and ap.active = true")
	public List<ActivityPerformed> findAllByTeacherCidAndStudentGradeCidAndActivityCidAndActivityStatusOrActivityStatusAndActiveTrue(
			@Param("coachCid") String coachCid, @Param("gradeCid") String gradeCid,
			@Param("activityCid") String activityCid, @Param("status1") ActivityStatus status1,
			@Param("status2") ActivityStatus status2);

	public List<ActivityPerformed> findAllByStudentCidAndStudentActiveTrueAndActivityCidAndActivityActiveTrueAndActivityStatusAndActiveTrue(
			String studentCid, String activityCid, ActivityStatus activityStatus);

	public List<ActivityPerformed> findAllByActiveTrue();

	public List<ActivityPerformed> findAllByStudentSchoolCidAndStudentSchoolActiveTrueAndActiveTrue(String schoolCid);

	public List<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityCidAndAndActivityStatusAndStudentSchoolActiveTrueAndStudentGradeActiveTrueAndActivityActiveTrueAndActiveTrue(
			String schoolCid, String gradeCid, String ActivityCid, ActivityStatus status);

	public List<ActivityPerformed> findAllByStudentCidAndActivityStatusInAndActiveTrue(String studentCid,
			Collection<ActivityStatus> statuses, Pageable pageable);

	public List<ActivityPerformed> findAllByTeacherCidAndActivityStatusAndActiveTrue(String coachCid,
			ActivityStatus reviewed);

	public List<ActivityPerformed> findAll(Predicate build);

	public List<ActivityPerformed> findAllByStudentCidAndActiveTrue(String studentCid);

	@Query(value = "Select ap from ActivityPerformed ap where ap.teacher.cid =:coachCid and (ap.activityStatus =:submittedbystudent or ap.activityStatus =:savedbyteacher or ap.activityStatus =:reviewed) and ap.active = true")
	public List<ActivityPerformed> findAllByTeacherCidAndActivityStatusOrActivityStatusOrActivityStatusAndActiveTrue(
			@Param("coachCid") String coachCid, @Param("submittedbystudent") ActivityStatus submittedbystudent,
			@Param("savedbyteacher") ActivityStatus savedbyteacher, @Param("reviewed") ActivityStatus reviewed);

	@Query(value = "select ap.id from ActivityPerformed  ap where ap.cid =:activityPerformedCid and ap.active = true")
	public Long findIdByCidAndActiveTrue(@Param("activityPerformedCid") String activityPerformedCid);

	public boolean existsByCidAndActiveTrue(String activityCid);

	public boolean existsByTeacherCidAndActivityCidAndActiveTrue(String teacherCid, String activityCid);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(a.activity.fourS, COUNT(DISTINCT a.id)) " + "FROM "
			+ "    ActivityPerformed a "
			+ "WHERE a.student.cid =:cid AND a.activityStatus =:status AND a.active = TRUE " + "GROUP BY "
			+ "   a.activity.fourS")
	public List<PropertyCount> findFourSCount(@Param("cid") String cid, @Param("status") ActivityStatus status);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(f.name, COUNT(DISTINCT ap.id)) " + "FROM "
			+ "    ActivityPerformed ap JOIN ap.activity a JOIN a.focusAreas f  "
			+ "WHERE ap.student.cid =:cid AND ap.activityStatus =:status AND ap.active = TRUE " + "GROUP BY "
			+ "   f.name")
	public List<PropertyCount> findFocusAreaCount(@Param("cid") String cid, @Param("status") ActivityStatus status);

	@Query("SELECT " + "new com.nxtlife.mgs.view.PropertyCount(f.psdArea, COUNT(DISTINCT ap.id)) " + "FROM "
			+ "    ActivityPerformed ap JOIN ap.activity a JOIN a.focusAreas f  "
			+ "WHERE ap.student.cid =:cid AND ap.activityStatus =:status AND ap.active = TRUE " + "GROUP BY "
			+ "   f.psdArea")
	public List<PropertyCount> findPsdAreaCount(@Param("cid") String cid, @Param("status") ActivityStatus status);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, PSDArea psdArea, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, String gradeCid, PSDArea psdArea, ActivityStatus status,
			Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, PSDArea psdArea, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndActivityFocusAreasPsdAreaAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, PSDArea psdArea, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, FourS fourS, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndStudentGradeCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, String gradeCid, FourS fourS, ActivityStatus status, Date startDate,
			Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, FourS fourS, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndActivityFourSAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, FourS fourS, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, String focusAreaName, ActivityStatus status, Date startDate,
			Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, String gradeCid, String focusAreaName, ActivityStatus status,
			Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityFocusAreasNameAndActivityCidInAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, String focusAreaName, Collection<String> activityCids,
			ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String focusAreaName, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndActivityFocusAreasNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, String focusAreaName, ActivityStatus status, Date startDate,
			Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndStudentGradeCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String gradeCid, String activityName, ActivityStatus status, Date startDate,
			Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndStudentGradeCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, String gradeCid, String activityName, ActivityStatus status,
			Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByStudentSchoolCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String schoolCid, String activityName, ActivityStatus status, Date startDate, Date endDate);

	public Set<ActivityPerformed> findAllByTeacherCidAndStudentSchoolCidAndActivityNameAndActivityStatusAndDateOfActivityGreaterThanEqualAndDateOfActivityLessThanEqualAndActiveTrue(
			String teacherCid, String schoolCid, String activityName, ActivityStatus status, Date startDate,
			Date endDate);

//	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.id =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name")
//	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolIdAndGradesIdInAndActivityStatusIn(Long schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses );
//	com.nxtlife.mgs.view.GroupResponseByActivityName
//	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name")
//	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolCidAndGradesIdInAndActivityStatusIn(String schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses );
//	
//	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.id =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name having ap.activity.cid = ?4")
//	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolIdAndGradesIdInAndActivityStatusInAndClubId(Long schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses ,String activityCid );
//	
//	@Query("select ap.activity.name as criterionValue , COUNT(DISTINCT ap.id) as count , ap as responses from ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.activity.name having ap.activity.cid = ?4")
//	Set<GroupResponseBy<ActivityPerformed>> findAllBySchoolCidAndGradesIdInAndActivityStatusInAndClubId(String schoolId , Collection<Long> gradeIds ,Collection<ActivityStatus> statuses ,String activityCid );

	@Query("select ap from ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.student.id , ap.activity.name order by ap.student.name , ap.activity.name , ap.submittedOn ")
	public Set<ActivityPerformed> findAllBySchoolCidAndGradesIdInAndActivityStatusIn(String schoolId,
			Collection<Long> gradeIds, Collection<ActivityStatus> statuses);

	@Query("select ap from ActivityPerformed ap  where ap.student.school.cid =?1 and ap.student.grade.id in ?2 and ap.activityStatus in ?3 and ap.active = true group by ap.student.id , ap.activity.name having ap.activity.cid = ?4 order by ap.student.name , ap.activity.name , ap.submittedOn ")
	public Set<ActivityPerformed> findAllBySchoolCidAndGradesIdInAndActivityStatusInAndClubId(String schoolId,
			Collection<Long> gradeIds, Collection<ActivityStatus> statuses, String activityCid);

	public Boolean existsByActivityCidAndActive(String activityCid, Boolean active);

	public Boolean existsByActivityCidAndStudentSchoolCidInAndActive(String activityCid, Collection<String> schoolIds,
			Boolean active);
}
