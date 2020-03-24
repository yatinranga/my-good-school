package com.nxtlife.mgs.view;

import com.nxtlife.mgs.entity.activity.Certificate;

public class CertificateResponse {

	private String id;
	private String title;
	private String description;
	private String fourS;
	private String certificationAuthority;
	private String imageUrl;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFourS() {
		return fourS;
	}
	public void setFourS(String fourS) {
		this.fourS = fourS;
	}
	public String getCertificationAuthority() {
		return certificationAuthority;
	}
	public void setCertificationAuthority(String certificationAuthority) {
		this.certificationAuthority = certificationAuthority;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public CertificateResponse(Certificate certificate) {
		this.id = certificate.getCid();
		this.title = certificate.getTitle();
		this.description = certificate.getDescription();
		this.imageUrl = certificate.getImageUrl();
		this.certificationAuthority = certificate.getCertificationAuthority();
		if(certificate.getFourS() != null)
		  this.fourS = certificate.getFourS().toString();
	}
	
	public CertificateResponse() {
		
	}
	
}
