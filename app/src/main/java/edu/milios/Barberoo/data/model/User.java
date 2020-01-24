package edu.milios.Barberoo.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    private String Uid;
    private String Name;
    private String Surname;
    private String email;
    private boolean IsBarber;
    private String HasBarber = null;

    private List<Appointment> appointments = new ArrayList();

    public User(){

    }

    public User(String Uid, String Name,String Surname,String email,boolean IsBarber,String HasBarber, List<Appointment> appointments) {
        this.Uid = Uid;
        this.Name = Name;
        this.Surname = Surname;
        this.email = email;
        this.HasBarber = HasBarber;
        this.IsBarber = IsBarber;
        this.appointments = appointments;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", this.getUid());
        result.put("hasBarber", this.getHasBarber());
        result.put("isBarber", this.isBarber());
        result.put("name", this.getName());
        result.put("surname", this.getSurname());
        result.put("email", this.getEmail());
        HashMap<String,Appointment> ab =  new HashMap();
        for (Appointment aa : this.getAppointments()) {
            ab.put(aa.getUuid(),aa);

        }
        result.put("appointments", ab);


        return result;
    }

    public User getThis(){
        return this;
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

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Appointment> getAppointments() {
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
