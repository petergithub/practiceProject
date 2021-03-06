package doing;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.Test;
import org.pu.test.base.TestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.Assert;

/**
 * [{ },{ }]是 json array 的序列化值 json 格式的字符串组成的逗号隔开的字符串
 * @author Shang Pu
 * @version Date：Jan 18, 2017 11:21:25 AM
 */
public class PracticeJson extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(PracticeJson.class);

	@Test
	public void testJsonArrayWithObject() {
		String result = "[\"000\",\"001\"]";
		log.info("result = {}", result);
		PaymentParams item1 = new PaymentParams("merchantId1", "orderId1", null);
		PaymentParams item2 = new PaymentParams("merchantId2", "orderId2", null);

		JSONArray jsonArrayFast = new JSONArray();
		jsonArrayFast.add(0, item1);
		jsonArrayFast.add(1, item2);
		String jsonArrayList = jsonArrayFast.toJSONString();
		
		//[{"merchantId":"merchantId1","orderId":"orderId1"},{"merchantId":"merchantId2","orderId":"orderId2"}]
		log.info("jsonArrayList: {}", jsonArrayList);
		
		List<PaymentParams> list = new ArrayList<>();
		list.add(item1);
		list.add(item2);
		//[{"merchantId":"merchantId1","orderId":"orderId1"},{"merchantId":"merchantId2","orderId":"orderId2"}]
		String javaListJsonString = JSON.toJSONString(list);
		log.info("List json string = {}", javaListJsonString);
		
		Assert.assertEquals(jsonArrayList, javaListJsonString);
	}
	
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

	/**
	 * com.alibaba.fastjson.serializer.SerializerFeature
	 * DisableCheckSpecialChar：一个对象的字符串属性中如果有特殊字符如双引号，
	 * 将会在转成json时带有反斜杠转移符。如果不需要转义 ，可以使用这个属性。默认为false
	 * QuoteFieldNames———-输出key时是否使用双引号,默认为true
	 * WriteMapNullValue——–是否输出值为null的字段,默认为false
	 * WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
	 * WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
	 * WriteNullStringAsEmpty—字符类型字段如果为null,输出为”“,而非null
	 * WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null
	 */
	@Test
	public void testFeatures() {
		SerializerFeature[] features = { SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.DisableCircularReferenceDetect };

		UserAlias user = new UserAlias();
		user.setId(123);
		user.setName("name");
		Assert.assertEquals("{\"id\":123,\"name\":\"name\"}", JSONObject.toJSONString(user));

		// Write null as empty
		user.setName(null);
		Assert.assertEquals("{\"id\":123}", JSONObject.toJSONString(user));

		// Write null as null
		user.setId(null);
		Assert.assertEquals("{\"id\":null,\"name\":null}",
				JSONObject.toJSONString(user, SerializerFeature.WriteMapNullValue));

		// WriteNullStringAsEmpty,WriteNullNumberAsZero
		features = new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullBooleanAsFalse };
		Assert.assertEquals("{\"id\":0,\"name\":\"\"}", JSONObject.toJSONString(user, features));
	}

	/**
	 * User类有两个字段，过滤了name字段，而且把id字段重命名为uid。
	 * http://www.oschina.net/code/snippet_12_3494
	 */
	public void testAliasRename() {
		UserAlias user = new UserAlias();
		user.setId(123);
		user.setName("name");

		SerializeConfig config = new SerializeConfig();
		config.put(UserAlias.class,
				new JavaBeanSerializer(UserAlias.class, Collections.singletonMap("id", "uid")));

		JSONSerializer serializer = new JSONSerializer(config);
		serializer.write(user);
		String jsonString = serializer.toString();

		Assert.assertEquals("{\"uid\":123}", jsonString);
	}

	/**
	 * 过滤了name字段，而且把id字段重命名为uid。
	 */
	class UserAlias {
		private Integer id;
		private String name;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public void testJsonDate() {
		PaymentParams pay = new PaymentParams("merchantId", "orderId", new DateTime().toDate());
		log.info("JSONpay = {}", JSON.toJSONString(pay));
	}

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
		JSONObject fastjson = paramToJson(new PaymentParams("merchantId", "orderId", null));
		log.info("fastjson = {}", fastjson);
		// {"orderId":"orderId","merchantId":"merchantId"}
		Assert.assertEquals("{\"orderId\":\"orderId\",\"merchantId\":\"merchantId\"}",
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
		public String orderId;
		@JSONField(format = "yyyyMMdd HH:mm:ss")
		public Date date;

		public PaymentParams(String merchantId, String orderId, Date date) {
			this.merchantId = merchantId;
			this.orderId = orderId;
			this.date = date;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
	}
}
