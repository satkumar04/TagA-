package org.san.tgps.bean;

/**
 * Created by SANTECH on 03/30/2018.
 */

public class AlarmsBean {
    private String alarm_date;
    private String alarm_time;
    private String device_name;
    private String alarmaddress;
    private String alarmtype;
    private String regnumber;

    public String getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getalarmaddress() {
        return alarmaddress;
    }

    public void setalarmaddress(String sosalarmaddress) {
        this.alarmaddress = sosalarmaddress;
    }

    public String getAlarmtype() {
        return alarmtype;
    }

    public void setAlarmtype(String alarmtype) {
        this.alarmtype = alarmtype;
    }

    public String getRegnumber() {
        return regnumber;
    }

    public void setRegnumber(String regnumber) {
        this.regnumber = regnumber;
    }
}
