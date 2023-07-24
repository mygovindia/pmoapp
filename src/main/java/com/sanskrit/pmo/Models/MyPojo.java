package com.sanskrit.pmo.Models;

public class MyPojo {
    private String DOA;

    private String DOB;

    private String notifs_on_off;

    public String getDOA() {
        return DOA;
    }

    public void setDOA(String DOA) {
        this.DOA = DOA;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getNotifs_on_off() {
        return notifs_on_off;
    }

    public void setNotifs_on_off(String notifs_on_off) {
        this.notifs_on_off = notifs_on_off;
    }

    @Override
    public String toString() {
        return "ClassPojo [DOA = " + DOA + ", DOB = " + DOB + ", notifs_on_off = " + notifs_on_off + "]";
    }
}