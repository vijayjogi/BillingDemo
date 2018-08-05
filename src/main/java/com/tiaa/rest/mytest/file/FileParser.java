package com.tiaa.rest.mytest.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xml.sax.SAXException;

import com.tiaa.rest.mytest.util.FileUtil;

public class FileParser implements Job {
	private final static Logger logger = Logger.getLogger(FileParser.class);
	private static final String XML_EXT = ".xml";
	private static final String JSON_EXT = ".json";
	private static final String XSD_FILE = "/RestuarntDetail.xsd";

	private String foderPath;
	private List<File> matchFiles = null;
	private List<File> misMatchFiles = null;

	public FileParser(String baseFolder) {
		this.foderPath = baseFolder;
		matchFiles = new ArrayList<File>();
		misMatchFiles = new ArrayList<File>();
	}

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		parseFile();
		try {
			writeMatchFile();
		} catch (JSONException | IOException | ParseException e1) {
			logger.info("Not able to create Match.json");
		}
		try {
			writeMissMatchFile();
		} catch (JSONException | IOException | ParseException e) {
			logger.info("Not able to create missMAtch.json");
		}
	}
	
	void parseFile() {
		File xsdFile = loadXsdFile();
		File file = null;
		boolean validFile = false;
		try {
			file = new File(foderPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.getName().endsWith(XML_EXT)) {
					validFile = isValidXmlFile(xsdFile, f);
				} else if (f.getName().endsWith(JSON_EXT)) {
					validFile = isValidJsonFile(xsdFile, f);
				}
				if (validFile) {
					matchFiles.add(f);
				} else {
					misMatchFiles.add(f);
				}
				validFile = false;
			}

		} finally {
			if (file != null) {
			}
		}

	}

	File loadXsdFile() {
		return new File(foderPath/* + XSD_FILE*/);
	}

	private boolean isValidXmlFile(File xsdFile, File xmlFile) {
		return FileUtil.validateXMLSchema(xsdFile, xmlFile);
	}

	private boolean isValidJsonFile(File xsdFile, File jsonFile) {
		String jsonStr = null;
		try {
			jsonStr = new String(Files.readAllBytes(Paths.get(jsonFile
					.getAbsolutePath())));
		} catch (IOException e) {
			logger.info("Unable to parse the json file : "
					+ jsonFile.getAbsolutePath());
			return false;
		}
		JSONObject json = new JSONObject(jsonStr);
		String xmlString = XML.toString(json);
		return FileUtil.validateXMLSchema(xsdFile, xmlString);
	}

	public void writeMatchFile() throws JSONException, IOException,
			ParseException {
		JSONObject target = new JSONObject();
		JSONParser parser = new JSONParser();
		for (File f : matchFiles) {
			if (f.getName().endsWith(XML_EXT)) {
				MergeFile.deepMerge(FileUtil.convertXmlToJSONObject(f), target);
			} else if (f.getName().endsWith(JSON_EXT)) {
				MergeFile.deepMerge(
						(JSONObject) parser.parse(new FileReader(f)), target);
			}
		}
		writeFile("Match.json", target);
	}

	public void writeMissMatchFile() throws JSONException, IOException,
			ParseException {
		JSONObject target = new JSONObject();
		JSONParser parser = new JSONParser();
		for (File f : matchFiles) {
			if (f.getName().endsWith(XML_EXT)) {
				MergeFile.deepMerge(FileUtil.convertXmlToJSONObject(f), target);
			} else if (f.getName().endsWith(JSON_EXT)) {
				MergeFile.deepMerge(
						(JSONObject) parser.parse(new FileReader(f)), target);
			}
		}
		writeFile("MissMatch.json", target);
	}

	private void writeFile(String fileName, JSONObject target) {
		try (FileWriter file = new FileWriter(foderPath + "/MissMatch.json")) {
			file.write(target.toString());
			file.flush();
		} catch (IOException e) {
			logger.info("Not able to create " + fileName);
		}
	}

	public List<File> getMatchFiles() {
		return matchFiles;
	}

	public List<File> getMisMatchFiles() {
		return misMatchFiles;
	}
	
	

}