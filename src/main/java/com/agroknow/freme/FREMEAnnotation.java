package com.agroknow.freme;

import com.agroknow.process.Annotatable;

public class FREMEAnnotation {

	private Annotatable annotatable;
	private double confidence;
	private String identRef;
	private String dataset;
	private String language;
	private String label;
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDataset() {
		return dataset;
	}
	public void setDataset(String dataset) {
		this.dataset = dataset;
	}
	public Annotatable getAnnotatable() {
		return annotatable;
	}
	public void setAnnotatable(Annotatable annotatable) {
		this.annotatable = annotatable;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public String getIdentRef() {
		return identRef;
	}
	public void setIdentRef(String identRef) {
		this.identRef = identRef;
	}
	public FREMEAnnotation(Annotatable annotatable, double confidence, String identRef, String dataset, String language, String label) {
		super();
		this.annotatable = annotatable;
		this.confidence = confidence;
		this.identRef = identRef;
		this.dataset = dataset;
		this.language = language;
		this.label = label;
	}
	
	public String toString() {
		return this.label+" ("+this.language+"): "+this.identRef+" ("+this.confidence+")";
	}
	
	
}
