package com.vbermudez.floatingwidgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.vbermudez.floatingwidgets.data.Word;
import com.vbermudez.floatingwidgets.tasks.LoaderTask;
import com.vbermudez.floatingwidgets.tasks.ShowWordsActivityTask;
import com.vbermudez.floatingwidgets.views.WordCardView;


public class MainActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasOverlayPermission()) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView();
        }
    }

    public boolean hasOverlayPermission() {
        return Settings.canDrawOverlays(this);
    }

    /**
     * Set and initialize the view elements.
     */
    private void initializeView() {
        SearchView sv = (SearchView) findViewById(R.id.search_word);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showTermDefinitions(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

        startService(new Intent(MainActivity.this, FloatingWidgetService.class));
    }

    public void showTermDefinitions(final String term) {
        ProgressBar pb = (ProgressBar) findViewById(R.id.word_cards_loading);
        pb.setIndeterminate(true);
        LoaderTask lt = new LoaderTask(pb, true);
        lt.run();

        LoaderTask ltDisable = new LoaderTask(pb, false);
        ShowWordsActivityTask task = new ShowWordsActivityTask(term, ltDisable, MainActivity.this, getResources(), getApplicationContext());
        task.execute();
    }

    public void displayWord(Word word) {
        WordCardView wcv = new WordCardView(getApplicationContext(), word.getWord(), word.getFormattedDefinitions());
        LinearLayout lv = (LinearLayout ) findViewById(R.id.definition_list);
        lv.addView(wcv);
    }

    public void displayWordNotFound(String term) {
        WordCardView wcv = new WordCardView(getApplicationContext(), "Word not found", String.format("The term %s was not found.", term));
        LinearLayout  lv = (LinearLayout ) findViewById(R.id.definition_list);
        lv.addView(wcv);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (hasOverlayPermission()) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application.",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void clearDefinitions() {
        LinearLayout lv = findViewById(R.id.definition_list);
        lv.removeAllViews();
    }
}