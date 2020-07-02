package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.entity.activity.Activity;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

	public Activity getOneByNameAndActiveTrue(String name);
	
	@Query("select a.cid from Activity a where a.cid = :cid and a.active = true")
	public String findCidByNameAndActiveTrue(@Param("cid") String cid);

	public 	Activity getOneByCidAndActiveTrue(String cid);

	public 	List<Activity> findAllBySchoolsCidAndActiveTrue(String schoolCid);

	public List<Activity> findAllByTeachersIdAndActiveTrue(Long teacherId);

	public 	List<Activity> findAllByTeachersCidAndActiveTrue(String teacherCid);

	public 	List<Activity> findAllBySchoolsCidAndTeachersIdAndActiveTrue(String schoolCid, Long teacherId);

	public List<Activity> findAllBySchoolsCidAndTeachersCidAndActiveTrue(String schoolCid, String teacherCid);

	public Activity findByNameAndActiveTrue(String name);
	
	public Activity findByName(String name);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update Activity a set a.active = :active where a.cid = :cid")
	public int updateActivitySetActiveByCid(@Param("active") Boolean active, @Param("cid") String cid);

	public 	List<Activity> findAllByActiveTrue();

	public 	List<Activity> findAllBySchoolsCidAndFourSAndAndActiveTrue(String schoolCid, FourS fourS);

	public 	List<Activity> findAllBySchoolsCidAndFocusAreasCidAndActiveTrue(String schoolCid, String focusAreaCid);

	public 	List<Activity> findAllBySchoolsCidAndFocusAreasPsdAreaAndActiveTrue(String schoolCid, PSDArea psdArea);

	public 	Activity findByCidAndActiveTrue(String activityCid);
	
	public 	Activity findByCid(String activityCid);

	public 	Page<Activity> findAllByActiveTrue(Pageable paging);

	public 	Activity getOneByCid(String activity);

	public 	List<Activity> findAllByIsGeneralTrueAndActiveTrue();
	
	public 	List<Activity> findAllBySchoolsIdNotAndActiveTrue(Long schoolId);

	public 	boolean existsByCidAndActiveTrue(String activityCid);

	public 	boolean existsByNameAndActiveTrue(String activityName);
	
//	@Query("select a.id from Activity a where a.cid = :activityCid and a.school.id =:schoolId and a.active = true")
//	Long findCidByNameAndActiveTrue( @Param("activityCid") String activityCid , @Param("schoolId") Long schoolId);
	
	public 	boolean existsByCidAndSchoolsCidAndActiveTrue(String cid , String schoolCid);
	
	@Query(value = "select a.id from Activity a where a.cid = :cid and a.active = true")
	public Long findIdByCidAndActiveTrue(@Param("cid") String cid);

	public boolean existsByTeachersIdAndActiveTrue(Long teacherId);

	public boolean existsByNameAndCidNot(String name, String cid);

}
