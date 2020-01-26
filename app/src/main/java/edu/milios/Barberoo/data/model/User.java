package edu.milios.Barberoo.data.model;


import com.google.firebase.database.Exclude;

import java.util.ArrayList;


public class User {

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
}
