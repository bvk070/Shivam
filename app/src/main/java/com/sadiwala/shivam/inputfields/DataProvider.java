package com.sadiwala.shivam.inputfields;

import android.app.Activity;

import com.sadiwala.shivam.inputfields.searchinputfield.SelectionInputfieldSearchResultsCallback;
import com.sadiwala.shivam.models.common.ICodeName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface DataProvider extends Serializable {

    List<ICodeName> getOptions();

    String getOnlineSearchUrl();

    void searchOnline(Activity activity, String searchQuery, SelectionInputfieldSearchResultsCallback callback, Map<String, String> additionalParams);
}
