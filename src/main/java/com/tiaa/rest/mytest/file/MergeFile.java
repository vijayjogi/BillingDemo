package com.tiaa.rest.mytest.file;

import org.json.JSONException;
import org.json.JSONObject;

public class MergeFile {
	public static JSONObject deepMerge(JSONObject source, JSONObject target)
			throws JSONException {
		for (String key : JSONObject.getNames(source)) {
			Object value = source.get(key);
			if (!target.has(key)) {
				target.put(key, value);
			} else {
				if (value instanceof JSONObject) {
					JSONObject valueJson = (JSONObject) value;
					deepMerge(valueJson, target.getJSONObject(key));
				} else {
					target.put(key, value);
				}
			}
		}
		return target;
	}
}
