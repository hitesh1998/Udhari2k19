package com.example.udhary;

public class Contacts {
    public String cName, cNumber;

    public Contacts() {

    }

    public Contacts(String cName, String cNumber) {
        this.cName = cName;
        this.cNumber = cNumber;
    }

    public String getcNumber() {
        return cNumber;
    }

    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}
