package com.sadiwala.shivam.inputfields.searchinputfield;


import static com.sadiwala.shivam.util.AaryaConstants.REQUEST_CODE;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.base.BaseBottomSheet;
import com.sadiwala.shivam.base.SourceRouteUtil;
import com.sadiwala.shivam.base.VymoBaseAdapter;
import com.sadiwala.shivam.inputfields.DataProvider;
import com.sadiwala.shivam.inputfields.InputFieldType;
import com.sadiwala.shivam.inputfields.SelectionInputField;
import com.sadiwala.shivam.models.common.CodeName;
import com.sadiwala.shivam.models.common.IBottomSheetListener;
import com.sadiwala.shivam.models.common.ICodeName;
import com.sadiwala.shivam.util.BottomSheetListView;
import com.sadiwala.shivam.util.CustomTextView;
import com.sadiwala.shivam.util.Gson;
import com.sadiwala.shivam.util.MarginItemDecoration;
import com.sadiwala.shivam.util.StringUtils;
import com.sadiwala.shivam.util.UiUtil;
import com.sadiwala.shivam.util.Util;
import com.sadiwala.shivam.util.VymoNestedScrollView;
import com.sadiwala.shivam.util.VymoSearchBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link ListFragment} subclass.
 */
public class SelectionFragment extends BaseBottomSheet implements OnRemoveListener, SectionedListListener {
    public static final String BottomSheetConfirmationDialog = "BottomSheetConfirmationDialog";
    public static final String TAG = "SF";
    private static final String CODE_NAME = "CODE_NAME";
    public static final String SEARCH_CHECK_ENABLE = "search_check_enable";
    protected List<ICodeName> mOptions, mAllOfflineOptions;
    private static final int MAX_ITEMS_TO_ENABLE_SEARCH = 7;

    protected Map<String, ICodeName> mSelectedOptionsMap;
    private VymoNestedScrollView mNestedScrollView;
    protected boolean mSingleSelect, mSectionedList;
    private int mMinSelection;
    private int mMaxSelection;
    private VymoBaseAdapter<ICodeName> mListAdapter;
    private DataProvider mDataProvider;
    private String mHint;
    private String positiveButtonName;
    private CustomTextView mSearchMoreTxt, tvSelected, tvOthers;
    private String mLastSearchedQuery;
    private BottomSheetListView mListView;
    private View mProgressbar;
    private VymoSearchBar search;
    private boolean isShowingOnlineResults; // true if results are from backend API call.
    private TextView mErrorView;
    private RelativeLayout mainContainer, selectedSection, otherSection;
    protected IBottomSheetListener iBottomSheetListener;
    protected int requestCode;
    private boolean searchCheckEnable = false;
    private boolean isDetect = false;
    private boolean noSearchBar = false;
    private List<ICodeName> leads;
    private RecyclerView sectionedRecyclerView;

    private static final String ONLINE = "online";
    private static final String OFFLINE = "offline";
    public static final String NONE = "none";

    public static void showSelectionFragment(AppCompatActivity appCompatActivity, Bundle args) {
        IBottomSheetListener iBottomSheetListener = SourceRouteUtil.getBottomSheetListener(appCompatActivity);
        if (iBottomSheetListener != null) {
            SelectionFragment selectionFragment = SelectionFragment.newInstance(args, iBottomSheetListener);
            selectionFragment.setCancelable(true);
            selectionFragment.show(appCompatActivity.getSupportFragmentManager(),
                    SelectionFragment.TAG);
        }
    }

    public SelectionFragment() {
    }

    public SelectionFragment(IBottomSheetListener iBottomSheetListener) {
        // Required empty public constructor
        this.iBottomSheetListener = iBottomSheetListener;
    }

    public static SelectionFragment newInstance(Bundle args, IBottomSheetListener iBottomSheetListener) {
        SelectionFragment selectionFragment = new SelectionFragment(iBottomSheetListener);
        selectionFragment.setArguments(args);
        return selectionFragment;
    }

    @Override
    protected String getBottomSheetTitle() {
        return mHint;
    }

    @Override
    public boolean showClear() {
        return true;
    }

    @Override
    public void clearClicked() {
        // In single select dropdown, clear will close the bottomsheet after resetting all items
        Intent intent = new Intent();
        intent.putExtra(SelectionInputField.EXTRAS_CODE, getArguments().getString(SelectionInputField.EXTRAS_CODE));
        intent.putExtra(CLEAR_CLICK, true);
        iBottomSheetListener.onActivityResult(requestCode, AppCompatActivity.RESULT_OK, intent);
        dismiss();
    }

    @Override
    public boolean showSelectAll() {
        return !mSingleSelect;
    }

    @Override
    public void selectAllClicked() {

        List<ICodeName> codeNames = getCurrentListItems();
        if (mMaxSelection > 0 && mMaxSelection <= codeNames.size()) {
            // if there is mMaxSelection, we should not allow to select all, instead select mMaxSelection.
            List<ICodeName> maxCodeNames = new ArrayList<>();
            for (int i = 0; i < mMaxSelection; i++) {
                maxCodeNames.add(codeNames.get(i));
            }
            mSelectedOptionsMap = mapify(maxCodeNames);
            Toast.makeText(getActivity(), getActivity().getString(R.string.max_limit_selected, mMaxSelection), Toast.LENGTH_SHORT).show();
        } else {
            mSelectedOptionsMap = mapify(codeNames);
        }

        mListAdapter.notifyDataSetChanged();

    }

    private boolean isAllSelected() {
        return (getCurrentListItems().size() <= mSelectedOptionsMap.size());
    }

    private void checkAndUpdateAllSelection() {

    }

    @Override
    public boolean showRefresh() {
        return false;
    }

    @Override
    public void refreshClicked() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selection_input_field_list_view, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestCode = getArguments().getInt(REQUEST_CODE);
        mHint = getArguments().getString(SelectionInputField.HINT);
        positiveButtonName = getArguments().getString(SelectionInputField.POSITIVE_BUTTON_NAME);
        mSingleSelect = getArguments().getBoolean(SelectionInputField.EXTRAS_SINGLE_SELECT, false);
        mMinSelection = getArguments().getInt(SelectionInputField.EXTRAS_MIN_ITEMS_SELECTION, -1);
        mMaxSelection = getArguments().getInt(SelectionInputField.EXTRAS_MAX_ITEMS_SELECTION, -1);
        searchCheckEnable = getArguments().getBoolean(SEARCH_CHECK_ENABLE, false);


        mainContainer = view.findViewById(R.id.main_container);
        mainContainer.setBackground(getResources().getDrawable(R.drawable.bottom_sheet_bg));
        selectedSection = view.findViewById(R.id.selected_section);
        otherSection = view.findViewById(R.id.other_section);
        tvSelected = view.findViewById(R.id.tv_selected);
        tvOthers = view.findViewById(R.id.tv_others);
        mNestedScrollView = (VymoNestedScrollView) view.findViewById(R.id.nested_scroll);
        mSearchMoreTxt = view.findViewById(R.id.txt_search_more);
        int maxHeight = UiUtil.getDpToPixel(100);
        mNestedScrollView.setMaxHeight(maxHeight);
        if (mSingleSelect) {
            mNestedScrollView.setVisibility(View.GONE);
        }

        if (mOptions == null) {
            mOptions = new LinkedList<>();
        }

        mDataProvider = (DataProvider) getArguments().getSerializable(SelectionInputField.EXTRA_DATA_PROVIDER);

        // List of selected items.
        String selected = getArguments().getString(SelectionInputField.EXTRAS_SELECTED_OPTIONS, "[]");
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SelectionInputField.EXTRAS_SELECTED_OPTIONS)) {
                selected = savedInstanceState.getString(SelectionInputField.EXTRAS_SELECTED_OPTIONS, "[]");
            }
        }

        List<ICodeName> selectedOptions = new ArrayList<>();

        try {
            String type = CODE_NAME;
            if (getArguments().containsKey(SelectionInputField.EXTRAS_SELECTED_OPTIONS_CLASS_TYPE)) {
                type = getArguments().getString(SelectionInputField.EXTRAS_SELECTED_OPTIONS_CLASS_TYPE);
            }
            Type typeOfList = new TypeToken<List<CodeName>>() {
            }.getType();

            selectedOptions = Gson.getInstance().fromJson(selected, typeOfList);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Invalid selected options: " + selected);
        }

        mSelectedOptionsMap = mapify(selectedOptions);

        Button btnSearchFinished = (Button) view.findViewById(R.id.btn_search_finished);
        if (positiveButtonName != null) {
            btnSearchFinished.setText(positiveButtonName);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btnSearchFinished.setBackgroundTintList(ColorStateList.valueOf(UiUtil.getBrandedPrimaryColorWithDefault()));
        } else {
            btnSearchFinished.setBackgroundColor(UiUtil.getBrandedPrimaryColorWithDefault());
        }

        btnSearchFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMinSelection > 0 && mSelectedOptionsMap.size() < mMinSelection) {
                    String message = StringUtils.getString(R.string.minimum_item_validation, mMinSelection);
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    return;
                }
                returnResults();
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (hideConfirmButton()) {
            view.findViewById(R.id.buttons_container).setVisibility(View.GONE);
        }

        mListAdapter = getAdapter();
        mListView = view.findViewById(R.id.lv_search_input_fields);
        mProgressbar = view.findViewById(R.id.progress_bar);
        mErrorView = view.findViewById(R.id.error);
        if (!mSectionedList) {
            mListView.setAdapter(mListAdapter);
            if (TextUtils.isEmpty(mDataProvider.getOnlineSearchUrl())) {
                mListView.setEmptyView(mErrorView);
            }

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ICodeName codeName = (ICodeName) ((VymoBaseAdapter) ((HeaderViewListAdapter) mListView.getAdapter()).getWrappedAdapter()).getFilteredList().get(i);
                    final AppCompatCheckedTextView checkedTextView =
                            (AppCompatCheckedTextView) view.findViewById(R.id.txt_search_item_name);
                    onItemSelected(codeName, checkedTextView);
                }
            });

            mListView.setOnTouchListener(new ListView.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow NestedScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow NestedScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });

            // add footer
            View footerView = new View(getActivity());
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getDpToPixel(30));
            footerView.setLayoutParams(params);
            footerView.setBackgroundColor(UiUtil.getColor(R.color.background));
            mListView.addFooterView(footerView, null, false);
        }

        // sectioned recycleview
        sectionedRecyclerView = getView().findViewById(R.id.sectionedRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        sectionedRecyclerView.setLayoutManager(linearLayoutManager);
        sectionedRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sectionedRecyclerView.addItemDecoration(new MarginItemDecoration(0, 0, 0, 0));

        if (mSectionedList) {
            sectionedRecyclerView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            sectionedRecyclerView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }

        search = (VymoSearchBar) view.findViewById(R.id.etxt_input_search);

        search.setTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mListAdapter instanceof Filterable && !mSectionedList) {
                    mErrorView.setVisibility(View.GONE);
                    if (mSectionedList) {
                        sectionedRecyclerView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    } else {
                        sectionedRecyclerView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }
                    String searchedQuery = s.toString();
                    if (mLastSearchedQuery != null && (searchedQuery.length() < mLastSearchedQuery.length())) {
                        getAllOfflineOptions();
                    }
                    ((Filterable) mListAdapter).getFilter().filter(s);
                    if (!TextUtils.isEmpty(mDataProvider.getOnlineSearchUrl())) {
                        if (TextUtils.isEmpty(searchedQuery) || searchedQuery.length() < 3 || searchedQuery.equals(mLastSearchedQuery)) {
                            mSearchMoreTxt.setVisibility(View.GONE);
                        } else {
                            mSearchMoreTxt.setVisibility(View.VISIBLE);
                        }
                        mLastSearchedQuery = searchedQuery;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnlineResults();
            }
        });

        if (noSearchBar) {
            search.setVisibility(View.GONE);
        } else {
            search.setVisibility(View.VISIBLE);
        }
    }

    private void getAllOfflineOptions() {
        mOptions.clear();
        mOptions.addAll(mAllOfflineOptions);
        mListAdapter.notifyDataSetChanged();
    }

    protected boolean hideConfirmButton() {
        return mSingleSelect;
    }

    protected VymoBaseAdapter<ICodeName> getAdapter() {
        return new VymoBaseAdapter<ICodeName>(getActivity(), mOptions) {
            @Override
            public int getLayout() {
                return R.layout.selection_input_field_list_row_view;
            }

            @Override
            protected void populateViewForItem(View view, final ICodeName codeName) {

                final LinearLayout layoutView = (LinearLayout) view.findViewById(R.id.search_item_layout);
                final AppCompatCheckedTextView checkedTextView =
                        (AppCompatCheckedTextView) view.findViewById(R.id.txt_search_item_name);
                checkedTextView.setText(codeName.getName());
                checkedTextView.setVisibility(View.VISIBLE);

                getDefaultCheckedMark(checkedTextView);
                updateList(codeName.getCode(), checkedTextView);

                layoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemSelected(codeName, checkedTextView);
                    }
                });
            }

            @Override
            protected boolean search(CharSequence searchTerm, ICodeName codeName) {
                return (codeName.getName().toUpperCase().contains(
                        searchTerm.toString().toUpperCase()));
            }

            @Override
            public void updateFilteredList(List<ICodeName> searchList) {
                super.updateFilteredList(searchList);
                if (!TextUtils.isEmpty(mDataProvider.getOnlineSearchUrl())) {
                    if (mLastSearchedQuery.length() >= 3) {
                        int searchListCount = searchList != null ? searchList.size() : 0;
                        if (searchListCount <= 0) {
                            mListView.setVisibility(View.GONE);
                        } else {
                            if (mSectionedList) {
                                sectionedRecyclerView.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.GONE);
                            } else {
                                sectionedRecyclerView.setVisibility(View.GONE);
                                mListView.setVisibility(View.VISIBLE);
                            }
                            mErrorView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        };
    }

    private void getOnlineResults() {
        isShowingOnlineResults = true;
        SelectionInputfieldSearchResultsCallback callback = new SelectionInputfieldSearchResultsCallback() {
            @Override
            public void onSuccess(String searchTerm, List<ICodeName> results) {
                if (!TextUtils.isEmpty(searchTerm) && searchTerm.equals(mLastSearchedQuery)) {
                    if (!Util.isListEmpty(results)) {
                        mOptions = results;
                        if (mSectionedList) {
                            sectionedRecyclerView.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        } else {
                            sectionedRecyclerView.setVisibility(View.GONE);
                            mListView.setVisibility(View.VISIBLE);
                        }
                        mErrorView.setVisibility(View.GONE);
                        mListAdapter.updateFilteredList(mOptions);
                        mListAdapter.notifyDataSetChanged();
                    } else {
                        mListView.setVisibility(View.GONE);
                        mErrorView.setVisibility(View.VISIBLE);
                        mErrorView.setText(getString(R.string.no_result));
                    }
                }
                mProgressbar.setVisibility(View.GONE);
                if (!noSearchBar) {
                    updateSearchView();
                }
            }

            @Override
            public void onFailure(String error) {
                mProgressbar.setVisibility(View.GONE);
                mListView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                mErrorView.setText(error);

                Toast.makeText(SelectionFragment.this.getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        };
        mListView.setVisibility(View.INVISIBLE);
        mProgressbar.setVisibility(View.VISIBLE);
        mSearchMoreTxt.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mDataProvider.searchOnline(getActivity(), mLastSearchedQuery, callback, null);
    }

    protected void onItemSelected(ICodeName codeName, AppCompatCheckedTextView checkedTextView) {
        if (mSingleSelect) {
            mSelectedOptionsMap.clear();
            mSelectedOptionsMap.put(codeName.getCode(), codeName);
            mListAdapter.notifyDataSetChanged();
            updateList(codeName.getCode(), checkedTextView);
            returnResultsToChild(codeName);
        } else {
            if (mSelectedOptionsMap.containsKey(codeName.getCode())) {
                mSelectedOptionsMap.remove(codeName.getCode());
            } else {
                mSelectedOptionsMap.put(codeName.getCode(), codeName);
            }
            updateList(codeName.getCode(), checkedTextView);
        }
        checkAndUpdateAllSelection();
    }

    @Override
    public void onStart() {
        super.onStart();

        getOptionsFromLocalCache();
        mListAdapter.notifyDataSetChanged();
    }

    private void getOptionsFromLocalCache() {
        if (mDataProvider != null) {
            isShowingOnlineResults = false;
            Log.e("performance", "onStart before getting options: " + System.currentTimeMillis());
            List<ICodeName> options = new ArrayList<>(mSelectedOptionsMap.values());
            options.addAll(mDataProvider.getOptions());
            if (options.isEmpty()) {
                mListView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.VISIBLE);
                mErrorView.setText(getString(R.string.no_result));
            }

            Log.e("performance", "After getting options: " + System.currentTimeMillis());

            if (options != null) {
                Set<String> setForDuplicationRemoval = new HashSet<>(mOptions.size());
                mOptions.clear();
                Log.e("performance", "size = " + options.size());
                Log.e("performance", "creating duplicate records list: start: " + System.currentTimeMillis());

                for (ICodeName codeName : options) {
                    if (codeName != null && !TextUtils.isEmpty(codeName.getCode()) && !TextUtils.isEmpty(codeName.getName())
                            && !setForDuplicationRemoval.contains(codeName.getCode()) && !codeName.getCode().equals(NONE)) {
                        mOptions.add(codeName);
                        setForDuplicationRemoval.add(codeName.getCode());
                    }
                }
                if (mAllOfflineOptions == null) {
                    mAllOfflineOptions = new ArrayList<>();
                }
                mAllOfflineOptions.addAll(mOptions);
            }

        } else {
            Log.e(TAG, "data provider is null");
        }

        mListAdapter.notifyDataSetChanged();
        if (!noSearchBar) {
            updateSearchView();
        }
    }

    private void updateSearchView() {
        if (searchCheckEnable) {
            if (!Util.isListEmpty(mOptions) && mOptions.size() > MAX_ITEMS_TO_ENABLE_SEARCH) {
                search.setVisibility(View.VISIBLE);
            } else {
                search.setVisibility(View.GONE);
            }
        } else {
            search.setVisibility(View.VISIBLE);
        }
    }

    protected void updateList(String code, AppCompatCheckedTextView checkedTV) {
        changeTextStyle(checkedTV, mSelectedOptionsMap.containsKey(code));
        changeCheckedState(checkedTV, mSelectedOptionsMap.containsKey(code));
        updateHSVVisibility();
    }

    // update Horizontal Scroll View Visibility
    protected void updateHSVVisibility() {

    }

    private Map<String, ICodeName> mapify(List<ICodeName> list) {
        final Map<String, ICodeName> map = new LinkedHashMap<>();

        for (ICodeName option : list) {
            map.put(option.getCode(), option);
        }

        return map;
    }

    private Map<String, ICodeName> demapify(List<ICodeName> list) {
        final Map<String, ICodeName> map = new LinkedHashMap<>();

        for (ICodeName option : list) {
            map.remove(option.getCode());
        }

        return map;
    }

    private List<ICodeName> getCurrentListItems() {
        List<ICodeName> codeNames = new ArrayList<>();
        codeNames = ((VymoBaseAdapter) ((HeaderViewListAdapter) mListView.getAdapter()).getWrappedAdapter()).getFilteredList();
        return codeNames;
    }

    private String getSelectedOptionsJson() {
        return Gson.getInstance().toJson(new ArrayList<ICodeName>(mSelectedOptionsMap.values()));
    }

    private void changeCheckedState(AppCompatCheckedTextView view, boolean showBoldAndColored) {
        if (showBoldAndColored) {
            view.setChecked(true);
        } else {
            view.setChecked(false);
        }
    }

    private void changeTextStyle(AppCompatCheckedTextView view, boolean showBoldAndColored) {
        Drawable drawable;
        if (showBoldAndColored) {
            drawable = UiUtil.paintImageInBrandedColor(ContextCompat.getDrawable(getActivity(), R.drawable.colored_select));
            view.setCheckMarkDrawable(drawable);
        } else {
            drawable = UiUtil.paintImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.select),
                    ContextCompat.getColor(getActivity(), R.color.vymo_disabled));
            if (!mSingleSelect) {
                view.setCheckMarkDrawable(drawable);
            }
        }
    }

    /**
     * Remove checkbox mark if it is single select.
     */
    protected void getDefaultCheckedMark(AppCompatCheckedTextView view) {
        int resourceId = R.drawable.select;
        if (mSingleSelect) {
            view.setCheckMarkDrawable(null);
        } else {
            view.setCheckMarkDrawable(resourceId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SelectionInputField.EXTRAS_SELECTED_OPTIONS, getSelectedOptionsJson());
    }

    protected void returnResultsToChild(ICodeName codeName) {
        returnResults();
    }

    /**
     * This will close current activity and return selected items back to add/update form.
     */
    protected void returnResults() {
        Intent intent = new Intent();
        intent.putExtra(SelectionInputField.EXTRAS_CODE, getArguments().getString(SelectionInputField.EXTRAS_CODE));
        intent.putExtra(SelectionInputField.EXTRAS_SELECTED_OPTIONS, getSelectedOptionsJson());
        iBottomSheetListener.onActivityResult(requestCode, AppCompatActivity.RESULT_OK, intent);
        dismiss();
    }

    private String getPrefName() {
        return InputFieldType.INPUT_FIELD_TYPE_SELECTION + "_" +
                getArguments().getString(SelectionInputField.EXTRAS_CODE);
    }

    @Override
    public void onItemRemoved(String code) {
        if (mSelectedOptionsMap.containsKey(code)) {
            mSelectedOptionsMap.remove(code);
            mListAdapter.notifyDataSetChanged();
            checkAndUpdateAllSelection();
        }
    }

    @Override
    public void onSectionListItemClick(CodeName codeName, AppCompatCheckedTextView checkedTextView) {
        onItemSelected(codeName, checkedTextView);
    }

    @Override
    public void setViewForItem(ICodeName codeName, AppCompatCheckedTextView checkedTextView) {
        if (NONE.equalsIgnoreCase(codeName.getCode()) &&
                !(InputFieldType.INPUT_FIELD_TYPE_SPINNER.equals(getArguments().getString(SelectionInputField.EXTRAS_TYPE)) ||
                        InputFieldType.INPUT_FIELD_TYPE_CODE_NAME_SPINNER.equals(getArguments().getString(SelectionInputField.EXTRAS_TYPE)))) {
            return;
        }

        checkedTextView.setText(codeName.getName());
        checkedTextView.setVisibility(View.VISIBLE);

        getDefaultCheckedMark(checkedTextView);
        updateList(codeName.getCode(), checkedTextView);
    }


}