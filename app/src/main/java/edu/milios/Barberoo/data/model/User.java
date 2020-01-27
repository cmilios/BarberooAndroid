package edu.milios.Barberoo.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;


public class User implements Parcelable {

    private String Uid;
    private String Name;
    private String Surname;
    private String email;
    private boolean IsBarber;
    private String HasBarber = null;

    @Exclude
    private ArrayList<Appointment> appointments;


    public User(){

    }

    public User(String Uid, String Name,String Surname,String email,boolean IsBarber,String HasBarber) {
        this.Uid = Uid;
        this.Name = Name;
        this.Surname = Surname;
        this.email = email;
        this.HasBarber = HasBarber;
        this.IsBarber = IsBarber;
    }



    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBarber() {
        return IsBarber;
    }

    public void setBarber(boolean barber) {
        IsBarber = barber;
    }

    public String getHasBarber() {
        return HasBarber;
    }

    public void setHasBarber(String hasBarber) {
        HasBarber = hasBarber;
    }

    @Override
    public String toString() {
        return getName()+" "+getSurname();
    }

    protected User(Parcel in) {
        Uid = in.readString();
        Name = in.readString();
        Surname = in.readString();
        email = in.readString();
        IsBarber = in.readByte() != 0x00;
        HasBarber = in.readString();
        if (in.readByte() == 0x01) {
            appointments = new ArrayList<Appointment>();
            in.readList(appointments, Appointment.class.getClassLoader());
        } else {
            appointments = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Uid);
        dest.writeString(Name);
        dest.writeString(Surname);
        dest.writeString(email);
        dest.writeByte((byte) (IsBarber ? 0x01 : 0x00));
        dest.writeString(HasBarber);
        if (appointments == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(appointments);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
