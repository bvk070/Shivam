package com.sadiwala.shivam.inputfields.searchinputfield;

import androidx.appcompat.widget.AppCompatCheckedTextView;

import com.sadiwala.shivam.models.common.CodeName;
import com.sadiwala.shivam.models.common.ICodeName;

public interface SectionedListListener {

    void onSectionListItemClick(CodeName codeName, AppCompatCheckedTextView checkedTextView);

    void setViewForItem(ICodeName codeName, AppCompatCheckedTextView checkedTextView);

}
