package com.washnow.admin.parser;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.washnow.admin.vo.OrderPojo;
import com.washnow.admin.vo.OrderVo;
import com.washnow.admin.vo.ProductPojo;
import com.washnow.admin.vo.ProductVo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Parser {


	public static  ArrayList<OrderVo> getOrders(String response) throws Exception {


		ArrayList<OrderVo> cList = new ArrayList<OrderVo>();
		try{
			JSONObject jsonObject = new JSONObject(response);
			JsonNode jsonNode = convertJsonFormat(jsonObject);
			ObjectMapper mapper = new ObjectMapper();
			OrderPojo ad = mapper.readValue(new TreeTraversingParser(jsonNode), OrderPojo.class);
			cList.addAll(ad.info);
		}catch(Exception e) {

			Log.e("parse order", e.toString());
		}






		return cList;


	}

	public static  ArrayList<ProductVo> getProducts(String response) throws Exception {


		ArrayList<ProductVo> cList = new ArrayList<ProductVo>();
		try{
			JSONObject jsonObject = new JSONObject(response);
			JsonNode jsonNode = convertJsonFormat(jsonObject);
			ObjectMapper mapper = new ObjectMapper();
			ProductPojo ad = mapper.readValue(new TreeTraversingParser(jsonNode), ProductPojo.class);
			cList.addAll(ad.info);
		}catch(Exception e) {

			Log.e("parse order", e.toString());
		}






		return cList;


	}

	static JsonNode convertJsonFormat(JSONObject json) {
		    ObjectNode ret = JsonNodeFactory.instance.objectNode();

		    @SuppressWarnings("unchecked")
		    Iterator<String> iterator = json.keys();
		    for (; iterator.hasNext();) {
		        String key = iterator.next();
		        Object value;
		        try {
		            value = json.get(key);
		        } catch (JSONException e) {
		            throw new RuntimeException(e);
		        }
		        if (json.isNull(key))
		            ret.putNull(key);
		        else if (value instanceof String)
		            ret.put(key, (String) value);
		        else if (value instanceof Integer)
		            ret.put(key, (Integer) value);
		        else if (value instanceof Long)
		            ret.put(key, (Long) value);
		        else if (value instanceof Double)
		            ret.put(key, (Double) value);
		        else if (value instanceof Boolean)
		            ret.put(key, (Boolean) value);
		        else if (value instanceof JSONObject)
		            ret.put(key, convertJsonFormat((JSONObject) value));
		        else if (value instanceof JSONArray)
		            ret.put(key, convertJsonFormat((JSONArray) value));
		        else
		            throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
		    }
		    return ret;
		}
		
		
		static JsonNode convertJsonFormat(JSONArray json) {
		    ArrayNode ret = JsonNodeFactory.instance.arrayNode();
		    for (int i = 0; i < json.length(); i++) {
		        Object value;
		        try {
		            value = json.get(i);
		        } catch (JSONException e) {
		            throw new RuntimeException(e);
		        }
		        if (json.isNull(i))
		            ret.addNull();
		        else if (value instanceof String)
		            ret.add((String) value);
		        else if (value instanceof Integer)
		            ret.add((Integer) value);
		        else if (value instanceof Long)
		            ret.add((Long) value);
		        else if (value instanceof Double)
		            ret.add((Double) value);
		        else if (value instanceof Boolean)
		            ret.add((Boolean) value);
		        else if (value instanceof JSONObject)
		            ret.add(convertJsonFormat((JSONObject) value));
		        else if (value instanceof JSONArray)
		            ret.add(convertJsonFormat((JSONArray) value));
		        else
		            throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
		    }
		    return ret;
		}
		public static String convertStreamToString(java.io.InputStream is) {
		    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		    return s.hasNext() ? s.next() : "";
		}
}
