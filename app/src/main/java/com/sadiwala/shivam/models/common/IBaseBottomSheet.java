package com.sadiwala.shivam.models.common;

import android.widget.TextView;

public interface IBaseBottomSheet {

    boolean showRefresh();

    void refreshClicked();

    boolean showClear();

    void clearClicked();

    boolean showSelectAll();

    void selectAllClicked();

    TextView getSelectAllTextView();

}
