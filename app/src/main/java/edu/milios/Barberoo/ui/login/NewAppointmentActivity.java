package edu.milios.Barberoo.ui.login;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.channels.Channel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import edu.milios.Barberoo.R;
import edu.milios.Barberoo.data.model.Appointment;
import edu.milios.Barberoo.data.model.User;

public class NewAppointmentActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    final Calendar myCalendar = Calendar.getInstance();

    EditText edittext;
    EditText timeedit;
    ArrayList<Appointment> appointmentsList = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<User> barbers = new ArrayList<>();
    User user = new User();
    Appointment appointmentClicked;

    Spinner barber;
    Spinner typespinner;


    ConstraintLayout adminV;
    CheckBox approveBox;

    DatabaseReference myRef;
    DatabaseReference usersRef;
    DatabaseReference appointmentsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        adminV = findViewById(R.id.adminview);
        approveBox = findViewById(R.id.approve);


        appointmentsList = getIntent().getParcelableArrayListExtra("appointments");
        userList = getIntent().getParcelableArrayListExtra("users");
        user = getIntent().getExtras().getParcelable("user");
        appointmentClicked = getIntent().getExtras().getParcelable("appointmentClicked");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        usersRef = database.getReference().child("users");
        appointmentsRef = database.getReference().child("appointments");

        for (User us :
                userList) {
            if (us.isBarber()){
                barbers.add(us);
            }

        }

        edittext= (EditText) findViewById(R.id.datetime);
        timeedit = findViewById(R.id.time);
        Toolbar toolbar = findViewById(R.id.toolbarnew);
        toolbar.setTitle("New appointment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miprofile:
                        saveNewAppointment();

                }
                return true;
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewAppointmentActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))

                        .show();
            }
        });

        timeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(NewAppointmentActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        timeedit.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        barber = findViewById(R.id.spinner);
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, barbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        barber.setAdapter(adapter);

        ArrayList<String> types= new ArrayList<>();
        types.add("Beard");
        types.add("Hair");
        typespinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typespinner.setAdapter(adapter1);



        if (appointmentClicked!= null){
            int position1=0;
            int position2=0;
            for (User us :
                    barbers) {
                if (appointmentClicked.getBarberId().equals(us.getUid())){
                    position1 = barbers.indexOf(us);
                }
            }
            for (String st :
                    types) {
                if (appointmentClicked.getType().equals(st)){
                    position2 = types.indexOf(st);
                }
            }

            barber.setSelection(position1);
            typespinner.setSelection(position2);

            edittext.setText(appointmentClicked.getDate());
            timeedit.setText(appointmentClicked.getTime());
            if (user.isBarber()){
                adminV.setVisibility(View.VISIBLE);
                approveBox.setChecked(appointmentClicked.getState());
            }
        }



    }

    private void saveNewAppointment() {
        User selectedBarber = (User) barber.getSelectedItem();
        String selectedType = (String) typespinner.getSelectedItem();
        if (appointmentClicked!=null){
            Appointment a = new Appointment(appointmentClicked.getUuid(), appointmentClicked.getUserId(), edittext.getText().toString(), timeedit.getText().toString(),
                    selectedBarber.getUid(), selectedType,approveBox.isChecked() );

            int position = 0;
            for (Appointment ap :
                    appointmentsList) {
                if (ap.getUuid().equals(appointmentClicked.getUuid())){
                    position = appointmentsList.indexOf(ap);
                }
            }
            appointmentsList.remove(position);
            appointmentsList.add(a);

        }
        else{
            Appointment a = new Appointment(null, user.getUid(), edittext.getText().toString(), timeedit.getText().toString(),
                    selectedBarber.getUid(), selectedType,false );
            appointmentsList.add(a);
        }


        appointmentsRef.setValue(appointmentsList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                System.out.println("Value was set. Error = "+error);
                scheduleNotification(getNotification("Your appointment is about to start"));
                onBackPressed();

            }
        } );

    }
    private void scheduleNotification (Notification notification) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher.class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent.FLAG_UPDATE_CURRENT ) ;



        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, myCalendar.getTimeInMillis(), pendingIntent);
        }
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Barberoo!" ) ;

        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.ic_stat_roo ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }


}
