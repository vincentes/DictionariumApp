package com.vbermudez.floatingwidgets;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vbermudez.floatingwidgets.utils.EasyRequest;
import com.vbermudez.floatingwidgets.utils.ErrorReporter;
import com.vbermudez.floatingwidgets.utils.InternalStorage;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Objects;


public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("data")) {
            try {
                Toast.makeText(getApplicationContext(), "Sending json", Toast.LENGTH_SHORT).show();
                JSONObject json = new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("data")));
                ErrorReporter.report(json, getApplicationContext());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            TextView tvMessages = (TextView) findViewById(R.id.message);
            tvMessages.setText(R.string.crash_message_no_report);
        }

        setContentView(R.layout.activity_crash);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.search_actionbar_layout, null);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(v, new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            ));
        }
    }

    public void restartApp(View v) {
        finish();
    }
}
