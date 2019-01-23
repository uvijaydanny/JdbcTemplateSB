package com.example.demo.model;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ibm.as400.access.AS400SecurityException;

public class CalliSeries {

	public String cntdta = "";
	public String callStoredProc(String rcvdata) throws AS400SecurityException, IOException
 {
  String rcvAudPth = "";
  Connection conn = null;
  
  CallableStatement stmt = null;
  ResultSet rs = null;
  try {

   String DRIVER = "com.ibm.as400.access.AS400JDBCDriver"; 
   String URL = "jdbc:as400://172.20.1.10;libraries=QGPL,QTEMP,TESTDUND,YAJL;naming=system;errors=full";
//socket timeout=100000

   //Connect to iSeries 
   Class.forName(DRIVER); 
   conn = DriverManager.getConnection(URL,"CMOVVA","cmovva"); 
   
   //Make sure the Job Description for the {user_id} profile has the library that has the MYNAMEQ //stored procedure
   stmt = conn.prepareCall("{call WATSON(?)}");
   stmt.setString(1, rcvdata);
   boolean hasresult = stmt.execute();
   /*System.out.println(hasresult);*/
   rs = stmt.getResultSet();
   while (rs.next()) {
	    rcvAudPth = rs.getString(1);
	    /*System.out.println("Message is: " + rcvAudPth);*/
	   } 
  } catch (ClassNotFoundException ex) {
   ex.printStackTrace();
  } catch (SQLException ex) {
   ex.printStackTrace();
  } finally {

   try {

    rs.close();
    stmt.close();
    conn.close();

   } catch (SQLException ex) {
    ex.printStackTrace();
   }

  }
return rcvAudPth;
 }
	
}