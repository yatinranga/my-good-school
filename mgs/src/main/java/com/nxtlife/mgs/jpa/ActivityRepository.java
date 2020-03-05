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

	Activity getOneByNameAndActiveTrue(String name);
	
	String findCidByNameAndActiveTrue(String cid);

	Activity getOneByCidAndActiveTrue(String cid);

	List<Activity> findAllBySchoolsCidAndActiveTrue(String schoolCid);

	List<Activity> findAllByTeachersIdAndActiveTrue(Long teacherId);

	List<Activity> findAllByTeachersCidAndActiveTrue(String teacherCid);

	List<Activity> findAllBySchoolsCidAndTeachersIdAndActiveTrue(String schoolCid, Long teacherId);

	List<Activity> findAllBySchoolsCidAndTeachersCidAndActiveTrue(String schoolCid, String teacherCid);

	Activity findByNameAndActiveTrue(String name);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update Activity a set a.active = :active where a.cid = :cid")
	int updateActivitySetActiveByCid(@Param("active") Boolean active, @Param("cid") String cid);

	List<Activity> findAllByActiveTrue();

	List<Activity> findAllBySchoolsCidAndFourSAndAndActiveTrue(String schoolCid, FourS fourS);

	List<Activity> findAllBySchoolsCidAndFocusAreasCidAndActiveTrue(String schoolCid, String focusAreaCid);

	List<Activity> findAllBySchoolsCidAndFocusAreasPsdAreaAndActiveTrue(String schoolCid, PSDArea psdArea);

	Activity findByCidAndActiveTrue(String activityCid);

	Page<Activity> findAllByActiveTrue(Pageable paging);

	Activity getOneByCid(String activity);

	List<Activity> findAllByIsGeneralTrueAndActiveTrue();

}
