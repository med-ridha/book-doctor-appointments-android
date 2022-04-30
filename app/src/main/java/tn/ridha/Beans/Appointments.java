package tn.ridha.Beans;

import java.io.Serializable;

public class Appointments implements Serializable {
    private String _id;
    private String doctorName;
    private String location;
    private String userName;
    private String userId;
    private String doctorId;
    private String date;
    private String createdDate;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String get_id(){
        return this._id;
    }
    public void set_id(String _id){
        this._id = _id;
    }
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Appointments (){

    }
    public Appointments(String _id, String doctorName, String userName, String userId, String doctorId, String date, String createdDate, String location) {
        this._id = _id;
        this.doctorName = doctorName;
        this.userName = userName;
        this.userId = userId;
        this.doctorId = doctorId;
        this.date = date;
        this.createdDate = createdDate;
        this.location = location;
    }

    public Appointments(String _id, String doctorName, String location, String date){
        this._id = _id;
        this.doctorName = doctorName;
        this.location = location;
        this.date = date;
    }

}
