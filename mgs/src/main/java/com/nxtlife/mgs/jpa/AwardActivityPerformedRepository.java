package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.mgs.entity.activity.ActivityPerformed;
import com.nxtlife.mgs.entity.school.AwardActivityPerformed;
import com.nxtlife.mgs.util.AwardActivityPerformedId;

public interface AwardActivityPerformedRepository extends JpaRepository<AwardActivityPerformed, Long>{

	AwardActivityPerformed findByAwardCidAndActivityPerformedCidAndActiveTrue(String awardCid , String activityPerformedCid);
	
	List<AwardActivityPerformed> findAllByAwardCidAndIsVerifiedTrueAndActiveTrue(String awardCid);
	
	List<AwardActivityPerformed> findAllByAwardActivityPerformedIdAndActiveTrue(AwardActivityPerformedId compositeId);

	List<AwardActivityPerformed> findAllByAwardTeacherSchoolCidAndIsVerifiedFalseAndAwardTeacherSchoolActiveTrueAndActiveTrue(
			String schoolCid);

	List<AwardActivityPerformed> findAllByActiveTrue();
	
	@Query(value = "SELECT * FROM mgs.award_activity_performed a where (select extract(year from a.date_of_receipt)) = :yearOfActivity ",nativeQuery = true)
	List<AwardActivityPerformed> findAllByYearOfActivity(@Param("yearOfActivity") String yearOfActivity);
}
