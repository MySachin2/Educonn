package com.phacsin.student.recyclerviewnotifications;

/**
 * Created by Bineesh P Babu on 08-01-2017.
 */

public class DataModelNotification {

    String name;
    String body;
    String date;
            int id_;
            int image;
    public DataModelNotification()
    {

    }
    public DataModelNotification(String name, String body,String date, int id_, int image) {
            this.name = name;
            this.body = body;
            this.date=date;
            this.id_ = id_;
            this.image=image;
            }

    public String getName() {
            return name;
            }

    public String getBody() {
            return body;
            }
    public String getDate(){
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public void setDate(String date){
        this.date = date;
    }

    public int getImage() {
            return image;
            }

    public int getId() {
            return id_;
            }
}
