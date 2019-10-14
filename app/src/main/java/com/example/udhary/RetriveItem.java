package com.example.udhary;

public class RetriveItem {
    private String uItem,uAmount,uDate;


    public RetriveItem()
    {

    }

    public RetriveItem(String uItem, String uAmount,String uDate) {
        this.uItem = uItem;
        this.uAmount = uAmount;
        this.uDate=uDate;
    }

    public String getuItem() {
        return uItem;
    }

    public void setuItem(String uItem) {
        this.uItem = uItem;
    }

    public String getuAmount() {
        return uAmount;
    }

    public void setuAmount(String uAmount) {
        this.uAmount = uAmount;
    }
    public String getuDate() {
        return uDate;
    }
    public void setuDate(String uDate) {
        this.uDate = uDate;
    }
}
