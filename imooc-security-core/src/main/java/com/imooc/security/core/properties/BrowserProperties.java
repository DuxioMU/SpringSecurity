package com.imooc.security.core.properties;

public class BrowserProperties {

    private String loginPage = "/imooc-signIn.html";

    private LoginType loginType = LoginType.JSON;

    private  int rememberMeSecods = 3600;

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage)
    {
        this.loginPage = loginPage;
    }

    public int getRememberMeSecods() {
        return rememberMeSecods;
    }

    public void setRememberMeSecods(int rememberMeSecods) {
        this.rememberMeSecods = rememberMeSecods;
    }
}
