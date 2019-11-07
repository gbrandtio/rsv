package com.example.root.rsv.models;

public class Car {

    private String id,name,category,kms,license_plate,total_rsvs,image,monthly_rsvs;

    public Car(){
        //default constructor.
    }

    public Car(String id, String name, String category, String kms, String license_plate, String total_rsvs,String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.kms = kms;
        this.license_plate = license_plate;
        this.total_rsvs = total_rsvs;
        this.image = image;
    }

    public Car(String id, String name, String category, String kms, String license_plate, String total_rsvs,String image
    ,String monthly_rsvs) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.kms = kms;
        this.license_plate = license_plate;
        this.total_rsvs = total_rsvs;
        this.image = image;
        this.monthly_rsvs = monthly_rsvs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getTotal_rsvs() {
        return total_rsvs;
    }

    public void setTotal_rsvs(String total_rsvs) {
        this.total_rsvs = total_rsvs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMonthly_rsvs() {
        return monthly_rsvs;
    }

    public void setMonthly_rsvs(String monthly_rsvs) {
        this.monthly_rsvs = monthly_rsvs;
    }
}
