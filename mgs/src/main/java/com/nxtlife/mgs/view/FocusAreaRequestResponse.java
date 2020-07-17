package com.nxtlife.mgs.view;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.mgs.entity.activity.FocusArea;
import com.nxtlife.mgs.enums.PSDArea;
import com.nxtlife.mgs.ex.ValidationException;

@JsonInclude(content = Include.NON_NULL)
public class FocusAreaRequestResponse {

	@NotEmpty(message = "name of focus area cannot be null or empty.")
	private String name;

	@NotEmpty(message = "Psd Area cannot be null or empty.")
	private String psdArea;

	private String id;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPsdArea() {
		return psdArea;
	}

	public void setPsdArea(String psdArea) {
		this.psdArea = psdArea;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FocusArea toEntity(FocusArea focusArea) {
		focusArea = focusArea == null ? new FocusArea() : focusArea;
		focusArea.setName(this.name);
		focusArea.setCid(this.id);
		focusArea.setDescription(this.description);
		if (!PSDArea.matches(this.psdArea))
			throw new ValidationException(String.format(
					"Invalid psdArea (%s) it should be from list [Personal Development , Social Development]",
					this.psdArea));
		focusArea.setPsdArea(PSDArea.fromString(this.psdArea));
		return focusArea;
	}

	public FocusArea toEntity() {
		return toEntity(null);
	}

	public FocusAreaRequestResponse(FocusArea focusArea) {
		this.id = focusArea.getCid();
		this.name = focusArea.getName();
		this.description = focusArea.getDescription();
		if (focusArea.getPsdArea() != null)
			this.psdArea = focusArea.getPsdArea().getPsdArea();
	}

	public FocusAreaRequestResponse() {

	}
}
