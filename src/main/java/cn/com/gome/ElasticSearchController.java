package cn.com.gome;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.gome.elasticSearch.ElasticSeatchClient;
import cn.com.gome.entity.Message;

@SpringBootApplication
@RestController
public class ElasticSearchController {

	@Value("${es.url}")
	String url;

	@Value("${es.index_name}")
	String index_name;

	@Autowired
	ElasticSeatchClient elasticSeatchClient;
	private Logger logger = Logger.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchController.class, args);
	}

	/**
	 * url 方式请求es的查询功能
	 * 
	 * @author likaile
	 * @desc
	 */
	@RequestMapping("/searchByUrl")
	public String searchByUrl(@RequestParam(value = "index", required = false) String index,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "fieldName", required = false) String fieldName,
			@RequestParam(value = "keyWord", required = false) String keyWord) {
		if (index == null) {
			index = index_name;
		}
		if (type != null) {
			type = type + "/";
		} else {
			type = "";
		}
		String postUrl = url + "/" + index + "/" + type + "_search?pretty";
		return elasticSeatchClient.getResultFromEs(postUrl, fieldName, keyWord);
	}

	@RequestMapping("/getData")
	public Object getDate(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "docId", required = true) String docId) {
		Map<String, Object> map = elasticSeatchClient.getResponseDate(type, docId);
		return map == null ? " 没有对应的数据结果    " : map;
	}

	@RequestMapping("/addDoc")
	public boolean addDoc(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "docId", required = true) String docId,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "desc", required = true) String desc) {
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("name", name);
		mapParam.put("desc", desc);
		return elasticSeatchClient.addDoc(type, docId, mapParam);
	}

	@RequestMapping("/deleteDoc")
	public boolean deleteDoc(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "docId", required = true) String docId) {
		return elasticSeatchClient.deleteDoc(type, docId);
	}

	@RequestMapping("/addEntity")
	public boolean addEntity(Message message) {
		return elasticSeatchClient.addDoc("message", null, message, "getTitle", "getContent", "getAuthor");
	}

	/** 下面开始搜索的方法 **/
	@RequestMapping("/searche")
	public Map<String, Object> searche(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "fieldName", required = true) String fieldName,
			@RequestParam(value = "keyWord", required = true) String keyWord) {
		return elasticSeatchClient.searchDoc(type, fieldName, keyWord);
	}

	@RequestMapping("/updateDoc")
	public boolean updateDoc(@RequestParam(value = "docId", required = true) String docId,
			@RequestParam(value = "fieldName", required = true) String fieldName,
			@RequestParam(value = "fieldValue", required = true) String fieldValue) {
		Map<String, String> updateParam = new HashMap<String, String>();
		updateParam.put(fieldName, fieldValue);
		return elasticSeatchClient.updateDoc("message", docId, updateParam);
	}

	@RequestMapping("/searcheHigh")
	public Map<String, Object> searcheHigh(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "fieldName", required = true) String fieldName,
			@RequestParam(value = "keyWord", required = true) String keyWord) {
		return elasticSeatchClient.searchDocHighlight(type, fieldName, keyWord);
	}

	@RequestMapping("/searcheOr")
	public Map<String, Object> multiOrSearchDoc(@RequestParam(value = "type", required = true) String type) {
		Map<String, String> shouldMap = new HashMap<String, String>();
		shouldMap.put("name", "de");
		return elasticSeatchClient.multiOrSearchDoc(type, shouldMap);
	}

	@RequestMapping("/searcheAnd")
	public Map<String, Object> multiAndSearchDoc(@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "contain", required = true) boolean contain) {
		Map<String, String> shouldMap = new HashMap<String, String>();
		shouldMap.put("name", "de");
		return elasticSeatchClient.multiAndSearchDoc(type, shouldMap, contain);
	}
}
