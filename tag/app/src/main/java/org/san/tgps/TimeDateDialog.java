package org.san.tgps;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import org.san.tgps.utils.GlobalVariables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SANTECH on 23/03/2016.
 */
public class TimeDateDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    String todatetime,todatetime1;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        populateSetTime(hourOfDay, minute);
    }

    private void populateSetTime(int hourOfDay, int minute) {
       // Toast.makeText(getActivity(), "Time is "+hourOfDay +":"+minute, Toast.LENGTH_SHORT).show();

        GlobalVariables.customtime = hourOfDay +":"+minute+":00";
        GlobalVariables.cstmFrmdateTime = GlobalVariables.customdate +":" + hourOfDay + ":" + minute +":00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm");

        try {
            Date date = simpleDateFormat.parse(GlobalVariables.customdate +":" + hourOfDay + ":" + minute +":00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 2);
            Date date1 = calendar.getTime();
            todatetime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date1);
            todatetime1 = (new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss")).format(date1);
            //edtToTime.setText(todatetime);
        GlobalVariables.cstmTodateTime = todatetime1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
