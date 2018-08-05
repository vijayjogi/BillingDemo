package com.tiaa.rest.mytest.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Collections;

import org.json.JSONObject;
import org.junit.Test;

public class FileParserTest {
	
	@Test
	public void loadXsdFile() {
		FileParser parser = new FileParser("C://Mytest");
		File file = parser.loadXsdFile();
		assertNotNull(file);
	}
	
	@Test(expected = NullPointerException.class)
	public void loadXsdFileWithException() {
		FileParser parser = new FileParser(null);
		File file = parser.loadXsdFile();
		assertNotNull(file);
	}
	
	
	@Test
	public void missMatchEmptyTest() {
		FileParser parser = new FileParser("C://Mytest");
		assertEquals(parser.getMisMatchFiles().size(), Collections.EMPTY_LIST);
	}
	
	@Test
	public void matchEmptyTest() {
		FileParser parser = new FileParser("C://Mytest");
		assertEquals(parser.getMatchFiles(), Collections.EMPTY_LIST);
	}
	
	@Test(expected = NullPointerException.class)
	public void parseFileTest() {
		FileParser parser = new FileParser(null);
		parser.parseFile();
	}
	
	public void writeMissMatchFileTest(){
		FileParser parser = new FileParser("C://Mytest");
		JSONObject targetJson = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("test1", "value1");
		JSONObject jsonObj = new JSONObject();

		jsonObj.put("id", 0);
		jsonObj.put("name", "testName");
		json.put("test2", jsonObj);
		MergeFile.deepMerge(json, targetJson);
		assertEquals(json.toString(), targetJson.toString());
	}

}
