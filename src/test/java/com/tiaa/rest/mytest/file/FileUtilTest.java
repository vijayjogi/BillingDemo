package com.tiaa.rest.mytest.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.tiaa.rest.mytest.util.FileUtil;

public class FileUtilTest {

	File myTestXmlFile;
	File myTestXsdFile;
	
	@Before
	public void setup() {
		myTestXmlFile = new File("C://Mytest/BOM-1234-456.xml");
		myTestXsdFile= new File("C://Mytest/RestuarntDetail.xsd");
	}

	@Test
	public void convertXmlToJSONObject() throws IOException {
		assertNotNull(FileUtil.convertXmlToJSONObject(myTestXmlFile));
	}

	@Test(expected = NullPointerException.class)
	public void convertXmlToJSONObjectWithException() throws IOException {
		assertNotNull(FileUtil.convertXmlToJSONObject(null));
	}
	
	@Test(expected = NullPointerException.class)
	public void validateXMLSchemaWithException() throws IOException {
		assertNotNull(FileUtil.validateXMLSchema(null, new String()));
	}
	
	@Test
	public void validateXMLSchemaForFile() throws IOException {
		assertEquals(true, FileUtil.validateXMLSchema(myTestXsdFile, myTestXmlFile));
	}
	

	@Test
	public void validateXMLSchemaForString() throws IOException {
		assertEquals(false, FileUtil.validateXMLSchema(myTestXsdFile, "This is just for test"));
	}
}
