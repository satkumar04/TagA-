package org.san.tgps.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.san.tgps.R;
import org.san.tgps.VehicleTrackMain;
import org.san.tgps.bean.AlarmsBean;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SANTECH on 03/30/2018.
 */

public class PowerCutAlarmAdapter extends RecyclerView.Adapter<PowerCutAlarmAdapter.PowerCutAlarmViewHolder> {

    private Context mContext;
    private List<AlarmsBean> powercutalarmList = new ArrayList<>();
    private String query;
    private Spannable spannable;
    private ColorStateList colorstate;
    private TextAppearanceSpan highlightSpan;

    public PowerCutAlarmAdapter(Context mContext, List<AlarmsBean> powercutalarmList, String query) {
        this.mContext = mContext;
        this.powercutalarmList = powercutalarmList;
        this.query = query;
    }

    @Override
    public PowerCutAlarmAdapter.PowerCutAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allalarmstxt, parent, false);
        return new PowerCutAlarmAdapter.PowerCutAlarmViewHolder(itmView);
    }

    @Override
    public void onBindViewHolder(PowerCutAlarmAdapter.PowerCutAlarmViewHolder holder, int position) {
        AlarmsBean vhclbn = powercutalarmList.get(position);

        holder.txt_vehiclename.setText(vhclbn.getRegnumber());
        holder.txt_date.setText(vhclbn.getAlarm_date());
        // holder.txt_time.setText(vhclbn.getAlarm_time());
        holder.txt_address.setText(vhclbn.getalarmaddress());


        int startPos = vhclbn.getDevice_name().toLowerCase(Locale.getDefault()).indexOf(query.toLowerCase(Locale.getDefault()));
        int endPos = startPos + query.length();

        if (query.equals("")) {
            holder.txt_vehiclename.setText(vhclbn.getRegnumber());
        } else {
            spannable = new SpannableString(vhclbn.getAlarm_time());
            colorstate = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.RED});
            highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorstate, null);

            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //holder.txtvehiclename.setText(spannable);
        }

    }

    @Override
    public int getItemCount() {
        return powercutalarmList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //swap

    public final class PowerCutAlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_date, txt_vehiclename,txt_address;
        LinearLayout lv_allalarms;
        // ImageView imgvehiclests;

        public PowerCutAlarmViewHolder(View itemView) {
            super(itemView);
            //txt_alarmname = (TextView) itemView.findViewById(R.id.txt_alarmname);
            txt_date = (TextView) itemView.findViewById(R.id.txtdate);
            txt_vehiclename = (TextView) itemView.findViewById(R.id.txt_vehicleregnum);
            txt_address = (TextView) itemView.findViewById(R.id.txtcuraddress);
            // imgvehiclests = (ImageView) itemView.findViewById(R.id.imgvehiclests);
            lv_allalarms = (LinearLayout) itemView.findViewById(R.id.lv_allalarms);
            lv_allalarms.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lv_allvehicles:
                //    getclickitem(getAdapterPosition());
                    break;
            }
        }


    }
}
