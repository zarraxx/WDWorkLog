package com.wondersgroup.hs.workLog.client.core;

/**
 * Created by zarra on 15/3/8.
 */
public class WorkLog {
    String status;
    String id;
    String projectName;
    String moduleName;
    String taskType;
    String workedDay;
    String useDay;
    String flexDay;
    String planDay;
    String willDay;
    String desc;
    String date;
    String modifyTime;

    String url;

    @Override
    public String toString() {
        return "WorkLog{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", projectName='" + projectName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", taskType='" + taskType + '\'' +
                ", workedDay='" + workedDay + '\'' +
                ", useDay='" + useDay + '\'' +
                ", flexDay='" + flexDay + '\'' +
                ", planDay='" + planDay + '\'' +
                ", willDay='" + willDay + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", modifyTime='" + modifyTime + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getWorkedDay() {
        return workedDay;
    }

    public void setWorkedDay(String workedDay) {
        this.workedDay = workedDay;
    }

    public String getUseDay() {
        return useDay;
    }

    public void setUseDay(String useDay) {
        this.useDay = useDay;
    }

    public String getFlexDay() {
        return flexDay;
    }

    public void setFlexDay(String flexDay) {
        this.flexDay = flexDay;
    }

    public String getPlanDay() {
        return planDay;
    }

    public void setPlanDay(String planDay) {
        this.planDay = planDay;
    }

    public String getWillDay() {
        return willDay;
    }

    public void setWillDay(String willDay) {
        this.willDay = willDay;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
