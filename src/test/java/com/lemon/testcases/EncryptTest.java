package com.lemon.testcases;

import com.lemon.apidefinition.ApiCall;
import com.lemon.encryption.MD5Util;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class EncryptTest {
    @Test
    public void test_md5(){
        String originPwd = "123456";
        String pwd = MD5Util.stringMD5(originPwd);
        String inputParmas = "loginame=admin&password="+pwd;
        Response loginRes = ApiCall.erpLogin(inputParmas);
    }
}
