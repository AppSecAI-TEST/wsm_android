package com.framgia.wsm.screen.listrequest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import com.framgia.wsm.BR;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.Request;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.screen.BaseRecyclerViewAdapter;
import com.framgia.wsm.utils.RequestType;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import com.fstyle.library.MaterialDialog;
import java.util.Calendar;
import java.util.List;

/**
 * Exposes the data to be used in the ListRequest screen.
 */

public class ListRequestViewModel extends BaseObservable
        implements ListRequestContract.ViewModel, DatePickerDialog.OnDateSetListener,
        BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<Request> {
    private static final String TAG = "ListRequestViewModel";
    private static final int FORMAT_MONTH = 10;

    private Context mContext;
    private ListRequestContract.Presenter mPresenter;
    private DialogManager mDialogManager;
    private int mCurrentPositionStatus;
    private String mCurrentStatus;
    private String mMonthYear;
    private int mYear;
    private int mMonth;
    private Calendar mCalendar;
    private ListRequestAdapter mListRequestAdapter;
    private Navigator mNavigator;
    private Request mRequest;
    private User mUser;
    @RequestType
    private int mRequestType;

    public ListRequestViewModel(Context context, ListRequestContract.Presenter presenter,
            DialogManager dialogManager, ListRequestAdapter listRequestAdapter,
            Navigator navigator) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mDialogManager = dialogManager;
        mCurrentPositionStatus = PositionStatus.ALL;
        mCurrentStatus = mContext.getString(R.string.all);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDialogManager.dialogMonthYearPicker(this, mYear, mMonth);
        mListRequestAdapter = listRequestAdapter;
        mListRequestAdapter.setItemClickListener(this);
        mNavigator = navigator;
        mPresenter.getUser();
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void onItemRecyclerViewClick(Request item) {
        // TODO: Click item list request
    }

    @Override
    public void onGetListRequestError(BaseException e) {
        mNavigator.showToast(e.getMessage());
    }

    @Override
    public void onGetListRequestSuccess(List<Request> requests) {
        mListRequestAdapter.updateData(requests);
    }

    @Override
    public void onGetUserSuccess(User user) {
        if (user == null) {
            return;
        }
        mUser = user;
        notifyPropertyChanged(BR.user);
        mPresenter.getListAllRequest(mRequestType, mUser.getId());
    }

    @Override
    public void onGetUserError(BaseException e) {
        Log.e(TAG, "ListRequestViewModel", e);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        updateMonthYear();
    }

    public void setCurrentPositionStatus(int currentPositionStatus) {
        mCurrentPositionStatus = currentPositionStatus;
    }

    @Bindable
    public String getCurrentStatus() {
        return mCurrentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        mCurrentStatus = currentStatus;
        notifyPropertyChanged(BR.currentStatus);
    }

    @Bindable
    public String getMonthYear() {
        return mMonthYear;
    }

    public void setMonthYear(String monthYear) {
        mMonthYear = monthYear;
        notifyPropertyChanged(BR.monthYear);
    }

    public ListRequestAdapter getListRequestAdapter() {
        return mListRequestAdapter;
    }

    public Request getRequest() {
        return mRequest;
    }

    public void setRequestType(int requestType) {
        mRequestType = requestType;
    }

    public void onPickMonthYear(View view) {
        mDialogManager.showMonthYearPickerDialog();
    }

    private void updateMonthYear() {
        // Default value mMonth = 0 (From 0 to 11) => localMonth = 1 (From 1 to 12)
        int localMonth = (mMonth + 1);
        // Format month 01/2017...instead 1/2017...
        String month = localMonth < FORMAT_MONTH ? "0" + localMonth : Integer.toString(localMonth);
        String year = Integer.toString(mYear);
        setMonthYear(String.valueOf(
                new StringBuilder().append(month).append("/").append(year).append("")));
    }

    public void onPickTypeStatus(View view) {
        mDialogManager.dialogListSingleChoice(mContext.getString(R.string.status), R.array.status,
                mCurrentPositionStatus, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view,
                            int positionType, CharSequence charSequence) {
                        setCurrentPositionStatus(positionType);
                        setCurrentStatus(String.valueOf(charSequence));
                        return true;
                    }
                });
    }

    @IntDef({
            PositionStatus.ALL, PositionStatus.ACCEPTED, PositionStatus.PENDING,
            PositionStatus.REJECTED
    })
    @interface PositionStatus {
        int ALL = 0;
        int ACCEPTED = 1;
        int PENDING = 2;
        int REJECTED = 3;
    }
}