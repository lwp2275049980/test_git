package com.lemon.service;

import com.lemon.apidefinition.ApiCall;
import com.lemon.util.Environment;
import com.lemon.util.JDBCUtils;
import com.lemon.util.RandomData;
import io.restassured.response.Response;

import java.util.Random;

//业务逻辑层
public class BusinessFlow {
    /**
     * 业务组合场景  登录-》搜索商品-》商品信息
     * @return  商品信息接口的响应数据
     */
    public static Response test_login_search_info(){
        //1,登录
        String jsonData = "{\"principal\":\"weipeng\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}";
        String url = "http://mall.lemonban.com:8107/login";
        Response loginRes = ApiCall.login(jsonData);
        String token = loginRes.jsonPath().get("access_token");
            //手动保存到环境变量中
        Environment.saveToEnvironment("token",token);
        //2,搜索商品
        String inputParams = "prodName=宜家墨绿色衣柜&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response searchRes = ApiCall.searchProduct(inputParams);
          // 2-2，提取prodId
        int prodId = searchRes.jsonPath().get("records[0].prodId");
            //手动保存到环境变量中
        Environment.saveToEnvironment("prodId",prodId);
        //3，商品信息
        Response infoRes = ApiCall.productInfo(prodId);
        return infoRes;
    }

    public static String send_check_sms(){
        //发送验证码
        String randomPhone = RandomData.randomPhone();
        Environment.saveToEnvironment("randomPhone",randomPhone);
        String randomName = RandomData.randomName();
        Environment.saveToEnvironment("randomName",randomName);
        String sendData = "{\"mobile\":\"#randomPhone#\"}";
        Response sendRes = ApiCall.sendRegisterSms(sendData);

        //校验验证码
        String sql = "select mobile_code from tz_sms_log where id = (select MAX(id) from tz_sms_log);";
        String mobile_code = (String) JDBCUtils.querySingleData(sql);
        Environment.saveToEnvironment("mobile_code",mobile_code);
        String checkData = "{\"mobile\":\"#randomPhone#\",\"validCode\":\"#mobile_code#\"}";
        Response checkRes = ApiCall.checkRegisterSms(checkData);

        String smsFlag = checkRes.body().asString();
        Environment.saveToEnvironment("smsFlag",smsFlag);
        return smsFlag;
    }
}
