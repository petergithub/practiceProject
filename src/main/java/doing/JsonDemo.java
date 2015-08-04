package doing;

import java.lang.reflect.Field;
import java.util.Set;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonDemo extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(JsonDemo.class);

	/**
	 * ["000","001"]
	 */
	public void testJsonArray() {
		String result = "[\"000\",\"001\"]";
		log.info("result = {}", result);
		String item1 = "000";
		String item2 = "001";

		JSONArray jsonArrayFast = new JSONArray();
		jsonArrayFast.add(0, item1);
		jsonArrayFast.add(1, item2);
		String orderStr = jsonArrayFast.toJSONString();
		Assert.assertEquals(result, orderStr);

		jsonArrayFast = JSONArray.parseArray(result);
		Object[] array = jsonArrayFast.toArray();
		for (Object o : array) {
			Assert.assertTrue(item1.equals(o) || item2.equals(o));
		}

		org.json.JSONArray jsonArray = new org.json.JSONArray();
		jsonArray.put(item1);
		jsonArray.put(item2);
		orderStr = jsonArray.toString();
		Assert.assertEquals(result, orderStr);
		Assert.assertEquals(2, jsonArray.length());

		try {
			jsonArray = new org.json.JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {
				Assert.assertTrue(item1.equals(jsonArray.getString(i))
						|| item2.equals(jsonArray.getString(i)));
			}
		} catch (JSONException e) {
			log.error("JSONException in TestClass.testJsonArray()", e);
		}
	}

	@Test
	public void testJsonPro() {
		// {"response":0,"itemArray":[{"price":1.5,"fashion":{},
		// "product":{"name":"IDOL 2 MINI FlipCase CLOUDY","productId":1}}]}

		// build json
		JSONObject fastjson = new JSONObject();
		int response = 0;
		fastjson.put("response", response);

		JSONArray itemArray = new JSONArray();

		JSONObject fastjsonItem = new JSONObject();
		double price = 1.5;
		fastjsonItem.put("price", price);
		JSONObject fashinon = new JSONObject();
		fastjsonItem.put("fashinon", fashinon);

		JSONObject fastjsonProduct = new JSONObject();
		fastjsonProduct.put("productId", 2);
		String name = "IDOL 2 MINI FlipCase CLOUDY";
		fastjsonProduct.put("name", name);
		fastjsonItem.put("product", fastjsonProduct);
		itemArray.add(fastjsonItem);

		fastjson.put("itemArray", itemArray);
		String jsonStr = fastjson.toString();
		log.info("json = {}", fastjson);

		// Resolve json string with fastjson
		fastjson = JSONObject.parseObject(jsonStr);
		Set<String> keysFastjson = fastjson.keySet();
		log.info("get all keys = {}", keysFastjson);

		Assert.assertEquals(response, fastjson.getIntValue("response"));
		JSONArray fastjsonArray = fastjson.getJSONArray("itemArray");
		for (Object o : fastjsonArray.toArray()) {
			fastjsonItem = (JSONObject) o;
			log.info("item = {}", fastjsonItem);
			Assert.assertEquals(price, fastjsonItem.getDoubleValue("price"));
			fastjsonProduct = fastjsonItem.getJSONObject("product");
			Assert.assertEquals(name, fastjsonProduct.getString("name"));
		}

		// Resolve json string with org.json.JSONObject
		try {
			org.json.JSONObject json = new org.json.JSONObject(jsonStr);
			String[] keys = org.json.JSONObject.getNames(json);
			printArray(keys);

			Assert.assertEquals(response, json.getInt("response"));
			org.json.JSONArray jsonArray = json.getJSONArray("itemArray");
			for (int i = 0; i < jsonArray.length(); i++) {
				org.json.JSONObject item = jsonArray.getJSONObject(i);
				log.info("item = {}", item);
				Assert.assertEquals(price, item.getDouble("price"));
				org.json.JSONObject product = item.getJSONObject("product");
				Assert.assertEquals(name, product.getString("name"));
			}
		} catch (JSONException e) {
			log.error("JSONException in TestClass.testJsonPro()", e);
		}
	}

	public void testJsonDuplicate() {
		JSONObject fastjson = new JSONObject();
		fastjson.put("response", 0);
		fastjson.put("response", 1);
		// {"response":1}
		Assert.assertEquals("{\"response\":1}", fastjson.toJSONString());
	}

	public void testToJson() {
		JSONObject fastjson = paramToJson(new PaymentParams("merchantId", "mOutOrderId"));
		log.info("fastjson = {}", fastjson);
		// {"mOutOrderId":"mOutOrderId","merchantId":"merchantId"}
		Assert.assertEquals("{\"mOutOrderId\":\"mOutOrderId\",\"merchantId\":\"merchantId\"}",
				fastjson.toJSONString());
	}

	private JSONObject paramToJson(Object params) {
		Field[] fields = params.getClass().getDeclaredFields();
		JSONObject jObject = new JSONObject();
		for (Field f : fields) {
			try {
				String key = f.getName();
				// skip the this pointer
				if ("this$0".equals(key))
					continue;

				String value = (String) f.get(params);

				log.info("in paramToJSON(key[{}],value[{}])", key, value);
				if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
					key = key.trim();
					value = value.trim();
					jObject.put(key, value);
				}
			} catch (Exception e) {
				log.error("Exception in ParameterUtil.signAndAssembleParams()", e);
			}

		}
		return jObject;
	}

	class PaymentParams {
		public String merchantId;
		public String mOutOrderId;

		public PaymentParams(String merchantId, String mOutOrderId) {
			this.merchantId = merchantId;
			this.mOutOrderId = mOutOrderId;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getmOutOrderId() {
			return mOutOrderId;
		}

		public void setmOutOrderId(String mOutOrderId) {
			this.mOutOrderId = mOutOrderId;
		}
	}
}
