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
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SANTECH on 2017-12-16.
 */

public class RunningVehiclesAdapter extends RecyclerView.Adapter<RunningVehiclesAdapter.RunningVehiclesViewHolder> {

    private Context mContext;
    private List<VehiclesBean> onlineVehiclesList = new ArrayList<>();
    private String query;
    private Spannable spannable;
    private ColorStateList colorstate;
    private TextAppearanceSpan highlightSpan;

    public RunningVehiclesAdapter(Context mContext, List<VehiclesBean> onlineVehiclesList, String query) {
        this.mContext = mContext;
        this.onlineVehiclesList = onlineVehiclesList;
        this.query = query;
    }

    @Override
    public RunningVehiclesAdapter.RunningVehiclesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.allvehiclestxt, parent, false);
        return new RunningVehiclesAdapter.RunningVehiclesViewHolder(itmView);
    }

    @Override
    public void onBindViewHolder(RunningVehiclesAdapter.RunningVehiclesViewHolder holder, int position) {
        VehiclesBean vhclbn = onlineVehiclesList.get(position);

        // holder.txtvehiclename.setText(vhclbn.getDevice_Name());
        holder.txtregno.setText(vhclbn.getRegNumber());

        int startPos = vhclbn.getDevice_Name().toLowerCase(Locale.getDefault()).indexOf(query.toLowerCase(Locale.getDefault()));
        int endPos = startPos + query.length();

        if (query.equals("")) {
            holder.txtvehiclename.setText(vhclbn.getDevice_Name());
        } else {
            spannable = new SpannableString(vhclbn.getDevice_Name());
            colorstate = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.RED});
            highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorstate, null);

            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txtvehiclename.setText(spannable);
        }

        if (vhclbn.getDevice_Status().equals("1")) {
            holder.imgvehiclests.setBackgroundResource(R.drawable.greendrive);
        } else if (vhclbn.getDevice_Status().equals("2"))
        {
            holder.imgvehiclests.setBackgroundResource(R.drawable.greydrive);
        }

    }

    @Override
    public int getItemCount() {
        return onlineVehiclesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //swap

    public final class RunningVehiclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtvehiclename, txtregno;
        LinearLayout lv_allvehicles;
        ImageView imgvehiclests;

        public RunningVehiclesViewHolder(View itemView) {
            super(itemView);
            txtvehiclename = (TextView) itemView.findViewById(R.id.txtvehiclename);
            txtregno = (TextView) itemView.findViewById(R.id.txtregno);
            imgvehiclests = (ImageView) itemView.findViewById(R.id.imgvehiclests);
            lv_allvehicles = (LinearLayout) itemView.findViewById(R.id.lv_allvehicles);

            lv_allvehicles.setOnClickListener(this);



        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lv_allvehicles:
                    getclickitem(getAdapterPosition());
                    break;
            }
        }

        private void getclickitem(final int pos) {


                        GlobalVariables.DeviceId = onlineVehiclesList.get(pos).getDeviceId();
                        GlobalVariables.imei=onlineVehiclesList.get(pos).getDevice_Imei();

                        Intent i1 = new Intent(mContext, VehicleTrackMain.class);
                        i1.putExtra("DeviceId", onlineVehiclesList.get(pos).getDeviceId());
                        i1.putExtra("stat", onlineVehiclesList.get(pos).getDevice_Status());
                        i1.putExtra("vehicle", onlineVehiclesList.get(pos).getDevice_Name());
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(i1);
                        //}
                    }

                }
}
