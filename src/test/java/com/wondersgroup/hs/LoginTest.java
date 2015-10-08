package com.wondersgroup.hs;

import com.wondersgroup.hs.workLog.client.core.LogClientWSImpl;
import com.wondersgroup.hs.workLog.client.core.WorkLog;
import com.wondersgroup.hs.workLog.client.core.WorkLogEditSheet;
import org.apache.http.cookie.Cookie;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zarra on 15/3/8.
 */
public class LoginTest {

    public static String getProperty(String key) throws IOException {
        Properties properties = TestHelper.loadProperties("config");
        return properties.getProperty(key,"");
    }

//    @Test
//    public void testLogin() throws IOException {
//        LogClientWSImpl client = new LogClientWSImpl();
//        Cookie cookie = client.getSessionCookie();
//        Assert.assertTrue(client.postLogin(getProperty("username"),getProperty("password"),cookie));
//        cookie = client.getSessionCookie();
//        Assert.assertTrue(client.postLogin(getProperty("username"), "abcd", cookie));
//    }

    @Test
    public void testGetURL() throws Exception {
        LogClientWSImpl client = new LogClientWSImpl();
        Cookie cookie = client.getSessionCookie();
        Assert.assertTrue(client.postLogin(getProperty("username"),getProperty("password"),cookie));
        String html = client.getPersonWorkLogPage(cookie);
        String[] urls = client.getFuncURLS(html);
        String[] weeks = client.getWeeks(html);

        html = client.getLogsPage(urls[2],weeks[0],cookie);

        WorkLog[] logs = client.getWorkLogs(html);
        for(WorkLog workLog : logs)
            System.out.println(workLog);
        for (String url:client.getLogFuncURLs(html)){
            System.out.println(url);
        }

        String editURL = client.getLogFuncURLs(html)[0];
        //editURL = logs[0].getUrl();

        System.out.println(editURL);

        html = client.getEditLogPage(editURL,cookie);

        WorkLogEditSheet editSheet = client.getEditSheet(html);

        System.out.println(editSheet);

    }
}
