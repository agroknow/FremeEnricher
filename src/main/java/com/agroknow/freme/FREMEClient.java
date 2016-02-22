package com.agroknow.freme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.agroknow.process.Annotatable;

public class FREMEClient {
	private String service;
	private String informat = "text";
	private String outformat = "json-ld";
	private String mode;
	private Logger logger;
	
	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getInformat() {
		return informat;
	}

	public void setInformat(String informat) {
		this.informat = informat;
	}

	public String getOutformat() {
		return outformat;
	}

	public void setOutformat(String outformat) {
		this.outformat = outformat;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public FREMEClient(String service) {
		super();
		this.service = service;
	}

	public List<FREMEAnnotation> enrichAuthors(String input, String dataset, Annotatable creator) {
		
		ArrayList<FREMEAnnotation> creators = new ArrayList<FREMEAnnotation>();
		
		try {
			
			String uri = "http://api-dev.freme-project.eu/current/"+this.service+"/documents?"
					+ "input="+URLEncoder.encode(input,"UTF-8")+"&informat=text&outformat=json-ld&language=en&dataset="+dataset+"&mode=all";
			
			URL url = new URL(uri);
			logger.info("Calling FREME e-entity: "+uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
			    responseStrBuilder.append(inputStr);
			
		    int responseCode = connection.getResponseCode();
		    
		    switch (responseCode) {
		    	case 200:
		    		JSONObject root = new JSONObject(responseStrBuilder.toString());
		    		if (root.has("@graph")) {
		    			
		    		
		    		JSONArray annotations = (JSONArray) root.get("@graph");
		    		
		    		for ( Object annotationObject: annotations ) {
		    			JSONObject annotation = (JSONObject) annotationObject;
			    			if (annotation.has("taClassRef")) {
				    			switch (dataset) {
				    				case "viaf" :
				    					if (annotation.get("taClassRef").equals("http://nerd.eurecom.fr/ontology#Organization")) {
					    					if (annotation.has("taIdentRef")) {
					    						if (Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()) >= 0.60) {
						    						FREMEAnnotation annotatedCreator = new FREMEAnnotation(creator, Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()), annotation.get("taIdentRef").toString(), dataset, "en", annotation.getString("nif:anchorOf"));
						    						creators.add(annotatedCreator);
						    						logger.info(annotation.get("nif:anchorOf")+": "+annotation.get("taIdentRef"));
					    						}
					    					}
					    				}
				    				case "onld" :
				    					if (annotation.get("taClassRef").getClass().toString().equals("class org.json.JSONArray")) {
					    					if (annotation.getJSONArray("taClassRef").get(0).equals("http://dbpedia.org/ontology/Organisation") || annotation.getJSONArray("taClassRef").get(1).equals("http://dbpedia.org/ontology/Organisation")) {
						    					if (annotation.has("taIdentRef")) {
						    						if (Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()) >= 0.60) {
							    						FREMEAnnotation annotatedCreator = new FREMEAnnotation(creator, Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()), annotation.get("taIdentRef").toString(), dataset, "en", annotation.getString("nif:anchorOf"));
							    						creators.add(annotatedCreator);
							    						logger.info(annotation.get("nif:anchorOf")+": "+annotation.get("taIdentRef")+ " ("+annotation.get("itsrdf:taConfidence")+")");
						    						}
						    					}
						    				}
				    					}
				    				case "orcid" :
				    					if (annotation.get("taClassRef").getClass().toString().equals("class org.json.JSONArray")) {
					    					if (annotation.getJSONArray("taClassRef").get(0).equals("http://xmlns.com/foaf/0.1/Person") || annotation.getJSONArray("taClassRef").get(1).equals("http://xmlns.com/foaf/0.1/Person")) {
						    					if (annotation.has("taIdentRef")) {
						    						if (Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()) >= 0.60) {
							    						FREMEAnnotation annotatedCreator = new FREMEAnnotation(creator, Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()), annotation.get("taIdentRef").toString(), dataset, "en", annotation.getString("nif:anchorOf"));
							    						creators.add(annotatedCreator);
							    						logger.info(annotation.get("nif:anchorOf")+": "+annotation.get("taIdentRef")+ " ("+annotation.get("itsrdf:taConfidence")+")");
						    						}
						    					}
						    				}
				    					}
				    			}
			    			}
		    			}
		    		}
//		    	case 400:
//		    		System.out.println("Bad server request.");
//		    	case 502:
//		    		System.out.println("Service temporarily unavailable. Consult the FREME team.");
//		    	case 503:
//		    		System.out.println("Service temporarily unavailable. Try again later.");
		    }
		} catch (MalformedURLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		return creators;
	}
	
public List<FREMEAnnotation> enrichProjects(String dataset, Annotatable abstractElement, String language) {
		
		ArrayList<FREMEAnnotation> projects = new ArrayList<FREMEAnnotation>();
		
		try {
			
			String uri = "http://api-dev.freme-project.eu/current/"+this.service+"/documents?"
					+ "input="+URLEncoder.encode(abstractElement.getvalue(),"UTF-8")+"&informat=text&outformat=json-ld&language="+language+"&dataset="+dataset+"&mode=all&type=http://dbpedia.org/ontology/ResearchProject";
			
			URL url = new URL(uri);
			logger.info("Calling FREME e-entity: "+uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
			    responseStrBuilder.append(inputStr);
			
		    int responseCode = connection.getResponseCode();
		    
		    switch (responseCode) {
		    	case 200:
		    		JSONObject root = new JSONObject(responseStrBuilder.toString());
		    		if (root.has("@graph")) {
			    		JSONArray annotations = (JSONArray) root.get("@graph");
				    		for ( Object annotationObject: annotations ) {
				    			JSONObject annotation = (JSONObject) annotationObject;
				    			if (annotation.has("taClassRef")) {
					    			
					    					if (annotation.get("taClassRef").getClass().toString().equals("class org.json.JSONArray")) {
						    					if (annotation.getJSONArray("taClassRef").get(0).equals("http://dbpedia.org/ontology/ResearchProject") || annotation.getJSONArray("taClassRef").get(1).equals("http://dbpedia.org/ontology/ResearchProject")) {
							    					if (annotation.has("taIdentRef")) {
							    						if (Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()) >= 0.60) {
								    						FREMEAnnotation annotatedCreator = new FREMEAnnotation(abstractElement, Double.parseDouble(annotation.get("itsrdf:taConfidence").toString()), annotation.get("taIdentRef").toString(), dataset, "en", annotation.getString("nif:anchorOf"));
								    						projects.add(annotatedCreator);
								    						//System.out.println(annotation.get("nif:anchorOf")+": "+annotation.get("taIdentRef")+ " ("+annotation.get("itsrdf:taConfidence")+")");
							    						}
							    					}
							    				}
					    					}
				    			}
				    		}
		    		}
//		    	case 400:
//		    		System.out.println("Bad server request.");
//		    	case 502:
//		    		System.out.println("Service temporarily unavailable. Consult the FREME team.");
//		    	case 503:
//		    		System.out.println("Service temporarily unavailable. Try again later.");
		    }
		} catch (MalformedURLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		return projects;
	}

public List<FREMEAnnotation> enrichSubjects(Annotatable abstractElement, String language ) {
	
	ArrayList<FREMEAnnotation> subjects = new ArrayList<FREMEAnnotation>();
	Set<String> agrovocUris = new HashSet<String>();
	try {
		
		String uri = "http://api-dev.freme-project.eu/current/"+this.service+"?"
				+ "input="+URLEncoder.encode(abstractElement.getvalue(),"UTF-8")+"&informat=text&outformat=json-ld&source-lang="+language+"&target-lang="+language+"&domain=TaaS-1001";
		
		URL url = new URL(uri);
		logger.info("Calling FREME e-terminology: "+uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/ld+json;charset=UTF-8");
		
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
		StringBuilder responseStrBuilder = new StringBuilder();
		String inputStr;
		while ((inputStr = streamReader.readLine()) != null)
		    responseStrBuilder.append(inputStr);
		
	    int responseCode = connection.getResponseCode();
	    
	    switch (responseCode) {
	    	case 200:
	    		JSONObject root = new JSONObject(responseStrBuilder.toString());
	    		JSONArray annotations = (JSONArray) root.get("@graph");
	    		if ( annotations !=null ) {
		    		for ( Object annotationObject: annotations ) {
		    			JSONObject annotation = (JSONObject) annotationObject;
		    			if (annotation.has("taConfidence")) 	{
			    			
    						if (Double.parseDouble(annotation.get("taConfidence").toString()) >= 0.60) {
    							for ( Object annotationUnit : (JSONArray) root.get("@graph")) {
    								if (((JSONObject) annotationUnit).has("annotationUnit")) {
    									if ( ((JSONObject) annotationUnit).getString("annotationUnit").equals(annotation.get("@id").toString()) ) {
    										Object termInfoRef = ((JSONObject) annotationUnit).get("termInfoRef"); 
    										String agrovocUri = "";
    										if (termInfoRef.getClass().toString().equals("class org.json.JSONArray")) {
    											for ( Object refObject : (JSONArray) termInfoRef) {
    												try {
    													URL refUrl = new URL(refObject.toString());
    													agrovocUri = refObject.toString();
    												}
    												catch (MalformedURLException e) {
    													//e.printStackTrace();
    												}
    											}
    										}
    										else {
    											try {
													URL refUrl = new URL( ((JSONObject) annotationUnit).get("termInfoRef").toString() );
													agrovocUri = ((JSONObject) annotationUnit).get("termInfoRef").toString();
												}
												catch (MalformedURLException e) {
													//e.printStackTrace();
												}
    										}
    											
    										if (!agrovocUri.equals("")) {
	    										Double confidence = Double.parseDouble(annotation.get("taConfidence").toString());
	    										String annotationLanguage = annotation.getJSONObject("label").getString("@language");
	    										String annotationLabel = annotation.getJSONObject("label").getString("@value");
	    										
	    										FREMEAnnotation subject= new FREMEAnnotation(abstractElement, confidence, agrovocUri, "agrovoc", annotationLanguage, annotationLabel);
	    										if(agrovocUris.add(agrovocUri)) {
	        										subjects.add(subject);	
	    										}
    										}
    										
    										break;
    									}
    									
    								}
    							}
    						}
		    			}
		    		}
	    		}
//	    	case 400:
//	    		System.out.println("Bad server request.");
//	    	case 502:
//	    		System.out.println("Service temporarily unavailable. Consult the FREME team.");
//	    	case 503:
//	    		System.out.println("Service temporarily unavailable. Try again later.");
	    }
	} catch (MalformedURLException e) {
		
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	} catch (IOException e) {
	
		// TODO Auto-generated catch block
		e.printStackTrace();
	
		}
		
		return subjects;
	}


	public List<String> getGeolocations( String input ) {
			
			List<String> locations = new ArrayList<String>();
			
			try {
					
					String uri = "http://api-dev.freme-project.eu/current/"+this.service+"/documents?"
							+ "input="+URLEncoder.encode(input,"UTF-8")+"&informat=text&outformat=json-ld&language=en&dataset=geopolitical&mode=all";
					
					URL url = new URL(uri);
					logger.info("Calling FREME e-entity: "+uri);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					
					BufferedReader streamReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); 
					StringBuilder responseStrBuilder = new StringBuilder();
					String inputStr;
					while ((inputStr = streamReader.readLine()) != null)
					    responseStrBuilder.append(inputStr);
					
				    int responseCode = connection.getResponseCode();
				    
				    switch (responseCode) {
				    	case 200:
				    		JSONObject root = new JSONObject(responseStrBuilder.toString());
				    		JSONArray annotations = (JSONArray) root.get("@graph");
				    		
				    		for ( Object annotationObject: annotations ) {
				    			JSONObject annotation = (JSONObject) annotationObject;
				    			if (annotation.has("taClassRef")) {
				    				if (annotation.get("taClassRef").getClass().toString().equals("class org.json.JSONArray")) {
				    					if (annotation.getJSONArray("taClassRef").get(0).equals("http://nerd.eurecom.fr/ontology#Location") || annotation.getJSONArray("taClassRef").get(1).equals("http://nerd.eurecom.fr/ontology#Location")) {
					    					if (annotation.has("taIdentRef") && annotation.has("nif:anchorOf")) {
					    						logger.info(annotation.get("nif:anchorOf")+": "+annotation.get("taIdentRef")+ " ("+annotation.get("itsrdf:taConfidence")+")");
					    						String location = annotation.get("nif:anchorOf").toString();
					    						locations.add(location);
					    					}
					    				}
			    					}
				    			}
				    		}
	//			    	case 400:
	//			    		System.out.println("Bad server request.");
	//			    	case 502:
	//			    		System.out.println("Service temporarily unavailable. Consult the FREME team.");
	//			    	case 503:
	//			    		System.out.println("Service temporarily unavailable. Try again later.");
				    }
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return locations;
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


}
