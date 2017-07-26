package com.framgia.wsm.screen.managelistrequests;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.DatePicker;
import com.framgia.wsm.BR;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.LeaveRequest;
import com.framgia.wsm.data.model.OffRequest;
import com.framgia.wsm.data.model.QueryRequest;
import com.framgia.wsm.data.model.RequestOverTime;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.data.source.remote.api.request.ActionRequest;
import com.framgia.wsm.data.source.remote.api.response.ActionRequestResponse;
import com.framgia.wsm.screen.managelistrequests.memberrequestdetail
        .MemberRequestDetailDialogFragment;
import com.framgia.wsm.screen.managelistrequests.memberrequestdetail.MemberRequestDetailViewModel;
import com.framgia.wsm.utils.RequestType;
import com.framgia.wsm.utils.StatusCode;
import com.framgia.wsm.utils.TypeToast;
import com.framgia.wsm.utils.common.DateTimeUtils;
import com.framgia.wsm.utils.navigator.NavigateAnim;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import com.fstyle.library.DialogAction;
import com.fstyle.library.MaterialDialog;
import java.util.List;

/**
 * Exposes the data to be used in the Managelistrequests screen.
 */

public class ManageListRequestsViewModel extends BaseObservable
        implements ManageListRequestsContract.ViewModel, ItemRecyclerViewClickListener,
        ActionRequestListener, DatePickerDialog.OnDateSetListener,
        MemberRequestDetailViewModel.ApproveOrRejectListener {

    private static final String TAG = "ListRequestViewModel";

    private Context mContext;
    private ManageListRequestsContract.Presenter mPresenter;
    private DialogManager mDialogManager;
    private Navigator mNavigator;
    private ManageListRequestsAdapter mManageListRequestsAdapter;
    private int mRequestType;
    private QueryRequest mQueryRequest;
    private String mFromTime;
    private String mToTime;
    private String mCurrentStatus;
    private boolean mIsVisibleLayoutSearch;
    private boolean mIsFromTimeSelected;
    private int mCurrentPositionStatus;
    private String mUserName;
    private ActionRequest mActionRequest;
    private int mItemPosition;
    private int mAction;

    ManageListRequestsViewModel(Context context, ManageListRequestsContract.Presenter presenter,
            DialogManager dialogManager, ManageListRequestsAdapter manageListRequestsAdapter,
            Navigator navigator) {
        mContext = context;
        mNavigator = navigator;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mDialogManager = dialogManager;
        mManageListRequestsAdapter = manageListRequestsAdapter;
        mManageListRequestsAdapter.setItemClickListener(this);
        mManageListRequestsAdapter.setActionRequestListener(this);
        mDialogManager.dialogDatePicker(this);
        initData();
    }

    private void initData() {
        mActionRequest = new ActionRequest();
        mFromTime = DateTimeUtils.dayFirstMonthWorking();
        mToTime = DateTimeUtils.dayLasttMonthWorking();
        mQueryRequest = new QueryRequest();
        mQueryRequest.setFromTime(mFromTime);
        mQueryRequest.setToTime(mToTime);
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
    public void onGetListRequestManageError(BaseException exception) {
        mNavigator.showToastCustom(TypeToast.ERROR, exception.getMessage());
    }

    @Override
    public void onGetListRequestManageSuccess(Object object) {
        switch (mRequestType) {
            case RequestType.REQUEST_OVERTIME:
                List<RequestOverTime> listOverTime = (List<RequestOverTime>) object;
                showToastError(listOverTime.size());
                mManageListRequestsAdapter.updateDataRequestOverTime(listOverTime);
                break;
            case RequestType.REQUEST_OFF:
                List<OffRequest> listOff = (List<OffRequest>) object;
                showToastError(listOff.size());
                mManageListRequestsAdapter.updateDataRequestOff(listOff);
                break;
            case RequestType.REQUEST_LATE_EARLY:
                List<LeaveRequest> listLeave = (List<LeaveRequest>) object;
                showToastError(listLeave.size());
                mManageListRequestsAdapter.updateDataRequest(listLeave);
                break;
            default:
                break;
        }
    }

    @Override
    public void onReloadData() {
        mPresenter.getListAllRequestManage(mRequestType, mQueryRequest);
    }

    void setRequestType(@RequestType int requestType) {
        mRequestType = requestType;
        mQueryRequest.setRequestType(requestType);
        switch (requestType) {
            case RequestType.REQUEST_LATE_EARLY:
                mActionRequest.setTypeRequest(TypeRequest.LEAVE);
                break;
            case RequestType.REQUEST_OFF:
                mActionRequest.setTypeRequest(TypeRequest.OFF);
                break;
            case RequestType.REQUEST_OVERTIME:
                mActionRequest.setTypeRequest(TypeRequest.OT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onApproveOrRejectRequestSuccess(ActionRequestResponse actionRequestResponse) {
        updateItemRequest(mRequestType, mItemPosition, actionRequestResponse);
        if (mAction == TypeAction.APPROVE) {
            mNavigator.showToastCustom(TypeToast.SUCCESS,
                    mContext.getString(R.string.approve_success));
            return;
        }
        mNavigator.showToastCustom(TypeToast.SUCCESS, mContext.getString(R.string.reject_success));
    }

    @Override
    public void onApproveOrRejectRequestError(BaseException exception) {
        mDialogManager.dialogError(exception);
    }

    @Override
    public void onDismissProgressDialog() {
        mDialogManager.dismissProgressDialog();
    }

    @Override
    public void onShowIndeterminateProgressDialog() {
        mDialogManager.showIndeterminateProgressDialog();
    }

    @Override
    public void onApproveRequest(int itemPosition, int requestId) {
        mAction = TypeAction.APPROVE;
        approveOrRejectRequest(itemPosition, requestId, StatusCode.ACCEPT_CODE);
    }

    @Override
    public void onRejectRequest(int itemPosition, int requestId) {
        mAction = TypeAction.REJECT;
        approveOrRejectRequest(itemPosition, requestId, StatusCode.REJECT_CODE);
    }

    @Override
    public void onItemRecyclerViewClick(Object object, int position) {
        MemberRequestDetailDialogFragment dialogFragment =
                MemberRequestDetailDialogFragment.newInstance(object, position);
        dialogFragment.setApproveOrRejectListener(this);
        mNavigator.showDialogFragment(R.id.layout_container, dialogFragment, false,
                NavigateAnim.NONE, MemberRequestDetailDialogFragment.TAG);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateTime = DateTimeUtils.convertDateToString(year, month, dayOfMonth);
        if (mIsFromTimeSelected) {
            validateFromTime(dateTime);
        } else {
            setToTime(dateTime);
        }
    }

    public ManageListRequestsAdapter getManageListRequestsAdapter() {
        return mManageListRequestsAdapter;
    }

    private void updateItemRequest(@RequestType int requestType, int itemPosition,
            ActionRequestResponse actionRequestResponse) {
        switch (requestType) {
            case RequestType.REQUEST_LATE_EARLY:
                mManageListRequestsAdapter.updateItem(requestType, itemPosition,
                        actionRequestResponse.getStatus());
                break;
            case RequestType.REQUEST_OFF:
                mManageListRequestsAdapter.updateItem(requestType, itemPosition,
                        actionRequestResponse.getStatus());
                break;
            case RequestType.REQUEST_OVERTIME:
                mManageListRequestsAdapter.updateItem(requestType, itemPosition,
                        actionRequestResponse.getStatus());
                break;
            default:
                break;
        }
    }

    private void approveOrRejectRequest(int itemPosition, int requestId, String statusCode) {
        mItemPosition = itemPosition;
        mActionRequest.setStatus(statusCode);
        mActionRequest.setRequestId(requestId);
        mPresenter.approveOrRejectRequest(mActionRequest);
    }

    private void showToastError(int size) {
        if (size == 0) {
            mNavigator.showToastCustom(TypeToast.INFOR,
                    mContext.getString(R.string.can_not_find_data));
        }
    }

    @Bindable
    public boolean isVisibleLayoutSearch() {
        return mIsVisibleLayoutSearch;
    }

    private void setVisibleLayoutSearch(boolean visibleLayoutSearch) {
        mIsVisibleLayoutSearch = visibleLayoutSearch;
        notifyPropertyChanged(BR.visibleLayoutSearch);
    }

    @Bindable
    public String getFromTime() {
        return mFromTime;
    }

    public void setFromTime(String fromTime) {
        mFromTime = fromTime;
        mQueryRequest.setFromTime(fromTime);
        notifyPropertyChanged(BR.fromTime);
    }

    @Bindable
    public String getToTime() {
        return mToTime;
    }

    public void setToTime(String toTime) {
        mToTime = toTime;
        mQueryRequest.setToTime(toTime);
        notifyPropertyChanged(BR.toTime);
    }

    @Bindable
    public String getCurrentStatus() {
        return mCurrentStatus;
    }

    private void setCurrentStatus(String currentStatus) {
        mCurrentStatus = currentStatus;
        notifyPropertyChanged(BR.currentStatus);
    }

    @Bindable
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
        mQueryRequest.setUserName(userName);
        notifyPropertyChanged(BR.userName);
    }

    private void validateFromTime(String fromTime) {
        if (DateTimeUtils.convertStringToDate(fromTime)
                .after((DateTimeUtils.convertStringToDate(mToTime)))) {
            mDialogManager.dialogError(
                    mContext.getString(R.string.start_time_can_not_greater_than_end_time),
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog,
                                @NonNull DialogAction dialogAction) {
                            mDialogManager.showDatePickerDialog();
                        }
                    });
            return;
        }
        setFromTime(fromTime);
    }

    public void onPickTypeStatus(View view) {
        mDialogManager.dialogListSingleChoice(mContext.getString(R.string.status), R.array.status,
                mCurrentPositionStatus, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view,
                            int positionType, CharSequence charSequence) {
                        mCurrentPositionStatus = positionType;
                        mQueryRequest.setStatus(String.valueOf(mCurrentPositionStatus));
                        setCurrentStatus(String.valueOf(charSequence));
                        return true;
                    }
                });
    }

    public void onCickFromTime(View view) {
        mIsFromTimeSelected = true;
        mDialogManager.showDatePickerDialog();
    }

    public void onCickToTime(View view) {
        mIsFromTimeSelected = false;
        mDialogManager.showDatePickerDialog();
    }

    public void onClickFloatingButtonSearch(View view) {
        if (mIsVisibleLayoutSearch) {
            setVisibleLayoutSearch(false);
            return;
        }
        setVisibleLayoutSearch(true);
    }

    public void onClickClearDataFilter(View view) {
        setUserName(null);
        setCurrentStatus(null);
        mFromTime = DateTimeUtils.dayFirstMonthWorking();
        mToTime = DateTimeUtils.dayLasttMonthWorking();
        setFromTime(mFromTime);
        setToTime(mToTime);
        mQueryRequest.setStatus(null);
        mQueryRequest.setFromTime(mFromTime);
        mQueryRequest.setToTime(mToTime);
        mPresenter.getListAllRequestManage(mRequestType, mQueryRequest);
    }

    public void onClickSearch(View view) {
        mPresenter.getListAllRequestManage(mRequestType, mQueryRequest);
    }

    @Override
    public void onApproveOrRejectListener(@RequestType int requestType, int itemPosition,
            ActionRequestResponse actionRequestResponse) {
        updateItemRequest(requestType, itemPosition, actionRequestResponse);
    }

    @StringDef({ TypeRequest.LEAVE, TypeRequest.OFF, TypeRequest.OT })
    @interface TypeRequest {
        String LEAVE = "leave";
        String OFF = "off";
        String OT = "ot";
    }

    @IntDef({ TypeAction.APPROVE, TypeAction.REJECT })
    @interface TypeAction {
        int APPROVE = 0;
        int REJECT = 1;
    }
}
