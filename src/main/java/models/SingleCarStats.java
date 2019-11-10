package com.example.root.rsv.models;

public class SingleCarStats {
    private String id,name,category,kms,license_plate,total_rsvs,image,
            monthly_rsvs,yearly_rsvs,nationality_percentage,nam_nationality,
            previous_service_date,upcoming_service_date;

    public  SingleCarStats(){
        //default constructor
    }

    public SingleCarStats(String id, String name, String category, String kms, String license_plate,
                          String total_rsvs, String image, String monthly_rsvs, String yearly_rsvs,
                          String nationality_percentage, String nam_nationality, String previous_service_date,
                          String upcoming_service_date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.kms = kms;
        this.license_plate = license_plate;
        this.total_rsvs = total_rsvs;
        this.image = image;
        this.monthly_rsvs = monthly_rsvs;
        this.yearly_rsvs = yearly_rsvs;
        this.nationality_percentage = nationality_percentage;
        this.nam_nationality = nam_nationality;
        this.previous_service_date = previous_service_date;
        this.upcoming_service_date = upcoming_service_date;
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

    public String getYearly_rsvs() {
        return yearly_rsvs;
    }

    public void setYearly_rsvs(String yearly_rsvs) {
        this.yearly_rsvs = yearly_rsvs;
    }

    public String getNationality_percentage() {
        return nationality_percentage;
    }

    public void setNationality_percentage(String nationality_percentage) {
        this.nationality_percentage = nationality_percentage;
    }

    public String getNam_nationality() {
        return nam_nationality;
    }

    public void setNam_nationality(String nam_nationality) {
        this.nam_nationality = nam_nationality;
    }

    public String getPrevious_service_date() {
        return previous_service_date;
    }

    public void setPrevious_service_date(String previous_service_date) {
        this.previous_service_date = previous_service_date;
    }

    public String getUpcoming_service_date() {
        return upcoming_service_date;
    }

    public void setUpcoming_service_date(String upcoming_service_date) {
        this.upcoming_service_date = upcoming_service_date;
    }
}
