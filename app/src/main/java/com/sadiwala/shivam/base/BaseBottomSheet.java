package com.sadiwala.shivam.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sadiwala.shivam.R;
import com.sadiwala.shivam.models.common.IBaseBottomSheet;
import com.sadiwala.shivam.util.UiUtil;


public abstract class BaseBottomSheet extends BottomSheetDialogFragment implements IBaseBottomSheet {

    public static final String CLEAR_CLICK = "clear_click";

    private BottomSheetBehavior bottomSheetBehavior;
    private TextView tvHeader, tvClear, tvRefresh, tvSelectAll;

    protected abstract String getBottomSheetTitle();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleClear();
        handleRefresh();
        handleSelectAll();
        changeTitle(getBottomSheetTitle());
    }

    private void handleClear() {
        tvClear = getView().findViewById(R.id.tvClear);
        if (tvClear != null && showClear()) {
            tvClear.setVisibility(View.VISIBLE);
            tvClear.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
            tvClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearClicked();
                }
            });
        }
    }

    private void handleRefresh() {
        tvRefresh = getView().findViewById(R.id.tvRefresh);
        if (tvRefresh != null && showRefresh()) {
            tvRefresh.setVisibility(View.VISIBLE);
            tvRefresh.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
            tvRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshClicked();
                }
            });
        }
    }

    private void handleSelectAll() {
        tvSelectAll = getView().findViewById(R.id.tvSelectAll);
        if (tvSelectAll != null && showSelectAll()) {
            tvSelectAll.setVisibility(View.VISIBLE);
            tvSelectAll.setTextColor(UiUtil.getBrandedPrimaryColorWithDefault());
            tvSelectAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectAllClicked();
                }
            });
        }
    }

    protected void changeTitle(String title) {
        tvHeader = getView().findViewById(R.id.tvHeader);
        if (tvHeader != null) {
            tvHeader.setText(title);
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        return dialog;
    }

    @Override
    public boolean showRefresh() {
        return false;
    }

    @Override
    public void refreshClicked() {

    }

    @Override
    public boolean showClear() {
        return false;
    }

    @Override
    public void clearClicked() {

    }

    @Override
    public boolean showSelectAll() {
        return false;
    }

    @Override
    public void selectAllClicked() {

    }

    @Override
    public TextView getSelectAllTextView() {
        return tvSelectAll;
    }

}
