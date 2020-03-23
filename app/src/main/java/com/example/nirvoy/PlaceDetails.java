package com.example.nirvoy;

public class PlaceDetails {
    int placeImageResource;
    private String placeType="no type";
    private String placeId="no id";
    private String placeName="no name";
    private String placeRating="no rating";
    private String placeNumber="no number";
    private double placeLat,placeLng;


    public PlaceDetails(int placeImageResource, String placeId, String placeName, String number, String placeRating,String placeType, double placeLat, double placeLng) {
        this.placeImageResource = placeImageResource;
        this.placeNumber = number;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeRating = placeRating;
        this.placeType = placeType;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
    }

    public PlaceDetails(int placeImageResource, String placeId, String placeName, String number,String placeType, double placeLat, double placeLng) {
        this.placeImageResource = placeImageResource;
        this.placeNumber = number;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
    }

    public PlaceDetails(int placeImageResource, String placeId, String placeName,String placeType, double placeLat, double placeLng) {
        this.placeImageResource = placeImageResource;
        this.placeId = placeId;
        this.placeName = placeName;
        this.placeType = placeType;
        this.placeLat = placeLat;
        this.placeLng = placeLng;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public double getPlaceLng() {
        return placeLng;
    }

    public int getImageResource() {
        return placeImageResource;
    }

    public String getPlaceNumber() {
        return placeNumber;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceRating() {
        return placeRating;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceNumber(String placeNumber) {
        this.placeNumber = placeNumber;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setPlaceRating(String placeRating) {
        this.placeRating = placeRating;
    }


}
