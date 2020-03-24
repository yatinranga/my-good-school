package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.mgs.entity.activity.Certificate;
import com.nxtlife.mgs.view.CertificateResponse;

public interface CertificateRepository extends JpaRepository<Certificate, Long>{

	boolean existsByStudentCidAndTitleAndDescriptionAndActiveTrue(String studentCid , String title , String description);
	
	boolean existsByCidAndActiveTrue(String cid);
	
	List<CertificateResponse> findAllByStudentCidAndActiveTrue(String studentCid);
}
