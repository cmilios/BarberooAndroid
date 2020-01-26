package edu.milios.Barberoo.data.model;

import com.google.firebase.database.Exclude;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Appointment {

    private String date;
    private String time;
    private String barberId;
    private String type;
    private Boolean state;
    private String uuid;
    private String userId;

    public Appointment(){

    }

    public Appointment(String uuid,String userId, String date, String time, String barberId, String type, Boolean state){
        this.date = date;
        this.time = time;
        this.barberId = barberId;
        this.userId = userId;
        this.state = state;
        this.type = type;
        if(uuid!=null){
            this.uuid = uuid;
        }
        else{
            this.uuid = UUID.randomUUID().toString();
        }

    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
