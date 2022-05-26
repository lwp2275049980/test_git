package com.lemon.common;

import com.alibaba.fastjson.JSONObject;
import com.lemon.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

public class BaseTest {
    /**
     * 通用的断言方法。各测试类继承BaseTest类，就可以调用断言方法
     * @param assertResponse  响应断言--原始字符串
     * @param res 接口响应数据
     */
    public static void assertResponse(String assertResponse,Response res){
        //json如何转化为Map？？  【引入fastjson依赖】
        //{"statusCode":200,"nickName":"weipeng"}
        if(assertResponse != null){
            Map<String,Object> map = JSONObject.parseObject(assertResponse);
            //遍历Map
            Set<Map.Entry<String,Object>> datas = map.entrySet();  //获取所有的键值对并保存
            for (Map.Entry<String,Object> keyValue:datas){
                String name = keyValue.getKey();
                Object value = keyValue.getValue();
                if("statusCode".equals(name)){
                    Assert.assertEquals(res.getStatusCode(),value);
                }else{
                    Assert.assertEquals(res.jsonPath().get(name),value);
                }
            }
        }
    }

    /**
     * 通用的数据库断言
     * @param assertDB  数据库断言--原始字符串
     */
    public static void assertDBResponse(String assertDB){
        if (assertDB != null) {
            Map<String, Object> map = JSONObject.parseObject(assertDB);
            Set<Entry<String, Object>> datas = map.entrySet();
            for (Entry<String, Object> keyValue : datas) {
                String key = keyValue.getKey();
                Object value = keyValue.getValue();
                //实际值与期望值做字符化出路，方便比较
                Assert.assertEquals(JDBCUtils.querySingleData(key).toString(), value.toString());
            }
        }
    }
}
