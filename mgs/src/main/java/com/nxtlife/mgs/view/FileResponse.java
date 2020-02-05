package com.nxtlife.mgs.view;

import com.nxtlife.mgs.entity.activity.File;

public class FileResponse {
	
	private String id;
	private String name;
	private String url;
	private String extension;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public FileResponse(File file) {
		this.id = file.getCid();
		this.name = file.getName();
		this.extension = file.getExtension();
		this.url = file.getUrl();
	}
    public FileResponse() {
		
	}
}
