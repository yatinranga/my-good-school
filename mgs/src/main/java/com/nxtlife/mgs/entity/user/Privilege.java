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
	private String cid;

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

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Privilege(String name, String description, @NotNull String cid) {
		this.name = name;
		this.description = description;
		this.cid = cid;
	}
	
	public Privilege() {
	}

	public Privilege(Privilege p) {
		this.name = p.name;
		this.description = p.description;
	}

}
