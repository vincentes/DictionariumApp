package com.vbermudez.floatingwidgets.utils;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class InternalStorage {

    public static void processSavedReports(final Context context) {
        File dir = new File(context.getFilesDir() + "/");
        if(!dir.isDirectory()) {
            throw new IllegalStateException("Not a directory.");
        }
        for(File file : Objects.requireNonNull(dir.listFiles())) {
            final String fileName = file.getName();
            if(fileName.startsWith("bug_report_")) {
                String jsonStr = InternalStorage.readFile(context, fileName);
                JSONObject json = null;
                try {
                    json = new JSONObject(jsonStr);
                    // Only works on success
                    EasyRequest.quickPost(EasyRequest.url("error"), json, context, new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(context.getFilesDir() + "/" + fileName);
                            file.delete();
                            Toast.makeText(context,fileName + " processed",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveBugReport(Context context, String data) {
        String fileName = "bug_report_";
        int counter = 1;
        String fileNameEnd = fileName + counter;
        File file = new File(context.getFilesDir() + "/" + fileNameEnd);
        while(file.exists()) {
            counter++;
            fileNameEnd = fileName + counter;
            file = new File(context.getFilesDir() + "/" + fileNameEnd);
        }

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(fileNameEnd, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();

            Toast.makeText(context,fileNameEnd + " saved",
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}
