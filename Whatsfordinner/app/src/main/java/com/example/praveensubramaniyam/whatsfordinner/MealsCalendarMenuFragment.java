package com.example.praveensubramaniyam.whatsfordinner;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MealsCalendarMenuFragment extends Fragment {
    
    int mCurCheckPosition = 0;
    TextView date;
       
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       System.out.println("view created");


        View view = inflater.inflate(R.layout.mealsscreencalendarmenu, container, false);
        TextView day =  (TextView) view.findViewById(R.id.day);
        date =  (TextView) view.findViewById(R.id.date);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        System.out.println("Day of the week: "+dayOfTheWeek);
        System.out.println("TextView: "+day);
        day.setText(dayOfTheWeek);
        sdf = new SimpleDateFormat("MM dd yyyy");
        String currentDateandTime = sdf.format(d);
        date.setText(currentDateandTime);
        return view;
    }

    public TextView getTextViewDate(){
        return date;
    }

    public String getTextViewDateString(){
        return date.getText().toString();
    }

    public void updateToPreviousDate(){
        //this textview should be bound in the fragment onCreate as a member variable
        System.out.println("change tetx");
        TextView dateTextView=(TextView) getView().findViewById(R.id.date);
        TextView dayTextView=(TextView) getView().findViewById(R.id.day);
        System.out.println("Date: "+dateTextView.getText().toString());
        String date = dateTextView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        }
        catch(Exception e)
        {
            System.out.println("Exception e"+ e);
        }
        c.add(Calendar.DATE, -1);  // number of days to add
        date = sdf.format(c.getTime());
        dateTextView.setText(date);
        String dayOfWeek = c.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault());
        dayTextView.setText(dayOfWeek);
    }

    public void updateToNextDate(){
        //this textview should be bound in the fragment onCreate as a member variable
        System.out.println("change tetx");
        TextView dateTextView=(TextView) getView().findViewById(R.id.date);
        TextView dayTextView=(TextView) getView().findViewById(R.id.day);
        System.out.println("Date: "+dateTextView.getText().toString());
        String date = dateTextView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        }
        catch(Exception e)
        {
            System.out.println("Exception e"+ e);
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        date = sdf.format(c.getTime());
        dateTextView.setText(date);
        String dayOfWeek = c.getDisplayName( Calendar.DAY_OF_WEEK ,Calendar.LONG, Locale.getDefault());
        dayTextView.setText(dayOfWeek);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
    

}