package com.nxtlife.mgs.entity.school;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class AwardType extends BaseEntity {

	@NotNull
	@Column(unique = true)
	private String name;
	
	private Boolean active = true;
	
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "awardType")
	private List<Award> awards;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public AwardType(String name, List<Award> awards) {
		super();
		this.name = name;
		this.awards = awards;
	}

	public AwardType() {
		super();
	}

	public AwardType(String name) {
		this.name = name;
	}
	
	
}
