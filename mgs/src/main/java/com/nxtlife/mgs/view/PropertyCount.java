package com.nxtlife.mgs.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.nxtlife.mgs.enums.FourS;
import com.nxtlife.mgs.enums.PSDArea;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class PropertyCount {

	private Object name;
	private Long value;

	public PropertyCount(FourS name, Long value) {
		this.name = name.toString();
		this.value = value;
	}

	public PropertyCount(PSDArea name, Long value) {
		this.name = name.toString();
		this.value = value;
	}

	public PropertyCount(String name, Long value) {
		this.name = name.toString();
		this.value = value;
	}

	public PropertyCount() {

	}

}
