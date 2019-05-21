package ib.edu.Abtester;

import java.util.Date;
import java.util.List;

public class TestResult {

    private String userId;
    private String testName;
    private Date date;
    private List<Float> xPositionsList;
    private List<Float> yPositionsList;
    private List<Long> timeList;
    private List<Boolean> stateList;

    TestResult(String userId, String testName, Date date, List<Float> xPositionsList, List<Float> yPositionsList, List<Long> timeList, List<Boolean> stateList) {
        this.userId = userId;
        this.testName = testName;
        this.date = date;
        this.xPositionsList = xPositionsList;
        this.yPositionsList = yPositionsList;
        this.timeList = timeList;
        this.stateList = stateList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Float> getxPositionsList() {
        return xPositionsList;
    }

    public void setxPositionsList(List<Float> xPositionsList) {
        this.xPositionsList = xPositionsList;
    }

    public List<Float> getyPositionsList() {
        return yPositionsList;
    }

    public void setyPositionsList(List<Float> yPositionsList) {
        this.yPositionsList = yPositionsList;
    }

    public List<Long> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<Long> timeList) {
        this.timeList = timeList;
    }

    public List<Boolean> getStateList() {
        return stateList;
    }

    public void setStateList(List<Boolean> stateList) {
        this.stateList = stateList;
    }
}
