package com.example.nirvoy;

public class CurrentLocation {

    private double latitude, longitude;
    private String address;

    public CurrentLocation(double latitude, double longitude,String address){
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public CurrentLocation(){

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
