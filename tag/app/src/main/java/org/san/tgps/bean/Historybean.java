package org.san.tgps.bean;

/**
 * Created by SANTECH on 17/03/2016.
 */
public class Historybean {

    private String h_lattitude;
    private String h_longitude;
    private int imei;
    private String h_ddegree;
    private String h_cdate;
    private String pTime;

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getH_lattitude() {
        return h_lattitude;
    }

    public void setH_lattitude(String h_lattitude) {
        this.h_lattitude = h_lattitude;
    }

    public String getH_longitude() {
        return h_longitude;
    }

    public void setH_longitude(String h_longitude) {
        this.h_longitude = h_longitude;
    }

    public int getImei() {
        return imei;
    }

    public void setImei(int imei) {
        this.imei = imei;
    }

    public String getH_ddegree() {
        return h_ddegree;
    }

    public void setH_ddegree(String h_ddegree) {
        this.h_ddegree = h_ddegree;
    }

    public String getH_cdate() {
        return h_cdate;
    }

    public void setH_cdate(String h_cdate) {
        this.h_cdate = h_cdate;
    }
}
