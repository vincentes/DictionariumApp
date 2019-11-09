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

import java.util.List;

public class ShowWordsServiceTask extends AsyncTask<String, Void, Integer> {

    private String text;
    private Resources resources;
    private Context context;
    private FloatingWidgetService fws;
    private Runnable post;

    public ShowWordsServiceTask(String text, Runnable post, FloatingWidgetService fws, Resources resources, Context context) {
        assert text != null;
        assert resources != null;
        assert context != null;
        this.fws = fws;
        this.post = post;
        this.resources = resources;
        this.context = context;
        this.text = text;
    }


    @Override
    protected Integer doInBackground(String... strings) {
        WordBrain wordBrain = new WordBrain(resources, context);
        try {
            wordBrain.loadSearchTerm(text);
        } catch (InitialNotSupportedException e) {
            e.printStackTrace();
        }
        final List<Word> words = wordBrain.searchDefinition(text);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(words.isEmpty()) {
                    fws.displayWordNotFound(text);
                    Toast.makeText(context, "Term not found.", Toast.LENGTH_LONG).show();
                } else {
                    fws.expandWithWords(words);
                    Toast.makeText(context,"Copy: \n" + words.get(0).getWord(), Toast.LENGTH_LONG).show();
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
