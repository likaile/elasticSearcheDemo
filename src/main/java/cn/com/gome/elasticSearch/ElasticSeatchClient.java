package cn.com.gome.elasticSearch;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 
 * @author likaile 
 * @desc Es的请求发送类
 */
@Service
public class ElasticSeatchClient {
	 @Autowired
	 ElasticSearchUtils elasticSearchUtils;
	 
		private Logger logger = Logger.getLogger(getClass());

	 /**
	  * 基本型查询API 通过指定的index type id 来获取返回的 信息
	  * @author likaile 
	  * @desc
	  */
	 public  Map<String,Object> getResponseDate(String type,String id){
		 return elasticSearchUtils.getResponseDate(type, id);
	 }
	  /**
     * 将一个Map格式的数据（key,value）插入索引（指定_id，一般是业务数据的id，及elasticSearch和关系型数据使用同一个id，方便同关系型数据库互动）
     * 
     * @param type 类型（对应数据库表）
     * @param docId id，对应elasticSearch中的_id字段
     * @param mapParam Map格式的数据
     * @return
     */
    public  boolean addDoc(String type, String docId, Map<String, String> mapParam) {
        return elasticSearchUtils.addMapDocToIndex(type, docId, mapParam);
    }

    /**
     * 将一个Map格式的数据（key,value）插入索引 （使用默认_id）
     * 
     * @param type 类型（对应数据库表）
     * @param mapParam Map格式的数据
     * @return
     */
    public  boolean addDoc(String type, Map<String, String> mapParam) {
        return elasticSearchUtils.addMapDocToIndex(type, null, mapParam);
    }

    /**
     * 将一个实体存入到默认索引的类型中（默认_id）
     * 
     * @param type 类型（对应数据库表）
     * @param entity 要插入的实体
     * @param methodNameParm 需要将实体中哪些属性作为字段
     * @return
     */
    public  boolean addDoc(String type, Object entity, String... methodNameParm) {
        return elasticSearchUtils.addEntityDoc(type, null, entity, methodNameParm);
    }

    /**
     * 将一个实体存入到默认索引的类型中（指定_id，一般是业务数据的id，及elasticSearch和关系型数据使用同一个id，方便同关系型数据库互动）
     * 
     * @param type 类型（对应数据库表）
     * @param docId id，对应elasticSearch中的_id字段
     * @param entity 要插入的实体
     * @param methodNameParm 需要将实体中哪些属性作为字段
     * @return
     */
    public  boolean addDoc(String type, String docId, Object entity, String... methodNameParm) {
        return elasticSearchUtils.addEntityDoc(type, docId, entity, methodNameParm);
    }

    /**
     * 删除文档
     * 
     * @param type 类型（对应数据库表）
     * @param docId 类型中id
     * @return
     */
    public  boolean deleteDoc(String type, String docId) {
        return elasticSearchUtils.deleteDoc(type, docId);
    }

    /**
     * 修改文档
     * 
     * @param type 类型
     * @param docId 文档id
     * @param updateParam 需要修改的字段和值
     * @return
     */
    public  boolean updateDoc(String type, String docId, Map<String, String> updateParam) {
        return elasticSearchUtils.updateDoc(type, docId, updateParam);
    }

    // --------------------以下是各种搜索方法--------------------------

    /**
     * 高亮搜索
     * 
     * @param type 类型
     * @param fieldName 段
     * @param keyword 段值
     * @return
     */
    public  Map<String, Object> searchDocHighlight(String type, String fieldName, String keyword) {
        return elasticSearchUtils.searchDocHighlight(type, fieldName, keyword, 0, 10);
    }

    /**
     * 高亮搜索
     * 
     * @param type 类型
     * @param fieldName 段
     * @param keyword 关键词
     * @param from 开始行数
     * @param size 每页大小
     * @return
     */
    public  Map<String, Object> searchDocHighlight(String type, String fieldName, String keyword, int from,
            int size) {
        return elasticSearchUtils.searchDocHighlight(type, fieldName, keyword, from, size);
    }

    /**
     * or条件查询高亮
     * 
     * @param type 类型
     * @param shouldMap or条件和值
     * @return
     */
    public  Map<String, Object> multiOrSearchDocHigh(String type, Map<String, String> shouldMap, int from,
            int size) {
        return elasticSearchUtils.multiOrSearchDocHigh(type, shouldMap, from, size);
    }

    /**
     * 搜索
     * 
     * @param type 类型
     * @param fieldName 待搜索的字段
     * @param keyword 待搜索的关键词
     */
    public  Map<String, Object> searchDoc(String type, String fieldName, String keyword) {
        return elasticSearchUtils.searchDoc(type, fieldName, keyword, 0, 10);
    }

    /**
     * 多个条件进行or查询
     * 
     * @param type 类型
     * @param shouldMap 进行or查询的段和值
     * @return
     */
    public  Map<String, Object> multiOrSearchDoc(String type, Map<String, String> shouldMap) {
        return elasticSearchUtils.multiOrSearchDoc(type, shouldMap, 0, 10);
    }

    /**
     * 多个条件进行and查询
     * 
     * @param type 类型
     * @param mustMap 进行and查询的段和值
     * @return
     */
    public  Map<String, Object> multiAndSearchDoc(String type, Map<String, String> mustMap,boolean contain) {
        return elasticSearchUtils.multiAndSearchDoc(type, mustMap,contain, 0, 10);
    }
    /**
     * 通过url发送post请求
     * @author likaile 
     * @desc 搜索关键字段 是否高亮
     */
    public String getResultFromEs(String url,String fieldName,String keyWord) {
		// 拼凑请求体
		StringBuilder searchStr = new StringBuilder();
		searchStr.append("{\r\n");
		searchStr.append("  \"query\": {\r\n");
		searchStr.append("    \"match\": { \"");
		searchStr.append(fieldName+"\": \"");
		searchStr.append(keyWord+"\"}}");
		searchStr.append("        }\r\n");
		searchStr.append("    }\r\n");
		searchStr.append("  }\r\n");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("", searchStr.toString());
		if(fieldName == null) {
			paramMap = null;
		}
		String resultStr = HttpUtils.sendHttpPost(url, paramMap, null);
		return resultStr;
	}
    
    public String getDateByUrl(String url) {
		return HttpUtils.sendHttpGet(url);
	}
    /**
     * url post请求访问 and集合查询
     * @author likaile 
     * @desc
     */
	public String multiAndSearchDocByUrl(String url, Map<String, String> shouldMap) {
		if(shouldMap == null|| shouldMap.size()<1) {
			return null;
		}
		//拼凑请求体
		StringBuilder bodyStr = new StringBuilder();
		bodyStr.append("{\r\n");
		bodyStr.append("  \"query\": {\r\n");
		bodyStr.append("    \"bool\": {\r\n");
		bodyStr.append("        \"must\": [\r\n");
		StringBuilder searchStr = new StringBuilder();
		for(String key:shouldMap.keySet()) {
			searchStr.append("           {\"term\":{\""+key+"\": \""+shouldMap.get(key)+"\"}},\r\n");
		}
		searchStr.deleteCharAt(searchStr.length()-3);
		bodyStr.append(searchStr);
		bodyStr.append("        ]\r\n");
		bodyStr.append("    }\r\n");
		bodyStr.append("  }}\r\n");
		Map<String, String> paramMap = new HashMap<String, String>();
		String paramString = bodyStr.toString();
		paramMap.put("", paramString);
		String resultStr = HttpUtils.sendHttpPost(url, paramMap, null);
		logger.info("url send postString :"+paramString +" result:"+resultStr);
		return resultStr;
	}
}
