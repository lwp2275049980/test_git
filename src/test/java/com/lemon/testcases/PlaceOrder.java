package com.lemon.testcases;

import com.lemon.apidefinition.ApiCall;
import com.lemon.service.BusinessFlow;
import com.lemon.util.Environment;
import com.lemon.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlaceOrder {
    @Test
    public void place_order(){
        /*
        //登录请求
        String loginData = "{\"principal\":\"weipeng\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}";
        Response loginRes = ApiCall.login(loginData);
        String token = loginRes.jsonPath().get("access_token");
        //搜索商品
        String searchData = "prodName=宜家墨绿色衣柜&categoryId=&sort=0&orderBy=0&current=1&isAllProdType=true&st=0&size=12";
        Response searchRes = ApiCall.searchProduct(searchData);
        int prodId = searchRes.jsonPath().get("records[0].prodId");
        Environment.saveToEnvironment("prodId",prodId);
         */
        //商品信息         //调用业务逻辑层（简化代码）
        Response infoRes = BusinessFlow.test_login_search_info();
        int skuId = infoRes.jsonPath().get("skuList[0].skuId");
        Environment.saveToEnvironment("skuId",skuId);
        //确认订单请求
        String confirmData = "{\"addrId\":0,\"orderItem\":{\"prodId\":#prodId#,\"skuId\":#skuId#,\"prodCount\":1,\"shopId\":1}," +
                "\"couponIds\":[],\"isScorePay\":0,\"userChangeCoupon\":0," +
                "\"userUseScore\":0,\"uuid\":\"449cd6ae-e17d-4d94-9b26-a49a7dc8bfe3\"}";
        Response confirmRes = ApiCall.confirmOrder(confirmData,"#token#");
        //提交订单请求
        String submitData = "{\"orderShopParam\":[{\"remarks\":\"\",\"shopId\":1}]," +
                "\"uuid\":\"449cd6ae-e17d-4d94-9b26-a49a7dc8bfe3\"}";
        Response submitRes = ApiCall.submitOrder(submitData,"#token#");
        String orderNumbers = submitRes.jsonPath().get("orderNumbers");
        Environment.saveToEnvironment("orderNumbers",orderNumbers);

        //下单请求
        String placeData = "{\"payType\":3,\"orderNumbers\":\"#orderNumbers#\"}";
        Response placeRes = ApiCall.placeOrder(placeData,"#token#");

        //模拟支付
        String mockData = "{\"payNo\":#orderNumbers#,\"bizPayNo\":2342,\"isPaySuccess\":true}";
        Response mockPayRes = ApiCall.mockPay(mockData,"#token#");


        //断言-响应体
        String assertBody = mockPayRes.body().asString();
        Assert.assertEquals(assertBody,"success");   ////提取纯文本信息并作字符串化
        //断言-数据库
        String sql = "SELECT status FROM tz_order WHERE order_number = '#orderNumbers#';";
        int status = (int)JDBCUtils.querySingleData(sql);
        Assert.assertEquals(status,2);    //status字段表示订单状态：1-待付款 2-待发货
    }
}
