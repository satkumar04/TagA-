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

import org.san.tgps.HistoryVehicleTrackMain;
import org.san.tgps.R;
import org.san.tgps.VehicleTrackMain;
import org.san.tgps.bean.VehiclesBean;
import org.san.tgps.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllVehiclesHistoryAdapter extends RecyclerView.Adapter<AllVehiclesHistoryAdapter.AllVehiclesViewHolder> {

    Context mContext;
    private List<VehiclesBean> allVehiclesList = new ArrayList<>();
    private String query;
    private Spannable spannable;
    private ColorStateList colorstate;
    private TextAppearanceSpan highlightSpan;


    public AllVehiclesHistoryAdapter(Context mContext, List<VehiclesBean> allVehiclesList, String query) {
        this.mContext = mContext;
        // this.rv_allvehicles = rv_allvehicles;
        this.allVehiclesList = allVehiclesList;
        this.query = query;

        //((SimpleItemAnimator) RecyclerView.getItemAnimator()).setSupportsChangeAnimations(‌​false);
    }

    @Override
    public AllVehiclesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itmView = LayoutInflater.from(parent.getContext()).inflate(org.san.tgps.R.layout.allvehiclestxt, parent, false);
        return new AllVehiclesViewHolder(itmView);
    }

    @Override
    public void onBindViewHolder(AllVehiclesViewHolder holder, int position) {
        VehiclesBean vhclbn = allVehiclesList.get(position);

        //holder.txtvehiclename.setText(vhclbn.getDevice_Name());
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

        if (vhclbn.getDevice_Status().equals("0")) {
            holder.imgvehiclests.setBackgroundResource(R.drawable.offlinedrive);
        } else if (vhclbn.getDevice_Status().equals("1")) {
            holder.imgvehiclests.setBackgroundResource(R.drawable.greendrive);
        } else if (vhclbn.getDevice_Status().equals("2")) {
            holder.imgvehiclests.setBackgroundResource(R.drawable.greydrive);
        }
    }

    @Override
    public int getItemCount() {
        return allVehiclesList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final class AllVehiclesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtvehiclename, txtregno;
        // CardView cv_itms;
        LinearLayout lv_allvehicles;
        ImageView imgvehiclests;

        public AllVehiclesViewHolder(View itemView) {
            super(itemView);
            txtvehiclename = (TextView) itemView.findViewById(org.san.tgps.R.id.txtvehiclename);
            txtregno = (TextView) itemView.findViewById(org.san.tgps.R.id.txtregno);
            lv_allvehicles = (LinearLayout) itemView.findViewById(org.san.tgps.R.id.lv_allvehicles);
            imgvehiclests = (ImageView) itemView.findViewById(org.san.tgps.R.id.imgvehiclests);

            lv_allvehicles.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case org.san.tgps.R.id.lv_allvehicles:
                    getclickitem(getAdapterPosition());
                    break;
            }
        }

        private void getclickitem(final int pos) {

            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("You have selected :\n" + allVehiclesList.get(pos).getDevice_Name());
            alertDialog.setMessage(allVehiclesList.get(pos).getRegNumber());
            alertDialog.setButton("Continue ?", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    try {
                                /*if(!DALServerData.checkhttpConnection(GlobalVariables.IPAddress))
                                {
									Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
								}
								else
								{*/
                        GlobalVariables.DeviceId = allVehiclesList.get(pos).getDeviceId();
                        GlobalVariables.imei=allVehiclesList.get(pos).getDevice_Imei();
                        Intent i1 = new Intent(mContext, VehicleTrackMain.class);
                        i1.putExtra("DeviceId", allVehiclesList.get(pos).getDeviceId());
                        i1.putExtra("stat", allVehiclesList.get(pos).getDevice_Status());
                        i1.putExtra("vehicle", allVehiclesList.get(pos).getDevice_Name());
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
