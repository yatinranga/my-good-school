package com.nxtlife.mgs.entity.user;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
public class Role extends AbstractAuditable<Role,Long> implements Serializable {

    @Column(nullable = false)
    private String name;
    
    @NotNull
    @Column(unique = true)
    private String cid;
    
    private Boolean active;

    @Column
    private String description;

    @OneToMany(mappedBy = "roleForUser",cascade = CascadeType.REMOVE)
    private List<User> user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_privledge_authority",
            joinColumns = @JoinColumn(name = "role_id") , inverseJoinColumns = @JoinColumn(name = "authority_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id","authority_id"}))
    private Set<Authority> authorities;

    @PrePersist
    public void prePersist(){

        if(this.isNew())
            this.setCreatedDate(DateTime.now());
        else
            this.setLastModifiedDate(DateTime.now());
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

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void addAuthorities(Authority authority){
        if(authorities == null)
            authorities = new HashSet<>();
        authorities.add(authority);
    }
}
