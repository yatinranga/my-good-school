package com.nxtlife.mgs.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.nxtlife.mgs.entity.BaseEntity;

@Entity
public class Privilege extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	private String name;

	private String description;

	@NotNull
	@Column(unique = true)
	private String cId;

	public Privilege() {
	}

	public Privilege(Privilege p) {
		this.name = p.name;
		this.description = p.description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Privilege(String name, String description, @NotNull String cId) {
		this.name = name;
		this.description = description;
		this.cId = cId;
	}
	

}
