package com.sadiwala.shivam.inputfields;


import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_CURRENCY;
import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_DECIMAL;
import static com.sadiwala.shivam.inputfields.InputFieldType.INPUT_FIELD_TYPE_NUMBER;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sadiwala.shivam.R;
import com.sadiwala.shivam.util.CustomEditText;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.UiUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;


public class TextInputField extends ParentInputField {
    protected View textInputFieldView;
    protected CustomEditText mEditText;
    private RelativeLayout textInputLayout;
    protected ImageButton btnClear;
    private CustomTextView errorText;
    private Activity mActivity;
    private final List<String> emailMiEChipsList = new ArrayList<>(Arrays.asList("gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "aol.com"));

    private String mPrePopulateValue;
    private final String TAG = "TextIF";
    private final String CODE_NAME = "name";
    public static final String MODULE_PROFILE_NUMERIC_LIMIT = "module_profile_numeric_limit";
    public static final String MODULE_PROFILE_DATE_LIMIT = "module_profile_date_limit";
    protected EventBus bus;
    protected String charsCount;
    private boolean shouldEmitFocusChangedEvent;
    protected boolean isValueChangedFromRefreshView;
    private Timer timer;

    public TextInputField(AppCompatActivity activity, Bundle savedInstanceState, String prePopulateValue,
                          InputFieldType inputField, EventBus bus, EditMode mode, String startState) {
        super(activity, bus, mode, startState);

        this.bus = bus;
        mPrePopulateValue = prePopulateValue;
        mActivity = activity;
        LayoutInflater inflater = mActivity.getLayoutInflater();

        textInputFieldView = inflater.inflate(R.layout.text_inputfield_layout, null);
        textInputLayout = textInputFieldView.findViewById(R.id.textInputLayout);
        mEditText = textInputFieldView.findViewById(R.id.text_input);
        errorText = textInputFieldView.findViewById(R.id.error_text);
        btnClear = textInputFieldView.findViewById(R.id.btnClear);

        mInputFieldType = inputField;
        mEditText.setInputType(getType(inputField.getType()));
        mEditText.setBackgroundResource(R.drawable.edit_text_background);
        mEditText.setPadding(4, 4, UiUtil.getDpToPixel(40), 8);
        mEditText.setGravity(Gravity.CENTER_VERTICAL);
        setSelectionHint();

        int minLines = mInputFieldType.getMinLines();
        mEditText.setMinLines(minLines);
        mEditText.setSingleLine(minLines <= 1);

        // If minLines <= 1 set a custom height of the textInputLayout, else the default height of the textInputLayout will be wrap_content [for multiLineTextInputField]
        // note - earlier we were hardcoding the default height and changing the height according to minLines of the inputField
        if (minLines <= 1) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textInputLayout.getLayoutParams();
            params.height = (int) getActivity().getResources().getDimension(R.dimen.icon_size_36);
            textInputLayout.setLayoutParams(params);
        }

        if (minLines > 1) {
            mEditText.setGravity(Gravity.TOP);
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // The user is typing: reset already started timer (if existing)
                if (timer != null) {
                    timer.cancel();
                }

                setCount(s.length());
                if (TextUtils.isEmpty(s)) {
                    btnClear.setVisibility(View.GONE);
                } else {
                    btnClear.setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(s) && s.length() > 0) {
                    mieChipsRecyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {

                    validateInput();
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
                mieChipsRecyclerView.setVisibility(View.GONE);
            }
        });

        addInputFilter();

        setText(savedInstanceState, prePopulateValue);
    }

    /**
     * Show Selection Hint if selection hint is available in the {@link ParentInputField#mInputFieldType}
     */
    private void setSelectionHint() {

    }

    private void setCount(int count) {
        if (mInputFieldType.getMaxChars() > 0 && !mInputFieldType.getType().equals(INPUT_FIELD_TYPE_CURRENCY)) {
            charsCount = count + "/" + mInputFieldType.getMaxChars();

        }
    }

    protected int getType(String type) {
        switch (type) {
            case InputFieldType.INPUT_FIELD_TYPE_TEXT: {
                // Special case to capitalize only name fields ex-> (John Doe)
                if (CODE_NAME.equals(mInputFieldType.getCode())) {
                    return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS;
                }
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;
            }
            case InputFieldType.INPUT_FIELD_TYPE_SENTENCE:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES;

            case INPUT_FIELD_TYPE_NUMBER:
                return InputType.TYPE_CLASS_NUMBER;

            case INPUT_FIELD_TYPE_DECIMAL:
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;

            case InputFieldType.INPUT_FIELD_TYPE_PHONE:
                return InputType.TYPE_CLASS_PHONE;

            case InputFieldType.INPUT_FIELD_TYPE_EMAIL:
                // Display chips if it is enabled from config and current inputField is not read-only
                return InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;

            default:
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS;
        }
    }

    private void addInputFilter() {
        if (EditMode.WRITE == mMode) {
            if (mInputFieldType.getMaxChars() > 0) {
                CustomEditText editText = (CustomEditText) mEditText;
                editText.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(mInputFieldType.getMaxChars())
                });
            }
        }
    }

    private void setText(Bundle savedInstanceState, String value) {
        if (!TextUtils.isEmpty(value)) {
            mEditText.setText(value);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(mInputFieldType.getCode())) {
            mEditText.setText(savedInstanceState.getString(mInputFieldType.getCode()));
        }

        //this is to put pointer in last if server sends some values in IF.
        mEditText.setSelection(mEditText.getText().length());
        // initially clear button will not be visible.
        btnClear.setVisibility(View.GONE);
    }

    @Override
    public View getFormView() {
        return textInputFieldView;
    }

    @Override
    public View getDisplayView() {
        View displayView = getReadOnlyView(mInputFieldType.getHint(), mPrePopulateValue);
        final TextView value = (TextView) displayView.findViewById(R.id.value);

        if (InputFieldType.INPUT_FIELD_TYPE_PHONE.equals(mInputFieldType.getType())) {
            value.setTextIsSelectable(false);
            value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //we are doing this as we don't have lead here so we pass back data to screen
                    //where we came from (LeadInfoFragment).

                }
            });
            value.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    copyToClipBoard(value);
                    return true;
                }
            });
        } else if (InputFieldType.INPUT_FIELD_TYPE_HTML_TEXT.equalsIgnoreCase(mInputFieldType.getType())) {
            value.setText(Html.fromHtml(mPrePopulateValue));
            value.setLinkTextColor(UiUtil.getColor(R.color.attention_text));
            value.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            value.setText(mPrePopulateValue);
            value.setLinkTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
        }

        return displayView;
    }

    private void copyToClipBoard(TextView value) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied text", value.getText().toString());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View getReadOnlyView() {
        //this will be disabled in cased where we prepopulate the value and does not want user to
        //change those value such as in case of suggestions
        if (mInputFieldType.isReadOnly()) {
            mEditText.setEnabled(false);
        }
        return textInputFieldView;
    }

    public String getTextValue() {
        return mEditText.getText().toString();
    }

    public void requestFocus() {
        if (mEditText != null) {
            mEditText.requestFocus();
        }
    }

    @Override
    public void addFilter(Map<String, String> filters) {
        String value = getTextValue();

        if (value.length() > 0) {
            filters.put(mInputFieldType.getCode(), value);
        }
    }

    @Override
    public String getJsonValue() {
        try {
            String value = getTextValue();

            if (mInputFieldType.isRequired() || value.length() > 0) {
                return Gson.getInstance().toJson(value);
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public String getOnlineValidationValue() {
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(mInputFieldType.getCode(), getTextValue());
    }

    @Override
    public void applyContextFilter(Map<String, Set<String>> filters) {

    }

    @Override
    public void refreshView(String value) {

    }

    @Override
    public void refreshPlaceholder(String value) {
    }


    public boolean validateInput() {

        int length = getTextValue().length();
        if (length > 0 && mInputFieldType.getMinChars() >= 0) {
            if (length < mInputFieldType.getMinChars()) {

                String error = mActivity.getString(R.string.error_less_letters, String.valueOf(mInputFieldType.getMinChars()));
                if (mInputFieldType.getType().equals(InputFieldType.INPUT_FIELD_TYPE_PHONE)
                        || mInputFieldType.getType().equals(INPUT_FIELD_TYPE_NUMBER)
                        || mInputFieldType.getType().equals(INPUT_FIELD_TYPE_DECIMAL)) {
                    error = mActivity.getString(R.string.error_less_digit, String.valueOf(mInputFieldType.getMinChars()));
                }

                mEditText.setBackgroundResource(R.drawable.edittext_error_background);
                errorText.setVisibility(View.VISIBLE);
                errorText.setText(error);
                //Notify that this input field is not valid to listeners.
                EventBus.getDefault().post(this);

                return false;
            }

            if (mInputFieldType.getType().equals(InputFieldType.INPUT_FIELD_TYPE_EMAIL)) {
                boolean validate = android.util.Patterns.EMAIL_ADDRESS.matcher(getTextValue()).matches();
                if (!validate) {
                    mEditText.setBackgroundResource(R.drawable.edittext_error_background);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(getErrorMessage());
                    //Notify that this input field is not valid to listeners.
                    EventBus.getDefault().post(this);

                    return false;
                }
            }

            String regex = mInputFieldType.getRegex();

            if ((regex == null) && InputFieldType.INPUT_FIELD_TYPE_PAN.equals(mInputFieldType.getType())) {
                regex = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}";
            }

            if (regex != null && regex.length() > 0) {
                Pattern pattern = Pattern.compile(regex);

                Matcher matcher = pattern.matcher(getTextValue());
                // Check if pattern matches
                boolean validate = matcher.matches();

                if (!validate) {
                    mEditText.setBackgroundResource(R.drawable.edittext_error_background);
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText(getErrorMessage());
                    //Notify that this input field is not valid to listeners.
                    EventBus.getDefault().post(this);

                    return false;
                }
            }
        } else if (mInputFieldType.isRequired()) {
            String error = mActivity.getString(R.string.error_required);
            mEditText.setBackgroundResource(R.drawable.edittext_error_background);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(error);
            //Notify that this input field is not valid to listeners.
            EventBus.getDefault().post(this);

            return false;
        }

        mEditText.setBackgroundResource(R.drawable.edit_text_background);
        errorText.setVisibility(View.GONE);
        return true;
    }

    private String getErrorMessage() {

        if (mInputFieldType.getRegexHint() != null) {
            return mInputFieldType.getRegexHint();
        } else if (!TextUtils.isEmpty(mInputFieldType.getHint())) {
            return mActivity.getString(R.string.error_invalid, mInputFieldType.getHint());
        }
        return mActivity.getString(R.string.error_invalid, mActivity.getString(R.string.empty));
    }

}
