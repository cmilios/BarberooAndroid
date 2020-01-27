package edu.milios.Barberoo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Appointment implements Parcelable   {

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

    protected Appointment(Parcel in) {
        date = in.readString();
        time = in.readString();
        barberId = in.readString();
        type = in.readString();
        byte stateVal = in.readByte();
        state = stateVal == 0x02 ? null : stateVal != 0x00;
        uuid = in.readString();
        userId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(barberId);
        dest.writeString(type);
        if (state == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (state ? 0x01 : 0x00));
        }
        dest.writeString(uuid);
        dest.writeString(userId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Appointment> CREATOR = new Parcelable.Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };
}
