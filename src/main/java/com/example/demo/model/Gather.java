package com.example.demo.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



public class Gather {

	
	private  String speechTimeout  ;
	
	
	private  String action;
	
	
	private  String input ;
	
	/*private  String Say ;*/
	private Say say;
	
	
@XmlAttribute(name="speechTimeout ")
	public String getTimeout() {
		return speechTimeout ;
	}

	public void setTimeout(String speechTimeout ) {
		this.speechTimeout  = speechTimeout ;
	}
	
	@XmlAttribute(name="action")
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	@XmlAttribute(name="input")
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	@XmlElement(name="Say")
	public Say getSay() {
		return say;
	}

	public void setSay(Say say) {
		this.say = say;
	}
	

}
