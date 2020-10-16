package org.san.tgps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CompanyInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_information);
    }
    public void onBackPressed() {
        Intent in = new Intent(CompanyInformation.this, Navigation.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
    }
}
