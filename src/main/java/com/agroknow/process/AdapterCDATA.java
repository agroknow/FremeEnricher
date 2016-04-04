package com.agroknow.process;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdapterCDATA extends XmlAdapter<String, String> {
	
    @Override
    public String marshal(String arg0) throws Exception {
    	return arg0;
        //return "<![CDATA[" + arg0 + "]]>";
    }
    @Override
    public String unmarshal(String arg0) throws Exception {
        return arg0;
    }

}
