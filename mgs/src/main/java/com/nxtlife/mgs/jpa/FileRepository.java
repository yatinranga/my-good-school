package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.mgs.entity.activity.File;

public interface FileRepository extends JpaRepository<File, Long>{

	public File findByCidAndActiveTrue(String cid);
	
	@Query(value="select f.url from File f where f.active = true")
	public List<String> findAllUrls();
	
	@Transactional
	@Modifying( clearAutomatically = true)
	@Query(value="update File f set f.active = :active where f.cid = :cid")
	public int updateFileSetActiveByCid(@Param("active") Boolean active,@Param("cid") String cid);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update File f set f.active = :active where f.url = :url")
	public int updateFileSetActiveByUrl(@Param("active") Boolean active,@Param("url") String url);
	
//	int updateFileSetActiveBy
	
	public List<File> findAllByActivityPerformedCidAndActiveTrue(String activityPerformedCid);
	
	public File findByUrlAndActiveTrue(String url);

	public List<File> findAllByEventCidAndActiveTrue(String eventCid);
	
	@Query(value = "select f.cid from File f where f.event.cid = ?1 and f.active = true")
	public List<String> findAllCidByEventCidAndActiveTrue(String eventCid);
	
	@Query(value = "select f.cid from File f where f.activityPerformed.cid = ?1 and f.active = true")
	public List<String> findAllCidByActivityPerformedCidAndActiveTrue(String eventCid);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value="update File f set f.active = ?2 where f.cid in ?1")
	public int updateFileSetActiveByCidIn(List<String> cid ,Boolean active);
}
