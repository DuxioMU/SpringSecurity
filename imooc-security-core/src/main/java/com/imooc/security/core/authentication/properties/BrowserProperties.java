package com.imooc.security.core.authentication.properties;

public class BrowserProperties {

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    private String loginPage = "/imooc-signIn.html";

}
