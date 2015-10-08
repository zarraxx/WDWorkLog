package com.wondersgroup.hs.workLog.client.core;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zarra on 15/3/8.
 */
public class LogClientWSImpl implements LogClient {

    public Cookie getSessionCookie() throws Exception {
        try (CloseableHttpClient client  = WSUtils.createHttpClient()) {
            HttpClientContext context = WSUtils.createClientContext(null);
            CookieStore cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
            HttpGet httpGet = new HttpGet(WSUtils.APPBaseURL+"wad/vdmLogon.jsp");
            try(CloseableHttpResponse response = client.execute(httpGet,context)){
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.size() > 0) {
                    //System.out.println(cookies.get(0));
                    return cookies.get(0);
                } else {
                    return null;
                }
            }
        }
    }

    public boolean postLogin(String username, String password, Cookie cookie) throws Exception {
        String loginURL = "wad/vdmLogon.jsp";
        Map<String, String> form = new HashMap<>();

        form.put("UserId", username);
        form.put("PassWord", password);
        try(CloseableHttpClient httpClient = WSUtils.createHttpClient()) {
            try (CloseableHttpResponse response = WSUtils.postMethodForClient(httpClient, loginURL, form, cookie)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 302)
                    return true;
                else
                    return false;
            }
        }
    }

    /**
     * 取得所有可选的week，第一个元素为当前选中的week
    * */
    public String[] getWeeks(String html){
        List<String> cache = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements selects = doc.getElementsByAttributeValue("name", "stWeekStart");
        for(Element element:selects){
            Elements options = element.getElementsByTag("option");
            for(Element option : options){
                if(option.hasAttr("selected")){
                    cache.add(0,option.text());
                    ///System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
                }
                cache.add(option.text());
                //System.out.println(option.text());
            }

        }
        return cache.toArray(new String[0]);
    }

    public String[] getFuncURLS(String html){
        List<String> cache = new ArrayList<>();
        String[] lines = html.split("\n");
        //System.out.println(body);
        String pattern = "\"(.*?)\"";
        Pattern r = Pattern.compile(pattern);
        for (String line : lines){
            if(line.contains("var tabDetailFrameSrc")) {
                Matcher m = r.matcher(line);
                while(m.find()) {
                    //System.out.println("Found value: " + m.group(1));
                    cache.add(m.group(1));
                }

            }
                //System.out.println(line);

        }
        return cache.toArray(new String[0]);
    }

    public String getPersonWorkLogPage(Cookie cookie) throws Exception {
        String inputHomeURL = "addPersonWorkLog.jsp";
        String body = WSUtils.getMethod(inputHomeURL, cookie);

        return body;
    }

    public String getLogsPage(String queryURL,String week,Cookie cookie) throws Exception {
        List<WorkLog> workLogs = new ArrayList<>();
        String url = queryURL +week;
        String html =  WSUtils.getMethod(url, cookie);
        return html;
    }

    public WorkLog[] getWorkLogs(String html){
        List<WorkLog> workLogs = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Element table = doc.getElementById("mainResultData");
        Elements rows = table.getElementsByAttributeValue("onmouseover","this.className='selected'");
        for(Element tr : rows){
            Elements fields = tr.getElementsByAttribute("align");
            if(fields.size() >= 13) {
                WorkLog workLog = new WorkLog();
                workLog.setStatus(fields.get(0).text());
                workLog.setId(fields.get(1).text());
                workLog.setProjectName(fields.get(2).text());
                workLog.setModuleName(fields.get(3).text());
                workLog.setTaskType(fields.get(4).text());
                workLog.setWorkedDay(fields.get(5).text());
                workLog.setUseDay(fields.get(6).text());
                workLog.setFlexDay(fields.get(7).text());
                workLog.setPlanDay(fields.get(8).text());
                workLog.setWillDay(fields.get(9).text());
                workLog.setDesc(fields.get(10).text());
                workLog.setDate(fields.get(11).text());
                workLog.setModifyTime(fields.get(12).text());

                Elements links = fields.get(1).getElementsByTag("a");
                if (links.size() > 0) {
                    Element link = links.get(0);
                    String href = link.attr("href");
                    workLog.setUrl(href);
                }

                workLogs.add(workLog);
            }

        }
        return workLogs.toArray(new WorkLog[0]);
    }

    public String[] getLogFuncURLs(String html) throws IOException {
        List<String> cache = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        Element inputAdd    = doc.getElementById("addButton");
        Element inputDelete = doc.getElementById("deleteButton");

        String addAction = inputAdd.attr("action");
        String deleteAction = inputDelete.attr("action");

        cache.add(addAction);
        cache.add(deleteAction);

        return cache.toArray(new String[0]);

    }

    public String getEditLogPage(String url,Cookie cookie) throws Exception {
        String body = WSUtils.getMethod("wad/" + url, cookie);
        return body;
    }

    public WorkLogEditSheet getEditSheet(String html){
        return getEditSheet(Jsoup.parse(html));
    }

    public WorkLogEditSheet getEditSheet(Document doc){
        WorkLogEditSheet editSheet = new WorkLogEditSheet();
        Elements fields = doc.getElementsByAttributeValue("class","colObj");


        System.out.println(fields.size());

        int beginIndex = 0;

        if (fields.size() == 17)
            beginIndex = 6;

        Element tdWeek = fields.get(0+beginIndex);
        String v = tdWeek.getElementsByTag("input").get(0).attr("value");
        editSheet.setStWeekStart(v);

        Element tdUserName = fields.get(1+beginIndex);
        v = tdUserName.getElementsByTag("input").get(0).attr("value");
        editSheet.setStUserName(v);

        Element tdProject = fields.get(2+beginIndex);
        v = tdProject.getElementsByTag("input").get(0).attr("value");
        editSheet.setNmProjectSn(v);

        for (Element element : tdProject.getElementsByTag("select")){
            if (element.attr("style").equals("display:none")){
                List<WorkLogEditSheet.Project> projects = new ArrayList<>();
                for (Element e:element.getElementsByTag("option")){
                    WorkLogEditSheet.Project project = new WorkLogEditSheet.Project();
                    String key = e.attr("value");
                    String value = e.text();
                    project.id = key;
                    project.name = value;
                    projects.add(project);
                }
                editSheet.setProjects(projects.toArray(new WorkLogEditSheet.Project[0]));
            }
        }

        Element tdModule = fields.get(3+beginIndex);
        v = tdModule.getElementsByTag("input").get(0).attr("value");
        editSheet.setNmTaskSn(v);
        for (Element element : tdModule.getElementsByTag("select")){
            if (element.attr("style").equals("display:none")){
                List<WorkLogEditSheet.Module> modules = new ArrayList<>();
                for (Element e:element.getElementsByTag("option")){
                    WorkLogEditSheet.Module module = new WorkLogEditSheet.Module();
                    String key = e.attr("value");
                    String value = e.text();
                    module.id = key;
                    module.name = value;
                    modules.add(module);
                }
                editSheet.setModules(modules.toArray(new WorkLogEditSheet.Module[0]));
            }
        }

        Element tdTaskType = fields.get(4+beginIndex);
        v = tdTaskType.getElementsByTag("input").get(0).attr("value");
        editSheet.setStTaskType(v);
        for (Element element : tdTaskType.getElementsByTag("select")){
            if (element.attr("style").equals("display:none")){
                List<String> types = new ArrayList<>();
                for (Element e:element.getElementsByTag("option")){
                    String value = e.text();
                    types.add(value);
                }
                editSheet.setTaskTypes(types.toArray(new String[0]));
            }
        }

        Element tdWeekCost = fields.get(5+beginIndex);
        v = tdWeekCost.getElementsByTag("input").get(0).attr("value");
        editSheet.setNmWeekCost(v);
        for (Element element : tdWeekCost.getElementsByTag("select")){
            if (element.attr("style").equals("display:none")){
                List<WorkLogEditSheet.DayCost> costs = new ArrayList<>();
                for (Element e:element.getElementsByTag("option")){
                    WorkLogEditSheet.DayCost dayCost = new WorkLogEditSheet.DayCost();
                    String key = e.attr("value");
                    String dis = e.text();
                    dayCost.value = key;
                    dayCost.displayValue = dis;
                    costs.add(dayCost);
                }
                editSheet.setDayCosts(costs.toArray(new WorkLogEditSheet.DayCost[0]));
            }
        }

        Element tdOvertimeWork = fields.get(6+beginIndex);
        v = tdOvertimeWork.getElementsByTag("input").get(0).attr("value");
        editSheet.setNmOvertimeWork(v);

        Element tdWeekDay = fields.get(7+beginIndex);
        v = tdWeekDay.getElementsByTag("input").get(1).attr("value");
        editSheet.setStWeekDay(v);

        Element tdWorkDesc = fields.get(8+beginIndex);
        v = tdWorkDesc.getElementsByTag("textarea").get(0).text();
        editSheet.setStWorkDesc(v);

        Element tdWeekPlanCost = fields.get(9+beginIndex);
        v = tdWeekPlanCost.getElementsByTag("input").get(0).attr("value");
        editSheet.setNmWeekPlanCost(v);

        Element tdAlreadyCost = fields.get(10+beginIndex);
        v = tdAlreadyCost.getElementsByTag("input").get(0).attr("value");
        editSheet.setNmAlreadyCost(v);

        Element inputUserID = doc.getElementsByAttributeValue("name","nmUserSn").first();
        editSheet.setUserID(inputUserID.attr("value"));

        Element inputID = doc.getElementsByAttributeValue("name","nmSn").first();
        if (inputID!=null)
            editSheet.setId(inputID.attr("value"));

        return editSheet;
    }

    public String[] getThisWeekData(String week,String id,Cookie cookie) throws Exception {
        String url = "selectWeekDays.jsp?stWeekStart="+week+"&stProjectSn="+id;
        List<String> cache = new ArrayList<>();

        String html = WSUtils.getMethod(url, cookie);

        Document doc = Jsoup.parse(html);

        for (Element input :doc.getElementsByTag("Input")){
            String value = input.attr("value");
            if (value!=null && value.length()>0 && value.contains("/")){
                cache.add(value);
            }
        }

        return cache.toArray(new String[0]);
    }

    public boolean submitWorkLog(Map<String,String> body,Cookie cookie) throws Exception {
        String funcName = body.get("wadFormName");

        String url = "submitReloadParentWhenSuccessPMT.jsp?&funcXMLName=WorkLog&funcName=";

        if (funcName.contains("addOne")){
            url = url+"addOneWorkLogByWeek";
        }else if(funcName.contains("modifyOne")){
            url = url+"modifyWorkLogByWeek";
        }

        String html = WSUtils.postMethod(url,body,cookie);

        //System.out.println(html);

        return html.contains("工作日志表成功");
    }

    public boolean removeWorkLog(String id,Cookie cookie) throws Exception {
        String url = "wad/submitReloadWhenSuccess.jsp?&funcXMLName=WAD_WorkLog_Config.xml&funcName=removeWorkLog";

        Map<String,String> postData = new HashMap<>();

        postData.put("wadCurrentPage","1");
        postData.put("wadSummaryLine","null");
        postData.put("nmSn",id);

        String html = WSUtils.postMethod(url,postData,cookie);

        return html.contains("工作日志表成功");

    }
}
