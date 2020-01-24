package edu.milios.Barberoo.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import edu.milios.Barberoo.R;
import edu.milios.Barberoo.data.model.Appointment;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Appointment> appointments;
    private Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView timetxt;
        TextView typetxt;
        TextView usernametxt;
        ConstraintLayout app;

        ViewHolder(View itemView) {
            super(itemView);
            app = itemView.findViewById(R.id.appointment);
            timetxt = itemView.findViewById(R.id.time);
            typetxt = itemView.findViewById(R.id.type);
            usernametxt = itemView.findViewById(R.id.usernametext);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdapter(ArrayList<Appointment> myDataset, Context mContext) {
        appointments = myDataset;
        this.mContext = mContext;
    }

    public void replaceList(ArrayList<Appointment> appointments) {
        this.appointments.clear();
        this.appointments.addAll(appointments);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointments, parent ,false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.typetxt.setText(appointments.get(position).getType().substring(0,1).toUpperCase() + appointments.get(position).getType().substring(1));
        holder.timetxt.setText(appointments.get(position).getTime());

        if(appointments.get(position).getState()){
            holder.usernametxt.setText("Approved");
        }
        else {
            holder.usernametxt.setText("Pending");

        }

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return appointments.size();
    }
}