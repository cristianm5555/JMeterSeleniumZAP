package com.testing.zap;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;

public class ZapConnection {
	private static final String ZAP_ADDRESS = "localhost";
	private static final int ZAP_PORT = 8080;
	private static final String ZAP_API_KEY ="koc19elaq4jme461ta2h6jn54e"; // Change this if you have set the apikey in ZAP via Options / API

	private static final String TARGET = "https://xss-game.appspot.com/level1";//"http://localhost:8080/bodgeit/";
	@Test() 
	public void TestZAPConnection() {
		 ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
		 try {
	            // Start spidering the target
	            System.out.println("Spider : " + TARGET);
	            // It's not necessary to pass the ZAP API key again, already set when creating the
	            // ClientApi.
	            ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
	            String scanid;
	            int progress;
	           

	            // The scan now returns a scan id to support concurrent scanning
	            scanid = ((ApiResponseElement) resp).getValue();

	            // Poll the status until it completes
	            while (true) {
	                Thread.sleep(1000);
	                progress =
	                        Integer.parseInt(
	                                ((ApiResponseElement) api.spider.status(scanid)).getValue());
	                System.out.println("Spider progress : " + progress + "%");
	                if (progress >= 100) {
	                    break;
	                }
	            }
	            System.out.println("Spider complete");

	            
	            // Give the passive scanner a chance to complete
	            Thread.sleep(2000);

	            System.out.println("Active scan : " + TARGET);
	            resp = api.ascan.scan(TARGET, "True", "False", null, null, null);

	            // The scan now returns a scan id to support concurrent scanning
	            scanid = ((ApiResponseElement) resp).getValue();

	            // Poll the status until it completes
	            while (true) {
	                Thread.sleep(5000);
	                progress =
	                        Integer.parseInt(
	                                ((ApiResponseElement) api.ascan.status(scanid)).getValue());
	                System.out.println("Active Scan progress : " + progress + "%");
	                if (progress >= 100) {
	                    break;
	                }
	            }
	            System.out.println("Active Scan complete");

	            System.out.println("Alerts:");
	            // System.out.println(new String(api.core.xmlreport(), StandardCharsets.UTF_8));
	            System.out.println(new String(api.core.htmlreport(),StandardCharsets.UTF_8));

	        } catch (Exception e) {
	            System.out.println("Exception : " + e.getMessage());
	            e.printStackTrace();
	        }
	}
}
