package com.nxtlife.mgs.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.activity.TeacherActivityGrade;
import com.nxtlife.mgs.entity.user.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

	public Teacher getOneByCidAndActiveTrue(String cid);

	public Teacher getByUserId(Long userId);

	public int countByEmailAndActiveTrue(String email);

	public int countByUsernameAndActiveTrue(String username);

	public Teacher findByNameAndActiveTrue(String name);

	public Teacher findByCidAndActiveTrue(String cid);

	public Teacher findByCidAndIsCoachTrueAndActiveTrue(String cid);

	public Teacher findByCidAndIsClassTeacherTrueAndActiveTrue(String cid);

	public Teacher findByIdAndIsCoachTrueAndActiveTrue(Long id);

	public Teacher findByIdAndIsClassTeacherTrueAndActiveTrue(Long id);

	public Teacher findByMobileNumberAndActiveTrue(String mobileNumber);

	public Teacher findByUsernameAndActiveTrue(String username);

	public List<Teacher> findAllBySchoolCidAndActivitiesNameAndIsCoachTrueAndActiveTrue(String schoolCid,
			String activityName);

	public List<Teacher> findAllBySchoolCidAndActivitiesCidAndIsCoachTrueAndActiveTrue(String schoolCid,
			String activityCid);

	public List<Teacher> findAllBySchoolCidAndIsCoachTrueAndActiveTrue(String schoolCid);

	public List<Teacher> findAllBySchoolCidAndIsClassTeacherTrueAndActiveTrue(String schoolCid);

	public List<Teacher> findAllBySchoolCidAndActiveTrue(String schoolCid);

	public List<Teacher> findAllByIsCoachTrueAndActiveTrue();

	public List<Teacher> findAllByIsClassTeacherTrueAndActiveTrue();

	public Teacher findByIdAndActiveTrue(Long id);

	public Page<Teacher> findAllByActiveTrue(Pageable paging);

	public Boolean existsByCidAndActiveTrue(String teacherId);

	public List<Teacher> findAllBySchoolCidAndIsManagmentMemberTrueAndSchoolActiveTrueAndActiveTrue(String schoolCid);

	public String findNameByCidAndActiveTrue(String assignerCid);

	public boolean existsByUserIdAndIsManagmentMemberTrueAndActiveTrue(Long userId);

	public boolean existsByUserIdAndActiveTrue(Long userId);

	@Query(value = "select t.id from Teacher t where t.cid = :cid and t.active = true")
	public Long findIdByCidAndActiveTrue(@Param("cid") String cid);

	@Query(value = "select t.id from Teacher t where t.user.id = :userId and t.active = true")
	public Long getIdByUserIdAndActiveTrue(@Param("userId") Long userId);

	public Teacher findByUserIdAndActiveTrue(Long userId);

	@Query(value = "select s.cid from Teacher s where s.user.id = :userId and s.active = true")
	public String findCidByUserIdAndActiveTrue(@Param("userId") Long userId);

	@Modifying
	@Query(value = "update Teacher s set s.active = ?2 where s.cid = ?1 and s.active = true")
	public int deleteByCidAndActiveTrue(String cid, Boolean active);

	@Query(value = "select s.id as id , s.cid as cid from Teacher s where s.user.id = :userId and s.active = true")
	public Map<String, Object> findIdAndCidByUserIdAndActiveTrue(@Param("userId") Long userId);

	@Query(value = "select t.school.cid from Teacher t where t.cid = ?1 and t.active = true")
	public String findSchoolCidbyTeacherCid(String teacherCid);

	@Query(value = "select t.school.id from Teacher t where t.cid = ?1 and t.active = true")
	public Long findSchoolIdbyTeacherCid(String teacherCid);

	@Query(value = "select  s.teacherActivityGrades as clubs from Teacher s where s.cid = ?1 and s.active = true")
	public Map<String, Collection<TeacherActivityGrade>> findClubsByCidAndActiveTrue(String cid);

	@Query("Select t from Teacher t join t.grades g join t.teacherActivityGrades tag where t.school.cid = ?1 and (g.cid in ?2 or tag.grade.cid in ?2 ) and t.active = true")
	public List<Teacher> findAllBySchoolCidAndGradesCidOrTeacherActivityGradesGradeCidInAndActiveTrue(String schoolCid,
			Collection<String> gradesIds);

	public int deleteByUserId(Long id);

//	List<Teacher> findAllByActivitiesCid(String cid);

}
