package com.lemon.day01;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class HomeWork01 {
    @Test(enabled = false)
    public void loginTest(){
        String loginData = "{\"principal\":\"waiwai\",\"credentials\":\"lemon123456\",\"appType\":3,\"loginType\":0}";
        given().
                header("Content-Type","application/json").
                body(loginData).
        when().
                post("http://mall.lemonban.com:8107/login").
        then().
                log().body();
    }
}
