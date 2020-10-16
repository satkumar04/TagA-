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
 * Created by SANTECH on 2017-12-21.
 */

public class SpeedVehiclesAdapter extends RecyclerView.Adapter<SpeedVehiclesAdapter.SpeedVehiclesViewHolder> {

    private Context mContext;
    private List<VehiclesBean> speedVehiclesList = new ArrayList<>();
    private String query;
    private Spannable spannable;
    private ColorStateList colorstate;
    private TextAppearanceSpan highlightSpan;

    public SpeedVehiclesAdapter(Context mContext, List<VehiclesBean> onlineVehiclesList, String query) {
        this.mContext = mContext;
        this.speedVehiclesList = onlineVehiclesList;
        this.query = query;
    }

    @Override
    public SpeedVehiclesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itmView = LayoutInflater.from(parent.getContext()).inflate(R.layout.speedvehiclestxt, parent, false);
        return new SpeedVehiclesViewHolder(itmView);
    }

    @Override
    public void onBindViewHolder(SpeedVehiclesViewHolder holder, int position) {
        VehiclesBean vhclbn = speedVehiclesList.get(position);

      //  holder.txtvehiclename.setText(vhclbn.getSpeedv_Name());
        holder.txtregno.setText(vhclbn.getSpeedv_regno());

        int startPos = vhclbn.getSpeedv_Name().toLowerCase(Locale.getDefault()).indexOf(query.toLowerCase(Locale.getDefault()));
        int endPos = startPos + query.length();

        if (query.equals("")) {
            holder.txtvehiclename.setText(vhclbn.getSpeedv_Name());
            holder.txtdatetime.setText(vhclbn.getSpeedv_time());

        } else {
            spannable = new SpannableString(vhclbn.getSpeedv_Name());
            colorstate = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.RED});
            highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, colorstate, null);

            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.txtvehiclename.setText(spannable);
            holder.txtdatetime.setText(spannable);
        }

        if (vhclbn.getDevice_Status().equals("1"))
        {
            holder.imgvehiclests.setBackgroundResource(R.drawable.speedometer);
        } else if (vhclbn.getDevice_Status().equals("2")) {
            holder.imgvehiclests.setBackgroundResource(R.drawable.speedometer);
        }

    }

    @Override
    public int getItemCount() {
        return speedVehiclesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //swap

    public final class SpeedVehiclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtvehiclename,txtdatetime, txtregno;
        LinearLayout lv_allvehicles;
        ImageView imgvehiclests;

        public SpeedVehiclesViewHolder(View itemView) {
            super(itemView);
            txtvehiclename = (TextView) itemView.findViewById(R.id.txtvehiclename);
            txtdatetime = (TextView) itemView.findViewById(R.id.txtdatetime);
            txtregno = (TextView) itemView.findViewById(R.id.txtregno);
            imgvehiclests = (ImageView) itemView.findViewById(R.id.imgvehiclests);
            lv_allvehicles = (LinearLayout) itemView.findViewById(R.id.lv_allvehicles);
            lv_allvehicles.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lv_allvehicles:
                  //  getclickitem(getAdapterPosition());
                    break;
            }
        }

        private void getclickitem(final int pos) {

            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("You have selected :\n" + speedVehiclesList.get(pos).getSpeedv_Name());
            alertDialog.setMessage(speedVehiclesList.get(pos).getSpeedv_regno());
            alertDialog.setButton("Continue ?", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    try {

                        GlobalVariables.imei = speedVehiclesList.get(pos).getSpeedv_Imei();

                        Intent i1 = new Intent(mContext, VehicleTrackMain.class);
                        i1.putExtra("imei", speedVehiclesList.get(pos).getSpeedv_Imei());
                        i1.putExtra("stat", speedVehiclesList.get(pos).getDevice_Status());
                        i1.putExtra("vehicle", speedVehiclesList.get(pos).getSpeedv_Name());
                        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(i1);
                        //}
                    } catch (Exception e) {

                    }

                }
            });

            alertDialog.show();
        }
    }
}
