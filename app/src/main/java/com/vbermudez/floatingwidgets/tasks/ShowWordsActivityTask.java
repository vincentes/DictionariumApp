package com.vbermudez.floatingwidgets.tasks;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.vbermudez.floatingwidgets.FloatingWidgetService;
import com.vbermudez.floatingwidgets.MainActivity;
import com.vbermudez.floatingwidgets.data.Word;
import com.vbermudez.floatingwidgets.exceptions.InitialNotSupportedException;
import com.vbermudez.floatingwidgets.service.WordBrain;
import com.vbermudez.floatingwidgets.utils.ErrorReporter;

import java.util.List;

public class ShowWordsActivityTask extends AsyncTask<String, Void, Integer> {

    private String text;
    private Resources resources;
    private Context context;
    private MainActivity activity;
    private Runnable post;

    public ShowWordsActivityTask(String text, Runnable post, MainActivity activity, Resources resources, Context context) {
        assert text != null;
        assert resources != null;
        assert context != null;
        this.activity = activity;
        this.post = post;
        this.resources = resources;
        this.context = context;
        this.text = text;
    }


    @Override
    protected Integer doInBackground(String... strings) {
        WordBrain wordBrain = new WordBrain(resources, context);
        final List<Word> words;
        try {
            words = wordBrain.loadSearchTerm(text);
        } catch (InitialNotSupportedException e) {
            ErrorReporter.report(e, context);
            e.printStackTrace();
            return -1;
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                activity.clearDefinitions();
                if(words.isEmpty()) {
                    activity.displayWordNotFound(text);
                } else {
                    for(Word word : words) {
                        activity.displayWord(word);
                    }
                }
            }
        });

        if(post != null) {
            handler.post(post);
        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(post != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(post);
        }
        super.onPostExecute(integer);
    }
}
