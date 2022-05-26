package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class loginCaseData {
    @Excel(name = "用例编号")
    private int caseId;
    @Excel(name = "用例标题")
    private String caseTitle;
    @Excel(name = "接口入参")
    private String inputParams;
    @Excel(name = "响应断言")
    private String assertResponse;
    @Excel(name = "数据库断言")
    private String assertDB;

    //空参构造
    public loginCaseData(){
    }

    //set,get方法
    public void setCaseId(int caseId){
        this.caseId = caseId;
    }
    public int getCaseId(){
        return caseId;
    }

    public void setCaseTitle(String caseTitle){
        this.caseTitle = caseTitle;
    }
    public String getCaseTitle(){
        return caseTitle;
    }

    public void setInputParams(String inputParams){
        this.inputParams = inputParams;
    }
    public String getInputParams(){
        return inputParams;
    }

    public void setAssertResponse(String assertResponse){
        this.assertResponse = assertResponse;
    }
    public String getAssertResponse(){
        return assertResponse;
    }

    public void setAssertDB(String assertDB){
        this.assertDB = assertDB;
    }
    public String getAssertDB(){
        return assertDB;
    }


    //重写toString()
    @Override
    public String toString() {
        return "loginCaseData{" +
                "caseId=" + caseId +
                ", caseTitle='" + caseTitle + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", assertResponse='" + assertResponse + '\'' +
                ", assertDB='" + assertDB + '\'' +
                '}';
    }
}
