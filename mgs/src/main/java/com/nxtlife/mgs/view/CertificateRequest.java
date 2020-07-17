package com.nxtlife.mgs.view;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.Certificate;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.ex.ValidationException;

public class CertificateRequest {

	private String id;

	@NotEmpty(message = "title cannot be null or empty.")
	private String title;

	private String description;

	@NotEmpty(message = "fourS cannot be null or empty.")
	private String fourS;

	@NotEmpty(message = "certificationAuthority cannot be null or empty.")
	private String certificationAuthority;

	@NotNull(message = "image cannot be null/empty")
	private MultipartFile image;

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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public Certificate toEntity() {
		return toEntity(null);
	}

	public Certificate toEntity(Certificate certificate) {
		certificate = certificate == null ? new Certificate() : certificate;
		certificate.setTitle(this.title);
		certificate.setDescription(this.description);
		certificate.setCertificationAuthority(this.certificationAuthority);

		if (!FourS.matches(this.fourS))
			throw new ValidationException(
					"Invalid value for field fourS , it should belong to list : [Skill ,Sport ,Study ,Service]");
		certificate.setFourS(FourS.valueOf(this.fourS));

		return certificate;
	}
}
