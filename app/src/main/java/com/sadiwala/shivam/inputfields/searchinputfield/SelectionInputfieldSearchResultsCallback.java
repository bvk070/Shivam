package com.sadiwala.shivam.inputfields.searchinputfield;

import com.sadiwala.shivam.models.common.ICodeName;

import java.util.List;


public interface SelectionInputfieldSearchResultsCallback {
    void onSuccess(String searchTerm, List<ICodeName> results);

    void onFailure(String error);
}
