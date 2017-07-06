package com.framgia.wsm.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by tri on 06/07/2017.
 */

public class ErrorMessage extends BaseModel {
    @Expose
    @SerializedName("base")
    private List<String> mMessageList;

    public String getMessageList() {
        String listMessage = "";
        for (String message : mMessageList) {
            listMessage += "\n" + "-" + message;
        }
        return listMessage;
    }
}