package com.lemon.testcases;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.lemon.apidefinition.ApiCall;
import com.lemon.common.BaseTest;
import com.lemon.pojo.loginCaseData;
import com.lemon.util.ExcelUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//用例层
public class LoginTest extends BaseTest {
    @Test
    public void test_login_success(){
        String jsonData = "{\"principal\":\"weipeng\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}";
        String loginUrl = "http://mall.lemonban.com:8107/login";
        Response loginRes = ApiCall.login(jsonData);
    }

    //参数化
    //方式一：数据存储在代码容器内，比如数组。使用TestNG提供的DataProvider注解
    @Test(dataProvider = "loginDatas")
    public void test_login_from_array(String jsonData){
        String loginUrl = "http://mall.lemonban.com:8107/login";
        Response loginRes = ApiCall.login(jsonData);
    }
    @DataProvider
    public Object[] loginDatas(){
        Object[]  datas =
                {
                      "{\"principal\":\"weipeng\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}",
                      "{\"principal\":\"\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}",
                      "{\"principal\":\"weipeng\",\"credentials\":\"\",\"appType\":3,\"loginType\":0}",
                      "{\"principal\":\"wei1234\",\"credentials\":\"tester\",\"appType\":3,\"loginType\":0}"
                };
        return datas;
    }

    //方式二：通过读取外部文件中的测试数据，实现参数化测试。比如Excel，使用easyPOI技术读取数据
    @Test(dataProvider = "dataFromExcel")
    public void test_login_from_excel(loginCaseData loginCaseData){
        String url = "http://mall.lemonban.com:8107/login";
                         //因为集合当中的每个元素都是loginCaseData的对象，所以要用get方法才能取到值
        Response loginRes = ApiCall.login(loginCaseData.getInputParams());

        //断言
        String assertResponse = loginCaseData.getAssertResponse();    //获取到Excel表格中响应断言的原始字符串

        assertResponse(assertResponse,loginRes);


        //抽取成为公共断言方法。各测试类extends，就可以调用断言方法
//        //json如何转化为Map？？  【引入fastjson依赖】
//        //{"statusCode":200,"nickName":"weipeng"}
//        Map<String,Object> map = JSONObject.parseObject(assertResponse);
//        //遍历Map
//        Set<Entry<String,Object>> datas = map.entrySet();  //获取所有的键值对并保存
//        for (Entry<String,Object> keyValue:datas){
//            String name = keyValue.getKey();
//            Object value = keyValue.getValue();
//            if("statusCode".equals(name)){
//                System.out.println(name);
//                System.out.println(value);
//                Assert.assertEquals(loginRes.getStatusCode(),value);
//            }else{
//                Assert.assertEquals(loginRes.jsonPath().get(name),value);
//            }
//        }






//      String[] data01 = assertResponse.split(",");
//        for (int i=0;i<data01.length;i++){
//            String[] data02 = data01[i].split(":");
//            String name = data02[0];
//            String value = data02[1];
//            if(name.equals("statusCode")){
//                System.out.println(value.getClass());    //String类型。Java是强数据类型，int与String类型 无法比较。
//                Assert.assertEquals(loginRes.getStatusCode(),value);
//            }else{
//                Assert.assertEquals(loginRes.jsonPath().get(value),value);
//            }
//        }
    }
    @DataProvider
    public Object[] dataFromExcel() throws Exception {
        return ExcelUtil.readExcel(0);

//        ImportParams importParams = new ImportParams();
//        FileInputStream file = new FileInputStream("src/test/resources/loginData.xlsx");
//        List<loginCaseData> datas = ExcelImportUtil.importExcel(file,loginCaseData.class,importParams);
//        // 注意：读取到的数据保存在集合当中，但是@DataProvider注解需要返回的是 一维/二维数组【谨记】
//        return datas.toArray();
    }
}