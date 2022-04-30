package tn.ridha.other;

public class ItemModel {
    private String _id;
    private String name;
    private String speciality;
    private String hospital;
    private String location;
    public ItemModel(String _id, String name, String speciality, String hospital, String location){
        this._id = _id;
        this.name = name;
        this.speciality = speciality;
        this.hospital = hospital;
        this.location = location;
    }


    public String getItemName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getHospital() {
        return hospital;
    }

    public String getLocation() {
        return location;
    }
    public String get_id() {
        return _id;
    }
}
