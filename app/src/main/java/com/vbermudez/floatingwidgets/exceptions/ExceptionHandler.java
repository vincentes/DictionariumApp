package com.vbermudez.floatingwidgets.exceptions;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Switch;

import com.vbermudez.floatingwidgets.CrashActivity;
import com.vbermudez.floatingwidgets.R;
import com.vbermudez.floatingwidgets.utils.EasyRequest;
import com.vbermudez.floatingwidgets.utils.ErrorReporter;
import com.vbermudez.floatingwidgets.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Intent intent = null;
        SharedPreferences pref = myContext.getSharedPreferences("SettingsPref", MODE_PRIVATE);
        boolean shouldReport = pref.getBoolean("send_reports", true);
        if(shouldReport) {
            String dataJson = ErrorReporter.getReportJsonString(exception);
            intent = new Intent(myContext, CrashActivity.class);
            intent.putExtra("data", dataJson);
        } else {
            intent = new Intent(myContext, CrashActivity.class);
        }

        myContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}