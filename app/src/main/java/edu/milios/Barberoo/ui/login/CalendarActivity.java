package edu.milios.Barberoo.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import edu.milios.Barberoo.R;
import edu.milios.Barberoo.data.model.Appointment;
import edu.milios.Barberoo.data.model.User;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class CalendarActivity extends AppCompatActivity {
    private static final String TAG = "CalendarActivity";

    private RecyclerView recyclerView;
    private EventAdapter mAdapter;
    private User user;
    private CalendarView simpleCalendarView ;
    private ArrayList<Appointment> appointmentsList = new ArrayList();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DatabaseReference myRef;
    private DatabaseReference usersRef;
    private DatabaseReference appointmentsRef;
    private ArrayList<User> users;
    private ArrayList<Appointment> filteredList2;


    ValueEventListener UserListener;
    Date today = new GregorianCalendar().getTime();

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        simpleCalendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.events);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.eventsview);
        Toolbar toolbar = findViewById(R.id.toolbarnew);
        toolbar.setTitle("My Calendar");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        mAuth = getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        int index = mAuth.getCurrentUser().getEmail().indexOf('@');
        int length = mAuth.getCurrentUser().getEmail().length();
        user = new User(currentUser.getUid(),currentUser.getEmail().substring(0,index),
                currentUser.getEmail().substring(index+1,length),currentUser.getEmail(),
                true,"68veprDQ4AStOpH2VysyKROxZBg2" );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        usersRef = database.getReference().child("users");
        appointmentsRef = database.getReference().child("appointments");
        setListeners();


//        ArrayList list =  new ArrayList<User>();
//        list.add(user);
//        usersRef.setValue(list);
//        Appointment appointment = new Appointment(null, user.getUid(), "26/1/2020", "22:00", "68veprDQ4AStOpH2VysyKROxZBg2", "beard", true);
//
//        appointmentsList.add(appointment);
//        appointmentsRef.setValue(appointmentsList);

        mSwipeRefreshLayout.setRefreshing(true);
        myRef.addListenerForSingleValueEvent(UserListener);
        appointmentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Appointment>> s = new GenericTypeIndicator<ArrayList<Appointment>>() {};
                ArrayList<Appointment> appointments = dataSnapshot.getValue(s);
                ArrayList<Appointment> filteredList = new ArrayList<>();
                if (appointments!= null){
                    for (Appointment ap :
                            appointments) {
                        if (ap.getUserId().equals(user.getUid()) || (user.isBarber()&&ap.getBarberId().equals(user.getUid()))){
                            filteredList.add(ap);
                        }
                    }
                }
                appointmentsList= filteredList;
                filteredList2 = new ArrayList<>();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(today);
                for (Appointment as:appointmentsList) {
                    if(as.getDate().equals(formattedDate)){
                        filteredList2.add(as);
                    }

                }

                mAdapter.replaceList(filteredList2);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new EventAdapter((ArrayList<Appointment>) appointmentsList, CalendarActivity.this, users);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(CalendarActivity.this, NewAppointmentActivity.class);
                i.putParcelableArrayListExtra("appointments",  appointmentsList);
                i.putExtra("user", user);
                i.putParcelableArrayListExtra("users", users);
                i.putExtra("appointmentClicked", filteredList2.get(position));
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),1);
//        recyclerView.addItemDecoration(dividerItemDecoration);



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myRef.addListenerForSingleValueEvent(UserListener);

            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primaryColor,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNextActivity = new Intent(CalendarActivity.this, NewAppointmentActivity.class);
                goToNextActivity.putParcelableArrayListExtra("appointments",  appointmentsList);
                goToNextActivity.putExtra("user", user);
                goToNextActivity.putParcelableArrayListExtra("users", users);
                startActivity(goToNextActivity);




            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        myRef.addListenerForSingleValueEvent(UserListener);
    }

    private void setListeners() {
        UserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                GenericTypeIndicator<ArrayList<User>> t = new GenericTypeIndicator<ArrayList<User>>() {};
                users =(ArrayList<User>) dataSnapshot.child("users").getValue(t);
                mAdapter.setUsers(users);

                for (User usera :
                        users) {
                    if(usera.getUid().equals(user.getUid())){
                        user = usera;
                    }
                }
                GenericTypeIndicator<ArrayList<Appointment>> s = new GenericTypeIndicator<ArrayList<Appointment>>() {};
                ArrayList<Appointment> appointments =(ArrayList<Appointment>) dataSnapshot.child("appointments").getValue(s);
                ArrayList<Appointment> filteredList = new ArrayList<>();
                if(appointments!=null){
                    for (Appointment ap :
                            appointments) {
                        if (ap.getUserId().equals(user.getUid()) || (user.isBarber()&&ap.getBarberId().equals(user.getUid()))){
                            filteredList.add(ap);
                        }
                    }
                }


                appointmentsList= filteredList;
                filteredList2 = new ArrayList<>();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(today);
                for (Appointment as:appointmentsList) {
                    if(as.getDate().equals(formattedDate)){
                        filteredList2.add(as);
                    }

                }

                mAdapter.replaceList(filteredList2);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mSwipeRefreshLayout.setRefreshing(true);
                filteredList2 = new ArrayList<>();
                today = new GregorianCalendar(year, month, dayOfMonth).getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(today);
                for (Appointment as:appointmentsList) {
                    if(as.getDate().equals(formattedDate)){
                        filteredList2.add(as);
                    }

                }
                mAdapter.replaceList(filteredList2);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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
                .setIcon(R.drawable.ic_account_circle_24px)
                .show();

    }


}
