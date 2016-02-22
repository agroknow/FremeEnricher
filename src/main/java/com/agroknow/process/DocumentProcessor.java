package com.agroknow.process;

import java.io.File;

public interface DocumentProcessor {
	public void initializeProcessor(File file);
	public void enrichSubjects();
	public void enrichAuthors();
	public void enrichProjects();
}

