package com.lemon.util;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;

import java.util.Locale;

import static com.lemon.util.JDBCUtils.querySingleData;

public class RandomData {
    public static void main(String[] args) {
        Faker faker = new Faker(Locale.CHINA);
        System.out.println(faker.phoneNumber().cellPhone());
    }

    /**
     * 随机生成手机号码
     * @return   返回生成的手机号码
     */
    public static String randomPhone(){
        Faker faker = new Faker(Locale.CHINA);
        String randomPhone = faker.phoneNumber().cellPhone();
        String sql = "select count(*) from tz_sms_log where user_phone = '"+randomPhone+"'; ";
        Long count = (Long)JDBCUtils.querySingleData(sql);
        while(true){
            if (count == 0){
                //count为0.表示未注册过，符合要求
                break;
            } else {
                randomPhone = faker.phoneNumber().cellPhone();
                count = (Long)JDBCUtils.querySingleData(sql);
            }
        }
        return randomPhone;
    }


    public static String randomName(){
        Faker faker = new Faker();
        String randomName = faker.name().firstName();
        String sql = "select count(*) from tz_user where user_name = '"+randomName+"';";
        Long count = (Long) JDBCUtils.querySingleData(sql);
        while(true){
            if (count == 0 & randomName.length() >= 4){
                    break;

            }else{
                randomName = faker.name().firstName();
                count = (Long) JDBCUtils.querySingleData(sql);
            }
        }
        return randomName;
    }
}
