package com.example.demo.model;

import java.io.File;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

public class ClientAuth {

    final static String KEYSTORE_PASSWORD = "s3cr3t";

    static
    {
         File somethingFile = null;
         try {
         // deal with the src/main/resources
         // read why -
         ClassPathResource classPathResource = new ClassPathResource("client.jks");
           InputStream inputStream = classPathResource.getInputStream();
           somethingFile = File.createTempFile("test", ".txt");
           try {
            java.nio.file.Files.copy(
      inputStream, somethingFile.toPath(),
      java.nio.file.StandardCopyOption.REPLACE_EXISTING);
 //FileUtils.copyInputStreamToFile(inputStream, somethingFile);
           } finally {
                           //IOUtils.closeQuietly(inputStream);
                 inputStream.close();
           }
         } catch (java.lang.Exception e) {
         e.printStackTrace();
   }
         System.out.println("---- LOADED " + somethingFile);


         System.setProperty("javax.net.ssl.trustStore", somethingFile.getPath());
    }
}