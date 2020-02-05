package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.entity.activity.File;

public interface FileRepository extends JpaRepository<File, Long>{

	File findByCid(String cid);
	
	@Query(value="select f.url from File f where f.active = true")
	List<String> findAllUrls();
	
	@Transactional
	@Modifying(flushAutomatically = true , clearAutomatically = true)
	@Query(value="update File f set f.active = :active where f.cid = :cid")
	int updateFileSetActiveByCid(@Param("active") Boolean active,@Param("cid") String cid);
	
	@Transactional
	@Modifying(flushAutomatically = true , clearAutomatically = true)
	@Query(value="update File f set f.active = :active where f.url = :url")
	int updateFileSetActiveByUrl(@Param("active") Boolean active,@Param("url") String cId);
	
	List<File> findAllByActiveTrueAndActivityPerformedCid(String activityPerformedCid);
	
	File findByUrl(String url);
	
}
