package com.inventory_manager.kharcha_book;

public class ListViewHelper {
    public String name,amount,note,date,id,imgPath;

    ListViewHelper(String id,String name,String amount,String date,String note,String path){
        this.name=name;
        this.amount=amount;
        this.note=note;
        this.date=date;
        this.id=id;
        this.imgPath=path;
    }
    public String getAmount() {
        return amount;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getNote() {
        return note;
    }
    public String getId() {
        return id;
    }
    public String getImgPath(){return imgPath;}
}
