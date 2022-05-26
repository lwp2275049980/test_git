package com.lemon.testcases;

import com.lemon.apidefinition.ApiCall;
import com.lemon.service.BusinessFlow;
import com.lemon.util.Environment;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//用例层
public class ShopcartTest {
    @Test
    public void test_add_shopcart_success(){
        //1,登录
//        String jsonData = "{\"principal\":\"weipeng\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}";
//        String url = "http://mall.lemonban.com:8107/login";
//        Response loginRes = ApiCall.login(jsonData,url);
//        String access_token = loginRes.jsonPath().get("access_token");
//        String token = "bearer"+access_token;
//        //2,商品搜索
//        String inputParams = "prodName=&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
//        Response searchRes = ApiCall.searchProduct(inputParams);
//        int prodId = searchRes.jsonPath().get("records[0].prodId");
        //3,商品信息
//      Response infoRes = ApiCall.productInfo(prodId);
        Response infoRes = BusinessFlow.test_login_search_info();
        int skuId = infoRes.jsonPath().get("skuList[0].skuId");
        Environment.saveToEnvironment("skuId",skuId);
        //4,添加购物车
        String addData = "{\"basketId\":0,\"count\":1,\"prodId\":\"#prodId#\",\"shopId\":1,\"skuId\":#skuId#}";
        Response cartRes = ApiCall.addShopcart(addData,"#token#");
    }


    @Test
    public void test_canshuhua(){
        String addData = "{\"basketId\":0,\"count\":1,\"prodId\":\"#prodId#\",\"shopId\":1,\"skuId\":#skuId#}";
        String prodId = "101";
        int skuId = 203;
        Environment.saveToEnvironment("prodId",prodId);
        Environment.saveToEnvironment("skuId",skuId);
        System.out.println(Environment.replaceParams(addData));
        /*
        String regex = "#(.+?)#";    //括号表示分组
        Pattern pattern = Pattern.compile(regex);      //【有了模式】
        Matcher matcher = pattern.matcher(addData);    //【有了查找器】
        while(matcher.find()){
            String wholeStr = matcher.group(0);   //group(0) 表示找到整体，即 #prodId#  #skuId#
            String subStr = matcher.group(1);     //group(1) 表示找到整体里面的内容，即prodId，skuId
            addData = addData.replace(wholeStr,Environment.getEnvironment(subStr)+"");
        }
        System.out.println(addData);
         */
    }








}
