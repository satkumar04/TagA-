package org.san.tgps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import org.san.tgps.utils.GlobalVariables;

import java.util.Calendar;

/**
 * Created by SANTECH on 22/03/2016.
 */
public class DateTimeDailog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
   /* @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.dateselection, null);
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
      //  dob.setText(month+"/"+day+"/"+year);
       // Toast.makeText(getActivity(), "" +month+"/"+day+"/"+year , Toast.LENGTH_SHORT).show();

        String month1 = String.format("%02d", month);
        GlobalVariables.customdate = year +"-"+month1+"-"+day;

        DialogFragment timefrgmnt = new TimeDateDialog();
        timefrgmnt.show(getFragmentManager(), "TimePicker");
    }

    /* EditText etdate;

    public DateTimeDailog() {
    }

    public DateTimeDailog(View view) {
        etdate = (EditText) view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }*/
}
