package com.nxtlife.mgs.jpa;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.user.Guardian;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
	public Guardian getOneByCid(String cid);

	public Guardian getOneByEmail(String email);

	public Guardian findByEmailOrMobileNumber(String email, String mobileNumber);

	public Guardian getOneByMobileNumber(String mobileNumber);

	public Guardian findByMobileNumberAndActiveTrue(String mobileNumber);

	public Guardian findByEmailAndActiveTrue(String email);

	public boolean existsByCidAndActiveTrue(String cid);

	public Guardian findByCidAndActiveTrue(String id);

	@Query(value = "select s.cid from Guardian s where s.user.id = ?1 and s.active = true")
	public String findCidByUserIdAndActiveTrue(Long userId);

	@Query(value = "select s.imageUrl from Guardian s where s.cid = ?1 ")
	public String findImageUrlByCid(String cid);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "update Guardian g set g.imageUrl = ?2 where g.cid = ?1")
	public int setImageUrlByCid(String cid, String imageUrl);

	@Query(value = "select s.user.cid from Guardian s where s.cid = ?1 and s.active = true")
	public String findUserCidByCidAndActiveTrue(String cid);

	public boolean existsByUserIdAndActiveTrue(Long id);

	public void deleteByUserId(Long id);

}
