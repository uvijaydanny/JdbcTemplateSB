package com.example.demo.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Response")
public class TwilioCallFwd implements Twiml {

		private Dial dial;
		private Say Say;
		
		@XmlElement(name="Say")
		public Say getSay() {
			return Say;
		}


		public void setSay(Say say) {
			Say = say;
		}

		@XmlElement(name="Dial")
		public Dial getDial() {
			return dial;
		}
		public void setDial(Dial dial) {
			this.dial = dial;
		}

		@Override
		public void twiml() {
			// TODO Auto-generated method stub
			
		}
}
