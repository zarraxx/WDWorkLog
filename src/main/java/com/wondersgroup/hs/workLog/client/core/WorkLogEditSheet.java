package com.wondersgroup.hs.workLog.client.core;

/**
 * Created by zarra on 15/3/8.
 */
public class WorkLogEditSheet {
    static public class Project{
        public String id;
        public String name;

        @Override
        public String toString() {
            return "Project{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    static public class Module{
        public String id;
        public String name;

        @Override
        public String toString() {
            return "Moudle{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    static public class DayCost{
        public String value;
        public String displayValue;

        @Override
        public String toString() {
            return "DayCost{" +
                    "value='" + value + '\'' +
                    ", displayValue='" + displayValue + '\'' +
                    '}';
        }
    }

    String stWeekStart;//周次
    String stUserName;//员工姓名
    String nmProjectSn;//项目名称
    String nmTaskSn;//模块/子系统名
    String stTaskType;//任务类型
    String nmWeekCost;//本周实际投入(天)
    String nmOvertimeWork;//＋  弹性工作(小时)
    String stWeekDay;//实际投入日期
    String stWorkDesc;//工作描述
    String nmWeekPlanCost;//本周计划投入(天)
    String nmAlreadyCost;//已经投入(天)

    String userID;
    String id;


    Project[] projects;
    Module[]  modules;
    String[]  taskTypes;
    DayCost[] dayCosts;

    @Override
    public String toString() {
        return "WorkLogEditSheet{" +
                "id='" + id + '\'' +
                ", stWeekStart='" + stWeekStart + '\'' +
                ", stUserName='" + stUserName + '\'' +
                ", nmProjectSn='" + nmProjectSn + '\'' +
                ", nmTaskSn='" + nmTaskSn + '\'' +
                ", stTaskType='" + stTaskType + '\'' +
                ", nmWeekCost='" + nmWeekCost + '\'' +
                ", nmOvertimeWork='" + nmOvertimeWork + '\'' +
                ", stWeekDay='" + stWeekDay + '\'' +
                ", stWorkDesc='" + stWorkDesc + '\'' +
                ", nmWeekPlanCost='" + nmWeekPlanCost + '\'' +
                ", nmAlreadyCost='" + nmAlreadyCost + '\'' +
                ", userID='" + userID + '\'' +
                '}';
    }

    public String getStWeekStart() {
        return stWeekStart;
    }

    public void setStWeekStart(String stWeekStart) {
        this.stWeekStart = stWeekStart;
    }

    public String getStUserName() {
        return stUserName;
    }

    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }

    public String getNmProjectSn() {
        return nmProjectSn;
    }

    public void setNmProjectSn(String nmProjectSn) {
        this.nmProjectSn = nmProjectSn;
    }

    public String getNmTaskSn() {
        return nmTaskSn;
    }

    public void setNmTaskSn(String nmTaskSn) {
        this.nmTaskSn = nmTaskSn;
    }

    public String getStTaskType() {
        return stTaskType;
    }

    public void setStTaskType(String stTaskType) {
        this.stTaskType = stTaskType;
    }

    public String getNmWeekCost() {
        return nmWeekCost;
    }

    public void setNmWeekCost(String nmWeekCost) {
        this.nmWeekCost = nmWeekCost;
    }

    public String getNmOvertimeWork() {
        return nmOvertimeWork;
    }

    public void setNmOvertimeWork(String nmOvertimeWork) {
        this.nmOvertimeWork = nmOvertimeWork;
    }

    public String getStWeekDay() {
        return stWeekDay;
    }

    public void setStWeekDay(String stWeekDay) {
        this.stWeekDay = stWeekDay;
    }

    public String getStWorkDesc() {
        return stWorkDesc;
    }

    public void setStWorkDesc(String stWorkDesc) {
        this.stWorkDesc = stWorkDesc;
    }

    public String getNmWeekPlanCost() {
        return nmWeekPlanCost;
    }

    public void setNmWeekPlanCost(String nmWeekPlanCost) {
        this.nmWeekPlanCost = nmWeekPlanCost;
    }

    public String getNmAlreadyCost() {
        return nmAlreadyCost;
    }

    public void setNmAlreadyCost(String nmAlreadyCost) {
        this.nmAlreadyCost = nmAlreadyCost;
    }

    public Project[] getProjects() {
        return projects;
    }

    public void setProjects(Project[] projects) {
        this.projects = projects;
    }

    public Module[] getModules() {
        return modules;
    }

    public void setModules(Module[] modules) {
        this.modules = modules;
    }

    public String[] getTaskTypes() {
        return taskTypes;
    }

    public void setTaskTypes(String[] taskTypes) {
        this.taskTypes = taskTypes;
    }

    public DayCost[] getDayCosts() {
        return dayCosts;
    }

    public void setDayCosts(DayCost[] dayCosts) {
        this.dayCosts = dayCosts;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
