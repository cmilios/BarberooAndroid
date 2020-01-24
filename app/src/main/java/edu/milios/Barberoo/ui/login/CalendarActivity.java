package edu.milios.Barberoo.ui.login;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;

import edu.milios.Barberoo.R;
import edu.milios.Barberoo.data.model.Appointment;
import edu.milios.Barberoo.data.model.User;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";

    private RecyclerView recyclerView;
    private EventAdapter mAdapter;
    private User user;
    CalendarView simpleCalendarView ;
    ArrayList appointmentsList = new ArrayList();

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        simpleCalendarView = findViewById(R.id.calendarView);
        long as = simpleCalendarView.getDate();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Calendar");
        setSupportActionBar(toolbar);

        mAuth = getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        int index = mAuth.getCurrentUser().getEmail().indexOf('@');
        int length = mAuth.getCurrentUser().getEmail().length();

        user = new User(currentUser.getUid(),currentUser.getEmail().substring(0,index),
                currentUser.getEmail().substring(index+1,length),currentUser.getEmail(),
                true,"68veprDQ4AStOpH2VysyKROxZBg2", appointmentsList );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();



        ValueEventListener UserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String name = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("name").getValue(String.class);
                String surname = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("surname").getValue(String.class);
                String uid = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("uid").getValue(String.class);
                String hasBarber = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("hasBarber").getValue(String.class);
                Boolean isBarber = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("isBarber").getValue(Boolean.class);
                String email = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("email").getValue(String.class);
                HashMap<String,HashMap<String,Appointment>> appointments = (HashMap<String, HashMap<String, Appointment>>) dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("appointments")
                        .getValue();

                ArrayList<Appointment> appList =  new ArrayList<Appointment>();

                assert appointments != null;
                for (Map.Entry<String, HashMap<String, Appointment>> entry : appointments.entrySet()) {
                    HashMap aa = entry.getValue();
                    Appointment ap = new Appointment((String) aa.get("uuid"),(String) aa.get("date"),
                            (String) aa.get("time"),(String) aa.get("barberId"),
                            (String) aa.get("type"),(Boolean) aa.get("state"));
                    appList.add(ap);
                }


                user = new User(uid, name, surname, email, isBarber,hasBarber,appList);

                ArrayList<Appointment> filteredList = new ArrayList<>();
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(today);
                for (Appointment as:appList) {
                   if(as.getDate().equals(formattedDate)){
                       filteredList.add(as);
                   }

                }
                mAdapter.replaceList(filteredList);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addListenerForSingleValueEvent(UserListener);

        recyclerView = findViewById(R.id.events);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new EventAdapter((ArrayList<Appointment>) user.getAppointments(), CalendarActivity.this);
        recyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(CalendarActivity.this, R.style.AlertDialog)
                        .setTitle("Add new Appointment")
                        .setView(R.layout.new_appoinmentform)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //create new appointment
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();

            }
        });
        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                ArrayList<Appointment> filteredList = new ArrayList<>();
                Date today = new GregorianCalendar(year, month, dayOfMonth).getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(today);
                for (Appointment as:user.getAppointments()) {
                    if(as.getDate().equals(formattedDate)){
                        filteredList.add(as);
                    }

                }
                mAdapter.replaceList(filteredList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
    }



    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(CalendarActivity.this, R.style.AlertDialog)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_notification_clear_all)
                .show();

    }


}
