package com.tiaa.rest.mytest.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.SAXException;

public class FileUtil {
	
	private final static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static JSONObject convertXmlToJSONObject(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		String str = null;
		while ((line = br.readLine()) != null) {
			str += line;
		}
		return XML.toJSONObject(str);
	}
	
	public static boolean validateXMLSchema(File xsdFile, String xmlString) {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(xsdFile);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlString));
		} catch (IOException e) {
			logger.info("There is some issue while reading a file");
			return false;
		} catch (SAXException e) {
			logger.info("Unable to parse the json content : " + xmlString);
			return false;
		}
		return true;
	}

	public static boolean validateXMLSchema(File xsdFile, File xmlFile) {
		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(xsdFile);
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
		} catch (IOException e) {
			logger.info("There is some issue while reading a file");
			return false;
		} catch (SAXException e) {
			logger.info("Unable to parse the xml content : "
					+ xmlFile.getAbsolutePath());
			return false;
		}
		return true;
	}

}

