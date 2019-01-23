package com.example.demo.model;

import java.sql.*;  
public class TwilioLog
{  
	public String insertRecord(String callfrom, String Caller, String userid)
	{  
	try
	{  
	Class.forName("com.mysql.jdbc.Driver");  
	Connection conn=DriverManager.getConnection(  
	"jdbc:mysql://localhost:3306/dannydb?autoReconnect=true&useSSL=false","root","root");  
	
	String insertQuery = "insert into TwilioLog(CallFrom, Caller, UserId) values(?,?,?)";
	PreparedStatement preparedStmt = conn.prepareStatement(insertQuery);
    preparedStmt.setString (1, callfrom.trim());
    preparedStmt.setString (2, Caller.trim());
    preparedStmt.setString(3, userid.trim());  
    preparedStmt.execute();
	conn.close();
	return "Y";
	}
	catch(Exception e){ System.out.println(e); return "N";}  
	}  
}  	
