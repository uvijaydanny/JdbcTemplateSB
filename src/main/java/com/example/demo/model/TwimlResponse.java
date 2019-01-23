package com.example.demo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Response")
public class TwimlResponse implements Twiml {

	// TODO Auto-generated method stub
		
		private Gather Gather;
		
		@XmlElement(name="Gather")
		public Gather getGather() {
			return Gather;
		}

		public void setGather(Gather gather) {
			Gather = gather;
		}

		@Override
		public void twiml() {
			
			
		}

		
	
}
