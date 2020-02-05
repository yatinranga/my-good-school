package com.nxtlife.mgs.view;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.mgs.entity.activity.File;

public class FileRequest {

    private String name;
	
	private String id;
	
	private String url;
	
	private Boolean active;
	
	private String extension;
	
	private MultipartFile file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public File toEntity(File file) {
		file = file==null?new File():file;
		file.setName(this.name);
		file.setUrl(this.url);
		file.setActive(this.active);
		file.setExtension(this.extension);
		if(this.id !=null)
			file.setCid(this.id);
		return file;
	}

	public File toEntity() {
		return toEntity(null);
	}
	
	
}
