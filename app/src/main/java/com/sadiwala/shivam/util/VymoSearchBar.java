package com.sadiwala.shivam.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sadiwala.shivam.R;


public class VymoSearchBar extends RelativeLayout {
    private View mRootView;
    private TextWatcher mTextWatcher;
    private ProgressBar mProgressBar;
    private int mEditTextBackground;
    private int mEditTextColor;
    private int mEditTextHintColor;
    private EditText mSearchEditText;
    private ImageView mClearBtn;
    private ImageView mSearchBtn;
    private String mHintText;
    private String mText;
    private Drawable mClearButtonDrawable;
    private Drawable mSearchButtonDrawable;

    public VymoSearchBar(Context context) {
        super(context);
        init(context);
    }

    public VymoSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        initAttribute(context, attrs);
        mSearchEditText.setBackgroundColor(mEditTextBackground);
        mSearchEditText.setTextColor(mEditTextColor);
        mSearchEditText.setHintTextColor(mEditTextHintColor);
        mSearchEditText.setHint(mHintText);
        mSearchEditText.setText(mText);
        if (mClearButtonDrawable != null) {
            mClearBtn.setImageDrawable(mClearButtonDrawable);
        }

        if (mSearchButtonDrawable != null) {
            mSearchBtn.setImageDrawable(mSearchButtonDrawable);
        }
    }

    private void init(Context context) {
        mRootView = inflate(context, R.layout.vymo_search_bar, this);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.search_progress_bar);
        mSearchEditText = (EditText) mRootView.findViewById(R.id.search_et);
        mClearBtn = (ImageView) mRootView.findViewById(R.id.clear_all);
        mSearchBtn = (ImageView) mRootView.findViewById(R.id.search_icon);
        mClearBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchEditText.setText("");
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mTextWatcher != null) {
                    mTextWatcher.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mClearBtn.setVisibility(VISIBLE);
                } else {
                    mClearBtn.setVisibility(GONE);
                }

                if (mTextWatcher != null) {
                    mTextWatcher.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mTextWatcher != null) {
                    mTextWatcher.afterTextChanged(s);
                }
            }
        });
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VymoSearchBar, 0, 0);
        mEditTextBackground = ta.getColor(R.styleable.VymoSearchBar_search_bar_background_color, ContextCompat.getColor(context, android.R.color.transparent));
        mEditTextHintColor = ta.getColor(R.styleable.VymoSearchBar_search_bar_hint_text_color, ContextCompat.getColor(context, R.color.date_bg));
        mEditTextColor = ta.getColor(R.styleable.VymoSearchBar_search_bar_text_color, ContextCompat.getColor(context, R.color.date_bg));
        mHintText = ta.getString(R.styleable.VymoSearchBar_hint);
        mText = ta.getString(R.styleable.VymoSearchBar_text);
        mClearButtonDrawable = ta.getDrawable(R.styleable.VymoSearchBar_clear_icon);
        mSearchButtonDrawable = ta.getDrawable(R.styleable.VymoSearchBar_search_icon);
        ta.recycle();
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        mTextWatcher = textWatcher;
    }

    public void removeTextWatcher(TextWatcher textWatcher) {
        mSearchEditText.removeTextChangedListener(textWatcher);
        mTextWatcher = null;
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
        mSearchEditText.setOnEditorActionListener(listener);
    }

    public Editable getText() {
        return mSearchEditText.getText();
    }

    public void setText(String text) {
        mSearchEditText.setText(text);
    }

    public void showHideProgressBar(boolean show) {
        mProgressBar.setVisibility(show ? VISIBLE : GONE);
    }

    public void showHideClearButton(boolean show) {
        mClearBtn.setVisibility(show ? VISIBLE : GONE);
    }
}
