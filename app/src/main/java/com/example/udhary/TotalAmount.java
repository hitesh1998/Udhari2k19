package com.example.udhary;

public class TotalAmount {

    String tAmount,tDate;

    TotalAmount(){

    }

    public TotalAmount(String tAmunt, String tDate) {
        this.tAmount = tAmunt;
        this.tDate = tDate;
    }

    public String gettAmount() {
        return tAmount;
    }

    public void settAmount(String tAmount) {
        this.tAmount = tAmount;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }
}

