package com.vbermudez.floatingwidgets;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vbermudez.floatingwidgets.data.Word;
import com.vbermudez.floatingwidgets.tasks.LoaderTask;
import com.vbermudez.floatingwidgets.tasks.ShowWordsServiceTask;

import java.util.List;

public class FloatingWidgetService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingView;

    public FloatingWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                ProgressBar pb = mFloatingView.findViewById(R.id.loading_word);
                LoaderTask lt = new LoaderTask(pb, true);
                lt.run();
                LoaderTask ltDisable = new LoaderTask(pb, false);
                final ShowWordsServiceTask task = new ShowWordsServiceTask(clipboard.getText().toString(), ltDisable, FloatingWidgetService.this, getResources(), getApplicationContext());
                task.execute();
            }
        });

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;     //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        // The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        // The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);


        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;


                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                // collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            } else {
                                expandedView.setVisibility(View.GONE);
                                final ImageView icon = collapsedView.findViewById(R.id.collapsed_iv);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);


                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.expanded_container).getVisibility() == View.GONE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    public WindowManager getmWindowManager() {
        return mWindowManager;
    }

    public void setmWindowManager(WindowManager mWindowManager) {
        this.mWindowManager = mWindowManager;
    }

    public View getmFloatingView() {
        return mFloatingView;
    }

    public void setmFloatingView(View mFloatingView) {
        this.mFloatingView = mFloatingView;
    }

    public void expandWithWords(List<Word> word) {
        View view = getmFloatingView();
        View expandedView = view.findViewById(R.id.expanded_container);
        expandedView.setVisibility(View.VISIBLE);
        TextView title = view.findViewById(R.id.wordTitle);
        title.setText(word.get(0).getWord());
        TextView definitions = view.findViewById(R.id.wordDefinitions);
        String def = "";
        int counter = 1;
        for(String d : word.get(0).getDefinitions()) {
            // Don't append a new line to the last definition. Avoids unnecessary space.
            if(counter == word.get(0).getDefinitions().length) {
                def += counter + ". " + d;
            } else {
                def += counter + ". " + d + "\n\n";
            }
            counter++;
        }
        definitions.setText(def);
    }

    public void displayWordNotFound(String term) {
        View view = getmFloatingView();
        View expandedView = view.findViewById(R.id.expanded_container);
        expandedView.setVisibility(View.VISIBLE);
        TextView title = view.findViewById(R.id.wordTitle);
        TextView definitions = view.findViewById(R.id.wordDefinitions);
        title.setText("Term not found");
        definitions.setText("The term \"" + term + "\" was not found.");

    }
}
