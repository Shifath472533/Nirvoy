package com.example.nirvoy;

public class ContactData {

    private String name;
    private String number;

    public ContactData(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public ContactData(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
