package com.lemon.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment {
    private static Map<String,Object> envMap = new HashMap<String,Object>();

    public static void saveToEnvironment(String varName,Object varValue){
        Environment.envMap.put(varName,varValue);
    }

    public static Object getEnvironment(String varName){
        return Environment.envMap.get(varName);
    }

    //字符串类型数据的参数化替换
    public static String replaceParams(String jsonData){
        String regex = "#(.+?)#";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonData);
        while (matcher.find()){
            String wholeStr = matcher.group(0);
            String subStr = matcher.group(1);
            jsonData = jsonData.replace(wholeStr,getEnvironment(subStr)+"");
        }
        return jsonData;
    }

    //方法重载
    //Map类型转json 完成参数替换后，再转为Map
    public static Map replaceParams(Map headersMap) {
        String jsonData = JSONObject.toJSONString(headersMap);
        String regex = "#(.+?)#";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(jsonData);
        while (matcher.find()) {
            String whole = matcher.group(0);
            String subStr = matcher.group(1);
            jsonData = jsonData.replace(whole, getEnvironment(subStr) + "");
        }
        Map map = JSONObject.parseObject(jsonData);
        return map;
    }
}

