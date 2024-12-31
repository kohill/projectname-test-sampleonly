package com.healthedge.app.data;

import com.healthedge.common.data.Data;

public class HomePageData implements Data {

    private String documentName;
    private String selectedValue;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
