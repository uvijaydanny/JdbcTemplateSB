package com.example.demo.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Say {

	private String say;
	
	private String voice;

	@XmlValue
	public String getSay() {
		return say;
	}

	public void setSay(String say) {
		this.say = say;
	}

	@XmlAttribute(name="voice")
	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
	
	
}
