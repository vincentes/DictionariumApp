package com.vbermudez.floatingwidgets.utils;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.vbermudez.floatingwidgets.exceptions.ExceptionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;

public class ErrorReporter {

    public static String getReportJsonString(Exception exception) {
        final JSONObject data = new JSONObject();
        try {
            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));

            String utcTime = "";
            try {
                utcTime = TimeUtils.getCurrentUtcTime().toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            data.put("title", exception.getClass().getCanonicalName());
            data.put("date",  utcTime);
            data.put("stackTrace", sw.toString());
            data.put("brand", Build.BRAND);
            data.put("device", Build.DEVICE);
            data.put("model", Build.MODEL);
            data.put("id", Build.ID);
            data.put("product", Build.PRODUCT);
            data.put("sdk", Build.VERSION.SDK);
            data.put("release", Build.VERSION.RELEASE);
            data.put("incremental", Build.VERSION.INCREMENTAL);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public static String getReportJsonString(Throwable exception) {
        final JSONObject data = new JSONObject();
        try {
            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));

            String utcTime = "";
            try {
                utcTime = TimeUtils.getCurrentUtcTime().toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            data.put("title", exception.getClass().getCanonicalName());
            data.put("date",  utcTime);
            data.put("stackTrace", sw.toString());
            data.put("brand", Build.BRAND);
            data.put("device", Build.DEVICE);
            data.put("model", Build.MODEL);
            data.put("id", Build.ID);
            data.put("product", Build.PRODUCT);
            data.put("sdk", Build.VERSION.SDK);
            data.put("release", Build.VERSION.RELEASE);
            data.put("incremental", Build.VERSION.INCREMENTAL);
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    public static void report(JSONObject json, Context context) {
        if(EasyRequest.isNetworkAvailable(context)) {
            EasyRequest.quickPost(EasyRequest.url("error"), json, context);
            Toast.makeText(context, "Network available", Toast.LENGTH_SHORT).show();

        } else {
            InternalStorage.saveBugReport(context, json.toString());
            Toast.makeText(context, "Network unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public static void report(Exception e, Context context) {
        JSONObject json = null;
        try {
            json = new JSONObject(ErrorReporter.getReportJsonString(e));
            if(EasyRequest.isNetworkAvailable(context)) {
                EasyRequest.quickPost(EasyRequest.url("error"), json, context);
            } else {
                InternalStorage.saveBugReport(context, json.toString());
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
