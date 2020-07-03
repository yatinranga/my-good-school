package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.mgs.entity.activity.Certificate;
import com.nxtlife.mgs.view.CertificateResponse;

public interface CertificateRepository extends JpaRepository<Certificate, Long>{

	public boolean existsByStudentCidAndTitleAndDescriptionAndActiveTrue(String studentCid , String title , String description);
	
	public 	boolean existsByCidAndActiveTrue(String cid);
	
	public List<CertificateResponse> findAllByStudentCidAndActiveTrue(String studentCid);
	
	@Modifying
	@Query(value = "update Certificate c set c.active = ?2 where c.cid = ?1 and c.active = true")
	public int deleteByCidAndActiveTrue(String cid ,Boolean active);

	public Certificate findByCidAndActive(String cid, boolean active);
}
