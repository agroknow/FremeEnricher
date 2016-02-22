package com.agroknow.freme;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.agroknow.process.DocumentProcessor;
import com.agroknow.process.DocumentProcessorFactory;



/**
 * Run the 
 *
 */
public class StartFreme 
{
	static Options options = new Options();
	String[] extensions = { "xml" };
	
    public static void main( String[] args )
    {
    	// Open the provided directory
    	
    	
    	Option help = new Option( "help", "print this message" );
    	Option authorsOption = new Option( "a", "toggle author enrichment mode" );
    	Option projectsOption = new Option( "p", "toggle projects enrichment mode" );
    	Option subjectsOption = new Option( "s", "toggle subjects enrichment mode" );
    	Option mergeOption = new Option( "i", "ignore existing files in results folder" );
    	
    	Option inputFile = Option.builder("path")
                .longOpt("path")
                .numberOfArgs(1)
                .required(false)
                .type(String.class)
                .desc("absolute path that contains all XML files to be processed.")
                .build();
    	

    	options.addOption(help);
    	options.addOption(authorsOption);
    	options.addOption(projectsOption);
    	options.addOption(subjectsOption);
    	options.addOption(mergeOption);
    	options.addOption(inputFile);
    	
    	CommandLineParser parser = new DefaultParser();
    	DocumentProcessorFactory processorFactory = new DocumentProcessorFactory();
    	
    	try {
    	    // parse the command line arguments
    	    CommandLine line = parser.parse( options, args );

    	    if( line.hasOption( "help" ) ) {
    	        // print the value of block-size
    	    	HelpFormatter formater = new HelpFormatter();

    	    	formater.printHelp("Main", options);
    	    }

    	    if (line.getOptionValue("path") != null) {
    	    	// Get the path of the input directory;
        	    File inputDirectory = new File(line.getOptionValue("path")) ;
        	    String[] files = inputDirectory.list(new FilenameFilter() {
        	        public boolean accept(File directory, String fileName) {
        	            return fileName.endsWith(".xml");
        	        }
        	    });
        	    // Browse all XML files
            	for (int i =0; i < files.length; i++)
        		{
            		File fileEntry = new File(inputDirectory.getAbsolutePath()+"/"+files[i]);
            			boolean fileExists = (new File(fileEntry.getParent()+"/results/"+fileEntry.getName())).exists(); 		
            			int currentIndex = i+1;
            			if ( !fileExists || line.hasOption("i")) {
            				
            				System.out.print(currentIndex+"/"+files.length+" - Running annotation routine for file: "+fileEntry.getName()+"...");
            				DocumentProcessor processor = processorFactory.getDocumentProcessor(fileEntry);
	                		processor.initializeProcessor(fileEntry);
            				
                			if ( line.hasOption("a") ) { processor.enrichAuthors(); };
                			if ( line.hasOption("p") ) { processor.enrichProjects(); };
                			if ( line.hasOption("s") ) { processor.enrichSubjects(); };
                			System.out.print("OK!\n");
            			}
            			else {
            				System.out.println("Skipping file: \""+files[i]+"\". File is already annotated. Use \"-i\" if you want to override existing results.");
            			}
        		        
        		}
            	System.out.println("Annotation routine is over. Check \"results/results.log\" for more information.");
    	    }
    	    else {
    	    	System.out.println("No path is set. Please specify an input folder containing all files to be processed. Use -help for more information.");
    	    }
    	    
    	    
    	    
    	}
    	catch( ParseException exp ) {
    	    System.out.println( "Unexpected exception:" + exp.getMessage() );
    	}
    	
    }
}