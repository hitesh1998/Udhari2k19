package com.example.udhary;

public class RetrivePayment {
    String pAmunt,pDate;
    RetrivePayment(){

    }

    public RetrivePayment(String pAmunt,String pDate) {
        this.pAmunt = pAmunt;
        this.pDate=pDate;
    }

    public String getpAmunt() {
        return pAmunt;
    }

    public void setpAmunt(String pAmunt) {
        this.pAmunt = pAmunt;
    }
    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }
}
