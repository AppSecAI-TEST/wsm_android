package com.framgia.wsm.screen.listrequest;

import android.databinding.BaseObservable;
import com.framgia.wsm.data.model.Request;
import com.framgia.wsm.screen.BaseRecyclerViewAdapter;

/**
 * Created by ASUS on 13/06/2017.
 */

public class ItemListRequestViewModel extends BaseObservable {
    private final Request mRequest;
    private final BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Request>
            mItemClickListener;

    public ItemListRequestViewModel(Request request,
            BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Request> itemClickListener) {
        mRequest = request;
        mItemClickListener = itemClickListener;
    }

    public String getCreateAt() {
        if (mRequest != null) {
            return mRequest.getCreatedAt();
        }
        return "";
    }

    public int getLeaveTypeId() {
        if (mRequest != null && mRequest.getLeaveType() != null) {
            return mRequest.getLeaveType().getId();
        }
        return 0;
    }

    public String getLeaveTypeName() {
        if (mRequest != null && mRequest.getLeaveType() != null) {
            return mRequest.getLeaveType().getName();
        }
        return "";
    }

    public String getLeaveTypeCode() {
        if (mRequest != null && mRequest.getLeaveType() != null) {
            return mRequest.getLeaveType().getCode();
        }
        return "";
    }

    public String getFromTime() {
        if (mRequest != null) {
            return mRequest.getFromTime();
        }
        return "";
    }

    public String getToTime() {
        if (mRequest != null) {
            return mRequest.getToTime();
        }
        return "";
    }

    public String getCheckinTime() {
        if (mRequest != null) {
            return mRequest.getCheckinTime();
        }
        return "";
    }

    public String getCheckoutTime() {
        if (mRequest != null) {
            return mRequest.getCheckoutTime();
        }
        return "";
    }

    public int getStatus() {
        if (mRequest != null) {
            return mRequest.getStatus();
        }
        return 0;
    }

    public void onItemClicked() {
        if (mItemClickListener == null) {
            return;
        }
        mItemClickListener.onItemRecyclerViewClick(mRequest);
    }
}