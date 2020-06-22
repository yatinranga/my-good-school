package com.nxtlife.mgs.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.enums.PSDArea;

@Repository
public interface FocusAreaRepository extends JpaRepository<FocusArea, Long> {

	public FocusArea getOneByCidAndActiveTrue(String cid);
	
	public FocusArea findByNameAndActiveTrue(String name);

	public FocusArea findByCidAndActiveTrue(String cid);
	
	public List<FocusArea> findAllByActivitiesSchoolsCidAndActiveTrue(String schoolCid);

	public List<FocusArea> findAllByActiveTrue();
	
	public boolean existsByCidAndActiveTrue(String cid);

	public FocusArea findByNameAndPsdAreaAndActiveTrue(String name, PSDArea psdArea);

	public FocusArea findByCid(String id);

	public FocusArea findByNameAndPsdArea(String name, PSDArea psdArea);

	public boolean existsByNameAndActiveTrue(String focusArea);
	}
