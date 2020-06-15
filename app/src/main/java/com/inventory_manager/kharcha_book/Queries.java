package com.inventory_manager.kharcha_book;

public class Queries {
    public static String insert_data(String id,String name,String date,String time,String note,String amount){
        return new String("insert into expenses values('"+id+"','"+name+"','"+date+"','"+time+"','"+amount+"','"+note+"')");
    }

    public  static  String countData(){
        return new String("select count(id) as co from expenses");
    }

    public static String listData(){
        return  new String("SELECT a.id,name,date,amount,note,time,b.path FROM expenses as a,image as b where a.id=b.id");
    }

    public static  String loadSettingData(){
        return new String("select name,lim from expenseslimit");
    }

    public static String reportQuery(){
        return new String("select name,date,amount,time,note from expenses");
    }

    public static  String getWeekCount(){
        return new String("select date from expenses");
    }
}
