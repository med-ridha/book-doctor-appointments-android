package tn.ridha.Beans;

import java.io.Serializable;

public class UserData implements Serializable {
    private String _id;
    private String name;
    private String email;
    private String city;
    private String phone;
    private String birthdate;

    public UserData(String _id, String name, String email, String city,String phone, String birthdate){
        this._id = _id;
        this.email = email;
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.birthdate = birthdate;
    }
    public UserData(){
    }
    public void set_id(String name) {
        this._id = _id;
    }
    public String get_id() {
        return _id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() { return email; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
