package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.ServletContextResource;

import com.ibm.as400.access.AS400SecurityException;
//import com.ibm.watson.developer_cloud.*;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import com.restfb.types.send.Bubble;
import com.restfb.types.send.ButtonTemplatePayload;
import com.restfb.types.send.BuyButton;
import com.restfb.types.send.CallButton;
import com.restfb.types.send.DefaultAction;
import com.restfb.types.send.GenericTemplatePayload;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.ImageAspectRatioEnum;
import com.restfb.types.send.ListTemplatePayload;
import com.restfb.types.send.ListViewElement;
import com.restfb.types.send.MediaAttachment;
import com.restfb.types.send.Message;
import com.restfb.types.send.NestedButton;
import com.restfb.types.send.PersistentMenu;
import com.restfb.types.send.PostbackButton;
import com.restfb.types.send.ReceiptTemplatePayload;
import com.restfb.types.send.SendResponse;
import com.restfb.types.send.SenderActionEnum;
import com.restfb.types.send.TemplateAttachment;
import com.restfb.types.send.WebButton;
import com.restfb.types.send.WebviewHeightEnum;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessageItem;
import com.restfb.types.webhook.messaging.MessagingItem;
import com.seneca.TestWatson.WatsonConv;
import com.example.demo.model.CalliSeries;
import com.example.demo.model.Dial;
import com.example.demo.model.Gather;
import com.example.demo.model.PostToSkype;
import com.example.demo.model.Product;
import com.example.demo.model.Say;
import com.example.demo.model.TwilioCallFwd;
import com.example.demo.model.TwilioLog;
import com.example.demo.model.Twiml;
import com.example.demo.model.TwimlResponse;
import com.example.demo.service.ProductService;
import org.apache.catalina.connector.Request;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//@Controller
@RestController
/*@RequestMapping("/product")*/
public class ProductController 
{
	@Autowired
	private ProductService productService;
	public Map context = new HashMap();
	public String accessToken = "add your own access code";
	public String verifyToken = "fbwatson";
	public String callFrom;
	public String Fromnm;
	
	/*@RequestMapping("/fbtest")*/
	public void getfb(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String hubToken = request.getParameter("hub.verify_token");
		String hubChallenge = request.getParameter("hub.challenge");
		
		if(verifyToken.equals(hubToken))
		{
			response.getWriter().write(hubChallenge);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			response.getWriter().write("incorrect token");
		}
}
	@RequestMapping("/fbtest")
	public void postfb(HttpServletRequest request, HttpServletResponse response) throws IOException, AS400SecurityException
	{
		String hubToken = request.getParameter("hub.verify_token");
		String hubChallenge = request.getParameter("hub.challenge");
		
		if(verifyToken.equals(hubToken))
		{
			response.getWriter().write(hubChallenge);
			response.getWriter().flush();
			response.getWriter().close();
		} else {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = request.getReader();
		String line = "";
		while ((line=br.readLine())!= null)
		sb.append(line);
		JsonMapper mapper = new DefaultJsonMapper();
		WebhookObject webhookObj = mapper.toJavaObject(sb.toString(), WebhookObject.class);
		for(WebhookEntry entry: webhookObj.getEntryList())
		{
		System.out.println("webhookObj entry = " + entry);
		
			if(entry.getMessaging()!=null)
			{
				for(MessagingItem mItem: entry.getMessaging())
				{
					String senderId = mItem.getSender().getId();
					System.out.println("Sender Id=" + senderId);
					IdMessageRecipient recipient = new IdMessageRecipient(senderId);
					// Recieve Text Message 
					if(mItem.getMessage()!=null && mItem.getMessage().getText()!= null) 
					{
						WatsonConv wc = new WatsonConv();
						String rcvdata = mItem.getMessage().getText();
						String opdata = wc.mainMethod(rcvdata, context);
						System.out.println("prod controller ip-" + rcvdata);
						System.out.println("prod controller op-" + opdata);
				        context.putAll(wc.rtnContext());
						String userid = (String) context.get("userid"); 
//						if ( userid != null)
//				        {
//				        	CalliSeries cis = new CalliSeries();
//				        	cis.callStoredProc(userid);
//				        }
						String chk1 = "Catch you later";
						String chk2 = "Nice Talking to you Danny. Bye";
						if (opdata.trim().equals(chk1.trim())  || opdata.trim().equals(chk2.trim()))
						{
							sendFeedback(recipient);
							opdata = " ";
						}
						if (opdata != " ") { System.out.println("opdata =" + opdata);
							sendJustMsg(recipient, new Message(opdata.trim()));}
				    }
					else
					{ 
						if (mItem.getPostback() != null)
						{
							
								System.out.println(" Payload =" + mItem.getPostback().getPayload());
								String fdback = mItem.getPostback().getPayload();
								System.out.println("fdback="+fdback.trim());
								if (fdback.trim().equals("great")){
									sendMessage(recipient, new Message("I know I am awesome... ;-)"));}
								if (fdback.trim().equals("neutral")){
									sendMessage(recipient, new Message("I will see how I can impress you next time... B-)"));}
								if (fdback.trim().equals("bad")){
									sendMessage(recipient, new Message("No way... :-O I am too good to be bad. Anyways will see what I can do.."));}
						}
					}
				}
			}
		}
		}
	}
	
	void sendFeedback(IdMessageRecipient recipient)
	{
FacebookClient pageClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_6);
		
		ButtonTemplatePayload bt = new ButtonTemplatePayload("Please give us your feedback before you leave the chat");
		PostbackButton pbb1 = new PostbackButton("Great", "great");
		PostbackButton pbb2 = new PostbackButton("Neutral", "neutral");
		PostbackButton pbb3 = new PostbackButton("Bad", "bad");
		bt.addButton(pbb1);
		bt.addButton(pbb2);
		bt.addButton(pbb3);
		
		TemplateAttachment templateAttachment = new TemplateAttachment(bt);
		Message imageMessage = new Message(templateAttachment);
		SendResponse resp3 = pageClient.publish("me/messages", SendResponse.class,
			     Parameter.with("recipient", recipient), // the id or phone recipient
				 Parameter.with("message", imageMessage));
	}
	
	void sendJustMsg(IdMessageRecipient recipient, Message message)
	{
		
		FacebookClient msgClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_6);
		
		Parameter senderActionParam = Parameter.with("sender_action", SenderActionEnum.typing_on);
		Parameter recipientParam = Parameter.with("recipient", recipient);
		SendResponse resp = msgClient.publish("me/messages", SendResponse.class,
		     senderActionParam, // the sender action
		     recipientParam); // the recipient
		
		SendResponse resp3 = msgClient.publish("me/messages", SendResponse.class,
		     Parameter.with("recipient", recipient), // the id or phone recipient
			 Parameter.with("message", message));
		
	}
	
	void sendMessage(IdMessageRecipient recipient, Message message)
	{
		FacebookClient pageClient = new DefaultFacebookClient(accessToken, Version.VERSION_2_6);
		
	/*	
	 * List View Button Images
	 * CallButton cbt = new CallButton("Representative","+18174422151");
		ListViewElement lve1 = new ListViewElement("List View1");
		lve1.setSubtitle("No idea1");
		lve1.setImageUrl("https://media.glassdoor.com/sqll/445837/seneca-global-squarelogo-1419412200176.png");
		lve1.addButton(cbt);
		ListViewElement lve2 = new ListViewElement("List View2");
		lve2.setSubtitle("No idea2");
		lve2.setImageUrl("https://media.glassdoor.com/sqll/445837/seneca-global-squarelogo-1419412200176.png");
		ListViewElement lve3 = new ListViewElement("List View3");
		lve3.setSubtitle("No idea3");
		lve3.setImageUrl("https://media.glassdoor.com/sqll/445837/seneca-global-squarelogo-1419412200176.png");
		List<ListViewElement> lve = new ArrayList<ListViewElement>();
		lve.add(lve1);
		lve.add(lve2);
		lve.add(lve3);
				
		ListTemplatePayload ltp = new ListTemplatePayload(lve);
		ltp.addButton(cbt);
		TemplateAttachment templateAttachment = new TemplateAttachment(ltp);
		Message imageMessage = new Message(templateAttachment);
		*/

		// Generic Template Payload with Bubble view of image
	    GenericTemplatePayload payload = new GenericTemplatePayload();
	    payload.setImageAspectRatio(ImageAspectRatioEnum.square);
		// Create a bubble with a web button
		Bubble firstBubble = new Bubble("SENECA GLOBAL");
		WebButton webButton = new WebButton("Visit our Website", "https://www.senecaglobal.com/");
		firstBubble.addButton(webButton);
		firstBubble.setImageUrl("https://media.glassdoor.com/sqll/445837/seneca-global-squarelogo-1419412200176.png");
		firstBubble.setSubtitle("IT Services");
		/*DefaultAction asd = new DefaultAction("Default Action");
		firstBubble.setDefaultAction(asd);*/
		// Create a bubble with a postback button
		Bubble secondBubble = new Bubble("IT Support");
		CallButton cb = new CallButton("Call Support","+18174422151");
		secondBubble.addButton(cb);
		secondBubble.setImageUrl("https://n6-img-fp.akamaized.net/free-vector/support-with-icons_1212-151.jpg?size=338&ext=jpg");
		secondBubble.setSubtitle("You can reset the password by calling this number");
		/*DefaultAction asd2 = new DefaultAction("Default Action");
		secondBubble.setDefaultAction(asd2);*/
		payload.addBubble(firstBubble);
		payload.addBubble(secondBubble);

		TemplateAttachment templateAttachment = new TemplateAttachment(payload);
		Message imageMessage = new Message(templateAttachment);
		
		Parameter senderActionParam = Parameter.with("sender_action", SenderActionEnum.typing_on);
		Parameter recipientParam = Parameter.with("recipient", recipient);
		SendResponse resp = pageClient.publish("me/messages", SendResponse.class,
		     senderActionParam, // the sender action
		     recipientParam); // the recipient
		
		SendResponse resp3 = pageClient.publish("me/messages", SendResponse.class,
			     Parameter.with("recipient", recipient), // the id or phone recipient
				 Parameter.with("message", message));

		Parameter senderActionParam2 = Parameter.with("sender_action", SenderActionEnum.typing_on);
		Parameter recipientParam2 = Parameter.with("recipient", recipient);
		SendResponse resp4 = pageClient.publish("me/messages", SendResponse.class,
		     senderActionParam, // the sender action
		     recipientParam); // the recipient
		
		SendResponse resp2 = pageClient.publish("me/messages", SendResponse.class,
			     Parameter.with("recipient", recipient), // the id or phone recipient
				 Parameter.with("message", imageMessage));
		
		
	}

	void sendMessage2(IdMessageRecipient recipient, Message message)
	{
		FacebookClient pageClient2 = new DefaultFacebookClient(accessToken, Version.VERSION_2_6);

		GenericTemplatePayload payload = new GenericTemplatePayload();

		// Create a bubble with a web button
		Bubble firstBubble = new Bubble("Chiranjeevi");
		PostbackButton firstButton = new PostbackButton("Chiranjeevi", "Img1");
		firstBubble.addButton(firstButton);
		firstBubble.addButton(firstButton);
		firstBubble.addButton(firstButton);
		firstBubble.setImageUrl("https://tollywoodmegastar.files.wordpress.com/2010/04/original_chiru_47920581dcba6.jpg");
		firstBubble.setSubtitle("Mega Star");
		/*DefaultAction asd = new DefaultAction("Default Action");
		firstBubble.setDefaultAction(asd);*/
		// Create a bubble with a postback button
		Bubble secondBubble = new Bubble("Power Star");
		PostbackButton secondButton = new PostbackButton("Chiranjeevi", "Img2");
		secondBubble.addButton(secondButton);
		secondBubble.addButton(secondButton);
		secondBubble.addButton(secondButton);
		secondBubble.setImageUrl("https://tollywoodmegastar.files.wordpress.com/2010/04/original_chiru_47920581dcba6.jpg");
		secondBubble.setSubtitle("Mega Star");
		/*DefaultAction asd2 = new DefaultAction("Default Action");
		secondBubble.setDefaultAction(asd2);*/
		payload.addBubble(firstBubble);
		payload.addBubble(secondBubble);

		TemplateAttachment templateAttachment = new TemplateAttachment(payload);
		Message imageMessage = new Message(templateAttachment);
		
		Parameter senderActionParam = Parameter.with("sender_action", SenderActionEnum.typing_on);
		Parameter recipientParam = Parameter.with("recipient", recipient);
		SendResponse resp = pageClient2.publish("me/messages", SendResponse.class,
		     senderActionParam, // the sender action
		     recipientParam); // the recipient
		SendResponse resp2 = pageClient2.publish("me/messages", SendResponse.class,
			     Parameter.with("recipient", recipient), // the id or phone recipient
				 Parameter.with("message", imageMessage));
		
		SendResponse resp3 = pageClient2.publish("me/messages", SendResponse.class,
		     Parameter.with("recipient", recipient), // the id or phone recipient
			 Parameter.with("message", message));
		
		
	}

	
	@RequestMapping(value = "/twilio", produces = {MediaType.APPLICATION_XML_VALUE })
	public TwimlResponse index() throws IOException, AS400SecurityException
	{
		context.clear();
		TwimlResponse tresp = new TwimlResponse();
		Gather xml = new Gather();
		xml.setTimeout("auto");
		xml.setAction("/completed");
		xml.setInput("speech");
		Say say = new Say();
		say.setSay("Welcome to Seneca Global Support. This is Alice, how can I help you?");
		say.setVoice("alice");
		xml.setSay(say);
		tresp.setGather(xml);
		return tresp;
	}
    
	@RequestMapping(value = "/fwdCallBack", produces = {MediaType.APPLICATION_XML_VALUE })
	public TwimlResponse FwdCallBack() throws IOException, AS400SecurityException
	{
		context.clear();
		TwimlResponse tresp = new TwimlResponse();
		Gather xml = new Gather();
		xml.setTimeout("auto");
		xml.setAction("/completed");
		xml.setInput("speech");
		Say say = new Say();
		say.setSay("Welcome back again. Is there anything else I can assist you with?");
		say.setVoice("alice");
		xml.setSay(say);
		tresp.setGather(xml);
		return tresp;
	}
	
	@RequestMapping(value = "/completed", produces = {MediaType.APPLICATION_XML_VALUE })
	public Twiml recieve(@RequestParam(value = "SpeechResult") String calldata,@RequestParam(value = "From") String fromnm  ,@RequestParam(value = "Caller") String caller) throws IOException, AS400SecurityException 
	{
		Fromnm = fromnm;
		callFrom = caller;
		System.out.println("From   = " + Fromnm);
		System.out.println("Caller = " + callFrom);
		System.out.println("Completed URL Call = " + calldata);
		WatsonConv wc = new WatsonConv();
		String opdata = wc.mainMethod(calldata, context);
		if (!opdata.contains("Forwarding")) {
			/*System.out.println("Inside Regular Routine");*/
			TwimlResponse tresp = new TwimlResponse();
			tresp.setGather(getStaticValues(calldata));
			return tresp;	
		}
		TwilioCallFwd tcf = new TwilioCallFwd();
		Dial dial = new Dial();
		dial.setAction("/fwdCallBack");
		dial.setTimeout("auto");
		dial.setCallerId("918056131392");
		Say say = new Say();
		say.setSay(opdata);
		say.setVoice("alice");
		tcf.setSay(say);
		tcf.setDial(dial);
		System.out.println(tcf.toString());
		return tcf;
		
	}

	public Gather getStaticValues(String rcvdata) throws IOException, AS400SecurityException{
		
		WatsonConv wc = new WatsonConv();
		String opdata = wc.mainMethod(rcvdata, context);
		/*System.out.println(context);*/
        context.putAll(wc.rtnContext());
		String userid = (String) context.get("userid"); 
		/*if ( userid != null)
        {
        	if (userid.trim() != "") {
        	CalliSeries cis = new CalliSeries();
        	cis.callStoredProc(userid);
        	TwilioLog tlog = new TwilioLog();
        	System.out.println(tlog.insertRecord(Fromnm, callFrom, userid));
        	}
        }*/
			Gather xml = new Gather();
			xml.setTimeout("auto");
			xml.setAction("/completed");
			xml.setInput("speech");
			Say say = new Say();
			say.setSay(opdata.trim());
			say.setVoice("alice");
			xml.setSay(say);
			return xml;
		
	}
	
	@RequestMapping(value = "/image", method = RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE)
	public void getImageAsResource(HttpServletResponse response) throws IOException{
		ClassPathResource imgFile = new ClassPathResource("templates/product/Robot.PNG");
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
	}
	
	@RequestMapping("/callFwd")
	public TwilioCallFwd callFwd() {
		TwilioCallFwd tcf = new TwilioCallFwd();
		Dial dial = new Dial();
		dial.setAction("/completed");
		dial.setTimeout("30");
		dial.setCallerId("918056131392");
		tcf.setDial(dial);
		Say say = new Say();
		say.setSay("Forwarding the call");
		say.setVoice("alice");
		tcf.setSay(say);
		System.out.println(tcf.toString());
		return tcf;
	} 
	/*@RequestMapping(value = "detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable("id") Integer id, ModelMap modelMap) {
		modelMap.put("product", productService.find(id));
		return "product/detail";
	}
	
	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public String delete(@PathVariable("id") Integer id, ModelMap modelMap) {
		productService.deleteProduct(id);
		modelMap.put("products",  productService.findAll());
		return "product/index";
	}
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String addProduct(@ModelAttribute Product prod, ModelMap modelMap) {
		productService.addProduct(prod);
		modelMap.put("products", productService.findAll());
		return "product/index";
	}
	
	@GetMapping(value="/find",  produces=MediaType.APPLICATION_JSON_VALUE)
    public Collection<Product> findAllUsers() {
  Collection<Product> product = productService.findAll();
        return product;
    }
	
	*/
	/*@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("products", productService.findAll());
		return "product/addProduct";
	}*/
	@RequestMapping(
		    value = "/skype", 
		    method = RequestMethod.POST)
		/*public void process(@RequestBody Map<String, Object> payload)*/ 
		public void process(@RequestBody String payload)
		    throws Exception {

			String str = payload;
			Object ob = new JSONParser().parse(str);
			JSONObject jo = (JSONObject) ob;
			String type = (String) jo.get("type");
			System.out.println(type.equalsIgnoreCase("message"));
			if (type.equalsIgnoreCase("message")) {
			String text = (String) jo.get("text");
			WatsonConv wc = new WatsonConv();
			String opdata = wc.mainMethod(text, context);
			System.out.println(opdata);
		
			System.out.println(payload);
			PostToSkype pts = new PostToSkype();
			pts.recievePayload(payload, opdata);
			}
		
			
		}	
}
