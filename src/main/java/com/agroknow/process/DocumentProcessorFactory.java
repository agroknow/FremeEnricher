package com.agroknow.process;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DocumentProcessorFactory {
	public DocumentProcessor getDocumentProcessor(File file){
		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			try {
				
				DocumentBuilder builder = factory.newDocumentBuilder();
				builder.parse(file);
				
				return new AgrisApProcessor(file);
				
			} catch (SAXException | IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
				
			}
			//return new Circle();
			catch (ParserConfigurationException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
				
			}
			
	      
	      
	   }
}
