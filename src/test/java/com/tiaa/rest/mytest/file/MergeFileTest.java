package com.tiaa.rest.mytest.file;
import static org.junit.Assert.assertEquals;
import org.json.JSONObject;
import org.junit.Test;

public class MergeFileTest {

	@Test
	public void deepMerge() {
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
