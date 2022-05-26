package com.lemon.util;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.pojo.loginCaseData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class ExcelUtil {
    private static final String excelPath = "src/test/resources/loginData.xlsx";

    /**
     * 读取Excel的通用方法
     * @param sheet  开始读取的sheet位置，默认为0，即第一张sheet
     * @return  返回Object[]类型的一维数组
     * 用于dataProvider
     */
    public static Object[] readExcel(int sheet){
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheet);    //开始读取的sheet位置，默认为0，即第一张
        FileInputStream file = null;
        try {
            file = new FileInputStream(excelPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<loginCaseData> datas = null;
        try {
            datas = ExcelImportUtil.importExcel(file,loginCaseData.class,importParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas.toArray();
    }
}
