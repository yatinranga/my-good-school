package com.nxtlife.mgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The @ConfigurationProperties(prefix = "file") annotation does its job on
 * application startup and binds all the properties with prefix file to the
 * corresponding fields.
 * 
 * @author ajay
 *
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

	private String uploadDir;
	private String supportedExtension;
	private String imageSupportedExtension;

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getSupportedExtension() {
		return supportedExtension;
	}

	public void setSupportedExtension(String supportedExtension) {
		this.supportedExtension = supportedExtension;
	}

	public String getImageSupportedExtension() {
		return imageSupportedExtension;
	}

	public void setImageSupportedExtension(String imageSupportedExtension) {
		this.imageSupportedExtension = imageSupportedExtension;
	}

}
