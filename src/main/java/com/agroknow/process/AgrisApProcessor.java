package com.agroknow.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.LocaleUtils;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.agroknow.freme.FREMEAnnotation;
import com.agroknow.freme.FREMEClient;
import com.agroknow.schema.agrisap.AgsCreatorConference;
import com.agroknow.schema.agrisap.AgsCreatorCorporate;
import com.agroknow.schema.agrisap.AgsCreatorPersonal;
import com.agroknow.schema.agrisap.AgsPublisherPlace;
import com.agroknow.schema.agrisap.AgsResources;
import com.agroknow.schema.agrisap.AgsSubjectThesaurus;
import com.agroknow.schema.agrisap.DcCoverage;
import com.agroknow.schema.agrisap.DcCreator;
import com.agroknow.schema.agrisap.DcDescription;
import com.agroknow.schema.agrisap.DcPublisher;
import com.agroknow.schema.agrisap.DcSubject;
import com.agroknow.schema.agrisap.DctermsAbstract;
import com.agroknow.schema.agrisap.DctermsSpatial;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;  

public class AgrisApProcessor implements DocumentProcessor {
	
	File file;
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser parser;
	JAXBContext context;
	FileInputStream xmlStream;
	InputSource xmlSource;
	AgsResources resource;
	Logger logger;  
	FileHandler logFileHandler;
	String outputFolder;
	Marshaller marshaller;
	Unmarshaller unmarshaller;
	UnmarshallerHandler unmarshallerHandler;
	XMLReader reader;
	List<FREMEAnnotation> annotations;
	
	
	public AgrisApProcessor(File file) {
		super();
		this.file = file;
	}

	@Override
	public void initializeProcessor(File file) {
		
		try {
		
			
			File folder = new File( file.getParent()+"/results/" );
		    if (!folder.exists()) {
		    		folder.mkdir();
		    }
		    outputFolder = folder.getAbsolutePath();
		    logger  = Logger.getLogger("Results");
		    logFileHandler = new FileHandler(outputFolder+"/results.log");
		    logger.setUseParentHandlers(false);
		    logger.addHandler(logFileHandler);
		    Formatter formatter = new LogProcessFormatter();  
	        logFileHandler.setFormatter(formatter);

	        logger.info("Running annotation routine on "+file.getAbsolutePath());
		    
			annotations= new ArrayList<FREMEAnnotation>();
			
			parser = factory.newSAXParser();
			reader = parser.getXMLReader();
			
			context = JAXBContext.newInstance("com.agroknow.schema.agrisap");
			
			unmarshaller = context.createUnmarshaller();
			unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
			reader.setContentHandler(unmarshallerHandler);	
			
			xmlStream = new FileInputStream(file.getAbsolutePath());
			xmlSource = new InputSource(xmlStream);
	        reader.parse(xmlSource);
	        
	        resource = (AgsResources) unmarshallerHandler.getResult();
	        
	        } catch (Exception e) {
				e.printStackTrace();
	        }
		
	}

	@Override
	public void enrichSubjects() {
		FREMEClient client = new FREMEClient("e-terminology/tilde");
		client.setLogger(logger);
		List<FREMEAnnotation> annotations = new ArrayList<FREMEAnnotation>();
		logger.info("========================= Enriching subject elements in file: "+file.getName()+" ========================= ");
		try {
			
			Marshaller marshaller = context.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    marshaller.setProperty( "com.sun.xml.internal.bind.characterEscapeHandler", new CharacterEscapeHandler() {  
		    	   @Override  
		    	   public void escape( char[] ac, int i, int j, boolean flag, Writer writer ) throws IOException  
		    	   {  
		    	    // do not escape  
		    	    writer.write( ac, i, j );  
		    	   }

		     });  
	        // Iterating through multiple <ags:resource> elements in the file
	        for (int i = 0; i < resource.getAgsResource().size(); i++) {
	        	
	        	Iterator iterator = resource.getAgsResource().get(i).getDcDescription().listIterator();
		        
		        // Iterating through <dc:creator> elements in <ags:resource>
		        while (iterator.hasNext()) {
		        	
		        	DcDescription description = (DcDescription) iterator.next();
		        	
		        	// Iterating through <ags:creatorPersonal>, <ags:creatorCorporate or <ags:creatorConference elements in <dc:creator>
		        	for (Object descriptionObject : description.getAgsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract()) {		        	
			        	try {
			        		
			        		DctermsAbstract a = (DctermsAbstract) descriptionObject;
			        		String language = "";
					    	if (a.getXmlLang()==null) {
								language = "en";
							}
							else {
								Locale b = LocaleUtils.toLocale(a.getXmlLang());
								//System.out.println();
								language = a.getXmlLang();
							}
			        		annotations.addAll(client.enrichSubjects( a, language ));
			        		this.annotations.addAll(annotations);
			        		
			        	}
			        	catch (Exception e) {
			        		//System.out.println(e.getMessage());
			        	}
			        	
			        	
		        	}
		        }
		        
				    logger.info("********************ANNOTATION RESULTS********************");

				    for (FREMEAnnotation annotation : annotations) {
				    		logger.info(annotation.toString());
				    		
				    		//create subject element with AGROVOC URI
			    			DcSubject uriSubject = new DcSubject();
				    		AgsSubjectThesaurus uriAgrovocSubject = new AgsSubjectThesaurus();
				    		uriAgrovocSubject.setXmlLang(annotation.getLanguage());
				    		uriAgrovocSubject.setScheme("ags:AGROVOC");
				    		uriAgrovocSubject.setvalue(annotation.getIdentRef());
				    		uriSubject.getAgsSubjectThesaurusOrAgsSubjectClassification().add(uriAgrovocSubject);
				    		resource.getAgsResource().get(i).getDcSubject().add(uriSubject);
				    		
				    		//create subject element with AGROVOC label
				    		DcSubject labelSubject = new DcSubject();
				    		AgsSubjectThesaurus labelAgrovocSubject = new AgsSubjectThesaurus();
				    		labelAgrovocSubject.setXmlLang(annotation.getLanguage());
				    		labelAgrovocSubject.setScheme("ags:AGROVOC");
				    		labelAgrovocSubject.setvalue(annotation.getLabel());
				    		labelSubject.getAgsSubjectThesaurusOrAgsSubjectClassification().add(labelAgrovocSubject);
				    		resource.getAgsResource().get(i).getDcSubject().add(labelSubject);
				    }
				    
				    logger.info("**************************************************************");
				    annotations.clear();
				    
		        }
	        marshaller.marshal( resource,  new FileOutputStream( outputFolder+"/"+file.getName() ) );
	        
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void enrichAuthors() {
		
		
		FREMEClient client = new FREMEClient("e-entity/freme-ner");
		client.setLogger(logger);
		logger.info("========================= Enriching author elements in file: "+file.getName()+" ========================= ");
		List<Annotatable> creatorElements = new ArrayList<Annotatable>();
		List<String> locations = new ArrayList<String>(); 
		List<String> subjects = new ArrayList<String>(); 
		List<FREMEAnnotation> annotations = new ArrayList<FREMEAnnotation>();
		
		try {
			
			Marshaller marshaller = context.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		    
	        // Iterating through multiple <ags:resource> elements in the file
	        for (int i = 0; i < resource.getAgsResource().size(); i++) {
	        	
		        Iterator iterator = resource.getAgsResource().get(i).getDcCreator().listIterator();
		        
		        // Iterating through <dc:creator> elements in <ags:resource>
		        while (iterator.hasNext()) {
		        	
		        	DcCreator creator = (DcCreator) iterator.next();
		        	
		        	// Iterating through <ags:creatorPersonal>, <ags:creatorCorporate or <ags:creatorConference elements in <dc:creator>
		        	for (Object creatorObject : creator.getAgsCreatorPersonalOrAgsCreatorCorporateOrArgsCreatorConference()) {
		        	
			        	try {
			        		AgsCreatorPersonal c = (AgsCreatorPersonal) creatorObject;			        		
			        		creatorElements.add(c);
			        		
			        		
			        	}
			        	catch (Exception e) {
			        		//System.out.println(e.getMessage());
			        	}
			        	
			        	try {
			        		AgsCreatorCorporate c = (AgsCreatorCorporate)  creatorObject;
			        		creatorElements.add(c);
			        		ArrayList<String> corporateLocation = new ArrayList<String>();
			        		corporateLocation = (ArrayList<String>) client.getGeolocations(c.getvalue());
			        		locations.addAll(corporateLocation);
			        		
			        	}
			        	catch (Exception e) {
			        		//System.out.println(e.getMessage());
			        	}
			        	
			        	try {
			        		AgsCreatorConference c = (AgsCreatorConference)  creatorObject;
			        		//client.enrichAuthors( c.getvalue(), "orcid" );
			        	}
			        	catch (Exception e) {
			        		//System.out.println(e.getMessage());
			        	}
		        	}
		        }
		        
		        if ( creatorElements.size() > 0 ) {
		        	if (resource.getAgsResource().get(i).getDcPublisher() != null && !resource.getAgsResource().get(i).getDcPublisher().isEmpty()) {
		        		DcPublisher publisherElement = (DcPublisher) resource.getAgsResource().get(i).getDcPublisher().get(0);
		        		if (!publisherElement.getAgsPublisherNameOrAgsPublisherPlace().isEmpty()) {
		        			for (Object publisherObject : resource.getAgsResource().get(i).getDcPublisher().get(0).getAgsPublisherNameOrAgsPublisherPlace()) {
		        				try {
		        					
					        		AgsPublisherPlace place = (AgsPublisherPlace) publisherObject;
					        		locations.addAll(client.getGeolocations(place.getvalue()));
					        		
					        	}
					        	catch (Exception e) {
					        		//System.out.println(e.getMessage());
					        	}
		        			}
		        		}
		        	}
		        	
		        	if (resource.getAgsResource().get(i).getDcCoverage() != null && !resource.getAgsResource().get(i).getDcCoverage().isEmpty() ) {
		        		DcCoverage coverageElement = (DcCoverage) resource.getAgsResource().get(i).getDcCoverage();
		        		if (!coverageElement.getDctermsSpatialOrDctermsTemporal().isEmpty()) {
		        			for (Object coverageObject : resource.getAgsResource().get(i).getDcCoverage().get(0).getDctermsSpatialOrDctermsTemporal()) {
		        				try {
		        					
					        		DctermsSpatial coverage = (DctermsSpatial) coverageObject;
					        		locations.addAll(client.getGeolocations(coverage.getvalue()));
					        		
					        	}
					        	catch (Exception e) {
					        		//System.out.println(e.getMessage());
					        	}
		        			}
		        		}
		        	}
		        	
		        	if (resource.getAgsResource().get(i).getDcSubject() != null && !resource.getAgsResource().get(i).getDcSubject().isEmpty()) {
		        		for (DcSubject subjectElement : resource.getAgsResource().get(i).getDcSubject()) {
		        			for (Object subjectObject : subjectElement.getAgsSubjectThesaurusOrAgsSubjectClassification()) {
		        				try {
		        					
					        		AgsSubjectThesaurus subject = (AgsSubjectThesaurus) subjectObject;
					        		if (subject.getXmlLang().equals("en")) {
					        			subjects.add(subject.getvalue());
					        		}
					        		
					        	}
					        	catch (Exception e) {
					        		//e.printStackTrace();
					        	}
		        			}
		        		}
		        	}
		        	
				    for (Annotatable creator : creatorElements) {
				    	if (wordCount(creator.getvalue()) > 3) {
				    		if (creator.getClass().toString().equals("class com.agroknow.schema.agrisap.AgsCreatorCorporate")) {
				    			annotations.addAll(client.enrichAuthors( creator.getvalue(), "viaf", creator ));
				    			annotations.addAll(client.enrichAuthors( creator.getvalue(), "onld", creator ));
				    		}
				    		else {
				    			annotations.addAll(client.enrichAuthors( creator.getvalue(), "orcid", creator ));
				    		}
		        		}
				    	else {
				        	for (String location: locations) {
				        		annotations.addAll(client.enrichAuthors(normalizeName(creator.getvalue())+", "+location, "orcid", creator));
				        	}
				        	for (String subject: subjects) {
				        		if (creator.getClass().toString().equals("class com.agroknow.schema.agrisap.AgsCreatorCorporate")) {
					    			annotations.addAll(client.enrichAuthors( normalizeName(creator.getvalue())+", "+subject, "viaf", creator ));
					    			annotations.addAll(client.enrichAuthors( normalizeName(creator.getvalue())+", "+subject, "onld", creator ));
					    		}
					    		else {
					    			annotations.addAll(client.enrichAuthors(normalizeName(creator.getvalue())+", "+subject, "orcid", creator));
					    		}	
				        	}
				    	}
			        }
				    logger.info("********************ANNOTATION RESULTS********************");
				    
				    for (Annotatable creator : creatorElements) {
				    	
				    	double maxConfidenceORCID = 0.6;
				    	double maxConfidenceVIAF = 0.6;
				    	double maxConfidenceONLD = 0.6;
				    	
				    	FREMEAnnotation bestAnnotationORCID = null;
				    	FREMEAnnotation bestAnnotationVIAF = null;
				    	FREMEAnnotation bestAnnotationONLD = null;
				    	
				    	for (FREMEAnnotation annotation : annotations) {
				    		if (annotation.getAnnotatable() == creator) {
				    			if (annotation.getConfidence() > maxConfidenceORCID && annotation.getDataset().equals("orcid")) {
				    				maxConfidenceORCID = annotation.getConfidence();
				    				bestAnnotationORCID = annotation;
				    			}
				    			if (annotation.getConfidence() > maxConfidenceVIAF && annotation.getDataset().equals("viaf")) {
				    				maxConfidenceVIAF = annotation.getConfidence();
				    				bestAnnotationVIAF = annotation;
				    			}
				    			if (annotation.getConfidence() > maxConfidenceONLD && annotation.getDataset().equals("onld")) {
				    				maxConfidenceONLD = annotation.getConfidence();
				    				bestAnnotationONLD = annotation;
				    			}
				    		}
				    	}
				    	if (bestAnnotationORCID != null) {
				    		logger.info(bestAnnotationORCID.toString());
				    		this.annotations.add(bestAnnotationORCID);
						    bestAnnotationORCID.getAnnotatable().setOrcid(bestAnnotationORCID.getIdentRef());
						}
				    	if (bestAnnotationVIAF  != null) {
				    		logger.info(bestAnnotationVIAF.toString());
				    		this.annotations.add(bestAnnotationVIAF);
				    		bestAnnotationVIAF.getAnnotatable().setViaf(bestAnnotationVIAF.getIdentRef());
				    	}
				    	if (bestAnnotationONLD != null) {
				    		logger.info(bestAnnotationONLD.toString());
				    		this.annotations.add(bestAnnotationONLD);
				    		bestAnnotationONLD.getAnnotatable().setOnld(bestAnnotationONLD.getIdentRef());
				    	}
				    }
				    logger.info("**************************************************************");
				    annotations.clear();
				    
				    marshaller.marshal( resource,  new FileOutputStream( outputFolder+"/"+file.getName() ) );
		        }
	        }
	        
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void enrichProjects() {
		FREMEClient client = new FREMEClient("e-entity/freme-ner");
		client.setLogger(logger);
		logger.info("========================= Adding project links in file: "+file.getName()+" ========================= ");
		List<DctermsAbstract> abstractElements = new ArrayList<DctermsAbstract>();
		List<FREMEAnnotation> annotations = new ArrayList<FREMEAnnotation>();
		
		try {
	        
	        // Iterating through multiple <ags:resource> elements in the file
	        for (int i = 0; i < resource.getAgsResource().size(); i++) {
	        	
		        Iterator iterator = resource.getAgsResource().get(i).getDcDescription().listIterator();
		        
		        // Iterating through <dc:creator> elements in <ags:resource>
		        while (iterator.hasNext()) {
		        	
		        	DcDescription description = (DcDescription) iterator.next();
		        	
		        	// Iterating through <ags:creatorPersonal>, <ags:creatorCorporate or <ags:creatorConference elements in <dc:creator>
		        	for (Object abstractObject : description.getAgsDescriptionNotesOrAgsDescriptionEditionOrDctermsAbstract()) {
		        	
			        	try {
			        		DctermsAbstract a = (DctermsAbstract) abstractObject;	        		
			        		abstractElements.add(a);
			        	}
			        	catch (Exception e) {
			        		//System.out.println(e.getMessage());
			        	}
			        	
			        	
		        	}
		        }
		        	
				    for (DctermsAbstract abstractElement : abstractElements) {
				    	String language = "";
				    	if (abstractElement.getXmlLang()==null) {
							language = "en";
						}
						else {
							language = abstractElement.getXmlLang();
						}
					    annotations.addAll(client.enrichProjects( "cordis-fp7", abstractElement, language ));
					    		
			        }
				    logger.info("********************ANNOTATION RESULTS********************");
				    
				    for (Annotatable abstractElement : abstractElements) {
				    	
				    	double maxConfidenceCORDIS = 0.6;
				    	
				    	FREMEAnnotation bestAnnotationCORDIS = null;
				    	
				    	for (FREMEAnnotation annotation : annotations) {
				    		if (annotation.getAnnotatable() == abstractElement) {
				    			if (annotation.getConfidence() > maxConfidenceCORDIS) {
				    				maxConfidenceCORDIS = annotation.getConfidence();
				    				bestAnnotationCORDIS = annotation;
				    			}
				    		}
				    	}
				    	if (bestAnnotationCORDIS != null) {
				    		logger.info(bestAnnotationCORDIS.toString());
				    		this.annotations.add(bestAnnotationCORDIS);
				    	}
				    }
				    logger.info("**************************************************************");
				    annotations.clear();
				    
		        }
	        
	        
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		
		}
		
	}
	
	
	public static String normalizeName (String name) {
		
	    if (name.contains(",") || name.contains(";")) {
	    	
	    	name = name.replace(",", " ").replace(";", " ");
	    	name = name.trim();
	    	
		    StringBuilder reversedNameBuilder = new StringBuilder();
		    StringBuilder subNameBuilder = new StringBuilder();
	
		    for (int i = 0; i < name.length(); i++) {
	
		        char currentChar = name.charAt(i);
	
		        if (currentChar != ' ' && currentChar != '-') {
		            subNameBuilder.append(currentChar);
		        } else {
		            reversedNameBuilder.insert(0, currentChar + subNameBuilder.toString());
		            subNameBuilder.setLength(0);
		        }
		    }
		    
		    return reversedNameBuilder.insert(0, subNameBuilder.toString()).toString();
	    }
	    else {
	    	
	    	return name;
	    	
	    }
	}
	
	public static int wordCount(String s){
	    int wordCount = 0;

	    boolean word = false;
	    int endOfLine = s.length() - 1;

	    for (int i = 0; i < s.length(); i++) {
	        // if the char is a letter, word = true.
	        if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
	            word = true;
	            // if char isn't a letter and there have been letters before,
	            // counter goes up.
	        } else if (!Character.isLetter(s.charAt(i)) && word) {
	            wordCount++;
	            word = false;
	            // last word of String; if it doesn't end with a non letter, it
	            // wouldn't count without this.
	        } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
	            wordCount++;
	        }
	    }
	    return wordCount;
	}
	
	 
	
}


