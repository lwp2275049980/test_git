package com.lemon.testcases;

import com.lemon.apidefinition.ApiCall;
import com.lemon.common.BaseTest;
import com.lemon.pojo.loginCaseData;
import com.lemon.service.BusinessFlow;
import com.lemon.util.ExcelUtil;
import com.lemon.util.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterTest extends BaseTest {
    @Test(dataProvider = "readExcel")
    public void test_register_success(loginCaseData loginCaseData){
        /*
        //发送验证码接口
        String randomPhone = RandomData.randomPhone();
        String randomName = RandomData.randomName();
        Environment.saveToEnvironment("randomPhone",randomPhone);
        Environment.saveToEnvironment("randomName",randomName);
        String data01 = "{\"mobile\":\"#randomPhone#\"}";
        Response sendRes = ApiCall.sendRegisterSms(data01);

        //校验验证码接口
        String sql = "SELECT mobile_code from tz_sms_log WHERE id =(SELECT MAX(id) FROM tz_sms_log);";
        //通过查库，获取到验证码并保存到环境变量中
        String validCode = (String) JDBCUtils.querySingleData(sql);
        Environment.saveToEnvironment("validCode",validCode);
        String data02 = "{\"mobile\":\"#randomPhone#\",\"validCode\":\"#validCode#\"}";
        Response checkRes = ApiCall.checkRegisterSms(data02);
        String smsFlag = checkRes.body().asString();
        Environment.saveToEnvironment("smsFlag",smsFlag);
         */

        String smsFlag = BusinessFlow.send_check_sms();
        //注册接口
        String data03 = "{\"appType\":3,\"checkRegisterSmsFlag\":\"#smsFlag#\"," +
                "\"mobile\":\"#randomPhone#\",\"userName\":\"#randomName#\",\"password\":\"123456\"," +
                "\"registerOrBind\":1,\"validateType\":1}";
        System.out.println("================================================");
        Response registerRes = ApiCall.register(data03);

        //断言--响应体数据
        Assert.assertEquals(registerRes.getStatusCode(),200);
        System.out.println("状态码断言 "+"实际状态码值： "+registerRes.getStatusCode());

        //断言-- 1,数据库（自造）
        String sql = "select count(*) from tz_user where user_mobile = '#randomPhone#';";
        long count = (long) JDBCUtils.querySingleData(sql);
        Assert.assertEquals(JDBCUtils.querySingleData(sql).toString(),1+"");
        System.out.println("当遇到断言值类型不同的问题，针对实际值和预期值做字符串化，即 对象.toString() 然后做断言对比");

        //断言-- 2,数据库（读取Excel数据）
        String assertDB = loginCaseData.getAssertDB();
        assertDBResponse(assertDB);
        System.out.println("数据库断言");

    }
    @DataProvider
    public Object[] readExcel(){
        return ExcelUtil.readExcel(1);
    }
}
