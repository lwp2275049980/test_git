package com.lemon.apidefinition;

import com.lemon.common.GlobalConfig;
import com.lemon.util.Environment;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
//接口定义层
public class ApiCall {
    public static Response request(String interfaceName,Map headersMap,String inputParams,String mothod,String url){
        String logFilePath = null;
        Response res = null;
        if (!GlobalConfig.IS_DEBUG) {
            //加日志
            //第一种方式.日志全部保存在一个文件中--不方便管理，查看
        /*PrintStream fileOutPutStream = null;
        try {
            fileOutPutStream = new PrintStream(new File("log/test_all"+System.currentTimeMillis()+".log"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new RequestLoggingFilter(fileOutPutStream));
         */
            //第二种方式
            logFilePath = "log/test_" + interfaceName + "_" + System.currentTimeMillis() + ".log";
            PrintStream fileOutStream = null;
            try {
                fileOutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutStream));
        }

            inputParams = Environment.replaceParams(inputParams);
            headersMap = Environment.replaceParams(headersMap);
            RestAssured.baseURI = GlobalConfig.base_uri;
            if ("get".equalsIgnoreCase(mothod)) {
                res = given().log().all().headers(headersMap).when().get(url + inputParams).then().log().all().extract().response();
            } else if ("post".equalsIgnoreCase(mothod)) {
                res = given().log().all().headers(headersMap).body(inputParams).when().post(url).then().log().all().extract().response();
            } else if ("put".equalsIgnoreCase(mothod)) {
                res = given().log().all().headers(headersMap).body(inputParams).when().put(url).then().log().all().extract().response();
            } else if ("delete".equalsIgnoreCase(mothod)) {
                //TODO
            } else {
                System.out.println("请求方法出错，请检查方法名是否正确");
            }
        if (!GlobalConfig.IS_DEBUG) {
            try {
                Allure.attachment(interfaceName + "_信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }



    /**
     * 登录的接口定义
     * @param inputParams  登录接口的请求参数
     * {"principal":"waiwai","credentials":"lemon123456","appType":3,"loginType":0}
     * @return  登录接口的响应数据
     */
    public static Response login(String inputParams){
        Map headersMap = new HashMap();
        headersMap.put("Content-Type","application/json");
        String url = "/login";
        return request("登录请求",headersMap,inputParams,"post",url);
    }

    /**
     * 商品搜索的接口定义
     * @param inputParams  商品搜素的查询参数
     * prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12
     * @return  接口的响应数据
     */
    public static Response searchProduct(String inputParams){
        Map headersMap = new HashMap();
        headersMap.put("Content-Type","application/json");
        String url = "/search/searchProdPage?";
        return request("搜索商品",headersMap,inputParams,"get",url);
    }

    /**
     * 商品信息请求的接口定义
     * @param inputParams   接口入参
     * prodId=XXX
     * @return  响应数据
     */
    public static Response productInfo(int inputParams){
        Map headersMap = new HashMap();
        headersMap.put("Content-Type","application/json");
        String url = "/prod/prodInfo?"+"prodId=";
        return request("商品信息",headersMap,inputParams+"","get",url);
    }

    /**
     * 添加购物车的接口定义
     * @param inputParams   接口入参
     *      {"basketId":0,"count":1,"prodId":"86","shopId":1,"skuId":418}
     * @param token  鉴权token信息
     * @return 接口响应数据
     */
    public static Response addShopcart(String inputParams,String token){
        token = Environment.replaceParams(token);
        Map headersMap = new HashMap();
        headersMap.put("Content-Type","application/json");
        headersMap.put("Authorization","bearer"+token);
        String url = "/p/shopCart/changeItem";
        return request("添加购物车",headersMap,inputParams,"post",url);
    }

    /**
     * 发送验证码接口请求
     * @param inputParams  接口入参
     * {"mobile":"17366228888"}
     * @return  接口响应数据信息
     */
    public static Response sendRegisterSms(String inputParams){
        Map headersMap = new HashMap();
        headersMap.put("Content-Type","application/json");
        String url = "/user/sendRegisterSms";
        return request("发送验证码",headersMap,inputParams,"put",url);
    }

    /**
     * 校验验证码接口请求
     * @param inputParams  接口入参
     * {"mobile":"17366228888","validCode":"575961"}
     * @return  接口响应数据信息
     */
    public static Response checkRegisterSms(String inputParams){
        Map headrsaMap = new HashMap();
        headrsaMap.put("Content-Type","application/json");
        String url = "/user/checkRegisterSms";
        return request("校验验证码",headrsaMap,inputParams,"put",url);
    }

    /**
     * 注册接口请求
     * @param inpurParams  接口入参
     * {"appType":3,"checkRegisterSmsFlag":"867924913161480d811a3a2ec168bc5c",
     *                     "mobile":"17366228888","userName":"lemontest01","password":"123456",
     *                     "registerOrBind":1,"validateType":1}
     * @return
     */
    public static Response register(String inpurParams){
        Map headersMap = new HashMap();
        headersMap.put("Content-Type","application/json");
        String url = "/user/registerOrBindUser";
        return request("注册接口",headersMap,inpurParams,"put",url);
    }

    /**
     * 确认订单的接口定义
     * @param inputParams  接口入参
     * {"addrId":0,"orderItem":{"prodId":4250,"skuId":4701,"prodCount":1,"shopId":1},
     *                     "couponIds":[],"isScorePay":0,"userChangeCoupon":0,"userUseScore":0,
     *                     "uuid":"449cd6ae-e17d-4d94-9b26-a49a7dc8bfe3"}
     * @param token   token值
     * @return   响应信息
     */
    public static Response confirmOrder(String inputParams,String token){
        Map<String,Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type","application/json");
        headersMap.put("Authorization","bearer"+token);
        String url = "/p/order/confirm";
        return request("确认订单",headersMap,inputParams,"post",url);

    }

    /**
     * 提交订单的接口定义
     * @param inputParams   接口入参
     * {"orderShopParam":[{"remarks":"","shopId":1}],"uuid":"449cd6ae-e17d-4d94-9b26-a49a7dc8bfe3"}
     * @param token  token值
     * @return  响应信息
     */
    public static Response submitOrder(String inputParams,String token){
        Map<String,Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type","application/json");
        headersMap.put("Authorization","bearer"+token);
        String url = "/p/order/submit";
        return request("提交订单",headersMap,inputParams,"post",url);
    }

    /**
     * 下单的接口定义
     * @param inputParams  接口入参
     * {"payType":3,"orderNumbers":"1524729016496689152"}
     * @param token   token值
     * @return  响应信息
     */
    public static Response placeOrder(String inputParams,String token){
        Map<String,Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type","application/json");
        headersMap.put("Authorization","bearer"+token);
        String url = "/p/order/pay";
        return request("下单",headersMap,inputParams,"post",url);
    }

    /**
     * 模拟支付的接口定义 （实际项目中可以找开发写一个模拟回调接口）
     * @param inputParams  接口入参
     * {"payNo":#orderNumbers#,"bizPayNo":2342,"isPaySuccess":true}
     * @param token  token值
     * @return  响应信息
     */
    public static Response mockPay(String inputParams,String token){
        Map<String,Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type","application/json");
        headersMap.put("Authorization","bearer"+token);
        String url = "/notice/pay/3";
        return request("模拟支付",headersMap,inputParams,"post",url);
    }

    /**
     * erp项目的登录接口定义
     * @param inputParams  接口入参
     * @return  响应信息
     */
    public static Response erpLogin(String inputParams){
        Map<String,Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type","application/x-www-form-urlencoded");
        String url = "http://erp.lemfix.com/user/login";
        return request("erp登录",headersMap,inputParams,"post",url);
    }
}
