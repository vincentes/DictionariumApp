package com.vbermudez.floatingwidgets.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.vbermudez.floatingwidgets.R;

public class WordCardView extends CardView {

    private String word, definition;

    public WordCardView(@NonNull Context context, String word, String definition) {
        super(context);
        this.word = word;
        this.definition = definition;
        init(context, null);
    }

    public WordCardView(@NonNull Context context, @Nullable AttributeSet attrs, String word, String definition) {
        super(context, attrs);
        init(context, attrs);
    }

    public WordCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, String word, String definition) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        // Not working
        // int marginTopAndBottom = getResources().getDimensionPixelSize(R.dimen.wordcard_margin_top_bottom);
        // int marginStartEnd = getResources().getDimensionPixelSize(R.dimen.wordcard_margin_start_end);
        // params.setMargins(marginStartEnd, marginTopAndBottom, marginStartEnd, marginTopAndBottom);

        setLayoutParams(params);
        setRadius(9);
        setContentPadding(15, 15, 15, 15);
        setMaxCardElevation(15);
        setCardElevation(9);
        int radius_in_dp = getResources().getDimensionPixelSize(R.dimen.wordcard_radius);
        setRadius(radius_in_dp);
        int padding_in_dp = getResources().getDimensionPixelSize(R.dimen.wordcard_padding);
        setContentPadding(padding_in_dp, padding_in_dp, padding_in_dp, padding_in_dp);
        setUseCompatPadding(true);

        LinearLayout ly = new LinearLayout(context);
        ly.setLayoutParams(params);
        ly.setOrientation(LinearLayout.VERTICAL);

        TextView tvWord = new TextView(context);
        tvWord.setLayoutParams(params);
        tvWord.setText(word);
        tvWord.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        LayoutParams tvDefinitionParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        int marginTop = getResources().getDimensionPixelSize(R.dimen.wordcard_definition_margin_top);
        params.setMargins(0, marginTop, 0, 0);

        TextView tvDefinition = new TextView(context);
        tvDefinition.setText(definition);
        tvDefinition.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tvDefinition.setLayoutParams(tvDefinitionParams);

        ly.addView(tvWord);
        ly.addView(tvDefinition);
        addView(ly);
    }
}
