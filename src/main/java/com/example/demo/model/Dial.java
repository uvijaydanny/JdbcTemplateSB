package com.example.demo.model;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
public class Dial {

	private String action;
	private String timeout;
	private String callerId;
	
	@XmlAttribute(name="speechTimeout")
	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	@XmlValue
	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	@XmlAttribute(name="action")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
