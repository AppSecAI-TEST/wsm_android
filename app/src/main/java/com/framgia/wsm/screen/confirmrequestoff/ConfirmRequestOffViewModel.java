package com.framgia.wsm.screen.confirmrequestoff;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.OffRequest;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.data.source.remote.api.request.RequestOffRequest;
import com.framgia.wsm.screen.requestoff.RequestOffActivity;
import com.framgia.wsm.utils.ActionType;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.RequestType;
import com.framgia.wsm.utils.StatusCode;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import com.fstyle.library.DialogAction;
import com.fstyle.library.MaterialDialog;

/**
 * Exposes the data to be used in the ConfirmRequestOff screen.
 */

public class ConfirmRequestOffViewModel extends BaseObservable
        implements ConfirmRequestOffContract.ViewModel {

    private static final String TAG = "ConfirmRequestOffViewModel";

    private ConfirmRequestOffContract.Presenter mPresenter;
    private Navigator mNavigator;
    private DialogManager mDialogManager;
    private User mUser;
    private OffRequest mOffRequest;
    private RequestOffRequest mRequestOffRequest;
    private int mActionType;
    private Context mContext;

    ConfirmRequestOffViewModel(Context context, ConfirmRequestOffContract.Presenter presenter,
            Navigator navigator, DialogManager dialogManager, OffRequest requestOff,
            int actionType) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mDialogManager = dialogManager;
        mOffRequest = requestOff;
        initData(mOffRequest);
        mNavigator = navigator;
        mPresenter.getUser();
        mActionType = actionType;
        mRequestOffRequest = new RequestOffRequest();
    }

    private void initData(OffRequest offRequest) {
        if (offRequest.getStartDayHaveSalary().getOffPaidFrom() == null) {
            OffRequest.OffHaveSalaryFrom offHaveSalaryFrom = new OffRequest.OffHaveSalaryFrom();
            offHaveSalaryFrom.setOffPaidFrom("");
            offHaveSalaryFrom.setPaidFromPeriod("");
            mOffRequest.setStartDayHaveSalary(offHaveSalaryFrom);

            OffRequest.OffHaveSalaryTo offHaveSalaryTo = new OffRequest.OffHaveSalaryTo();
            offHaveSalaryTo.setOffPaidTo("");
            offHaveSalaryTo.setPaidToPeriod("");
            mOffRequest.setEndDayHaveSalary(offHaveSalaryTo);
        }
        if (offRequest.getStartDayNoSalary().getOffFrom() == null) {
            OffRequest.OffNoSalaryFrom offNoSalaryFrom = new OffRequest.OffNoSalaryFrom();
            offNoSalaryFrom.setOffFrom("");
            offNoSalaryFrom.setOffFromPeriod("");
            mOffRequest.setStartDayNoSalary(offNoSalaryFrom);

            OffRequest.OffNoSalaryTo offNoSalaryTo = new OffRequest.OffNoSalaryTo();
            offNoSalaryTo.setOffTo("");
            offNoSalaryTo.setOffToPeriod("");
            mOffRequest.setEndDayNoSalary(offNoSalaryTo);
        }
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
    public void onCreateFormRequestOffSuccess() {
        mNavigator.finishActivityWithResult(Activity.RESULT_OK);
    }

    @Override
    public void onCreateFormFormRequestOffError(BaseException exception) {
        mDialogManager.dialogError(exception);
    }

    @Override
    public void onGetUserSuccess(User user) {
        if (user == null) {
            return;
        }
        mUser = user;
        notifyPropertyChanged(BR.user);
    }

    @Override
    public void onGetUserError(BaseException e) {
        Log.e(TAG, "ConfirmRequestOffViewModel", e);
    }

    @Override
    public void onDeleteFormRequestOffSuccess() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_REQUEST_TYPE_CODE, RequestType.REQUEST_OFF);
        mNavigator.finishActivityWithResult(bundle, Activity.RESULT_OK);
    }

    @Override
    public void onDeleteFormRequestOffError(BaseException exception) {
        mDialogManager.dialogError(exception);
    }

    @Override
    public void onEditFormRequestOffSuccess(OffRequest requestOff) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_REQUEST_TYPE_CODE, RequestType.REQUEST_OFF);
        mNavigator.finishActivityWithResult(bundle, Activity.RESULT_OK);
    }

    @Override
    public void onEditFormRequestOffError(BaseException exception) {
        mDialogManager.dialogError(exception, new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog,
                    @NonNull DialogAction dialogAction) {
                mNavigator.finishActivity();
            }
        });
    }

    @Override
    public void onDismissProgressDialog() {
        mDialogManager.dismissProgressDialog();
    }

    @Override
    public void onShowIndeterminateProgressDialog() {
        mDialogManager.showIndeterminateProgressDialog();
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    @Bindable
    public OffRequest getRequestOff() {
        return mOffRequest;
    }

    @Bindable
    public boolean isVisiableProjectName() {
        return mOffRequest.getProject() != null;
    }

    @Bindable
    public boolean isVisiablePosition() {
        return mOffRequest.getPosition() != null;
    }

    @Bindable
    public boolean isVisiableLayoutCompanyPay() {
        return mOffRequest.getAnnualLeave() != null
                || mOffRequest.getLeaveForChildMarriage() != null
                || mOffRequest.getLeaveForMarriage() != null
                || mOffRequest.getFuneralLeave() != null;
    }

    @Bindable
    public boolean isVisiableLayoutInsurance() {
        return mOffRequest.getLeaveForCareOfSickChild() != null
                || mOffRequest.getSickLeave() != null
                || mOffRequest.getMaternityLeave() != null
                || mOffRequest.getPregnancyExaminationLeave() != null
                || mOffRequest.getMiscarriageLeave() != null
                || mOffRequest.getWifeLaborLeave() != null;
    }

    @Bindable
    public boolean isVisiableLayoutHaveSalary() {
        return !"".equals(mOffRequest.getStartDayHaveSalary().getOffPaidFrom());
    }

    @Bindable
    public boolean isVisiableLayoutOffNoSalary() {
        return !"".equals(mOffRequest.getStartDayNoSalary().getOffFrom());
    }

    @Bindable
    public boolean isVisiableAnnualLeave() {
        return mOffRequest.getAnnualLeave() != null && !"".equals(mOffRequest.getAnnualLeave());
    }

    @Bindable
    public boolean isVisiableLeaveForChildMarriage() {
        return mOffRequest.getLeaveForChildMarriage() != null && !"".equals(
                mOffRequest.getLeaveForChildMarriage());
    }

    @Bindable
    public boolean isVisiableLeaveForMarriage() {
        return mOffRequest.getLeaveForMarriage() != null && !"".equals(
                mOffRequest.getLeaveForMarriage());
    }

    @Bindable
    public boolean isVisiableFuneralLeave() {
        return mOffRequest.getFuneralLeave() != null && !"".equals(mOffRequest.getFuneralLeave());
    }

    @Bindable
    public boolean isVisiableLeaveForCareOfSickChild() {
        return mOffRequest.getLeaveForCareOfSickChild() != null && !"".equals(
                mOffRequest.getLeaveForCareOfSickChild());
    }

    @Bindable
    public boolean isVisiableSickLeave() {
        return mOffRequest.getSickLeave() != null && !"".equals(mOffRequest.getSickLeave());
    }

    @Bindable
    public boolean isVisiableMaternityLeave() {
        return mOffRequest.getMaternityLeave() != null && !"".equals(
                mOffRequest.getMaternityLeave());
    }

    @Bindable
    public boolean isVisiablePregnancyExaminationLeave() {
        return mOffRequest.getPregnancyExaminationLeave() != null && !"".equals(
                mOffRequest.getPregnancyExaminationLeave());
    }

    @Bindable
    public boolean isVisiableMiscarriageLeave() {
        return mOffRequest.getMiscarriageLeave() != null && !"".equals(
                mOffRequest.getMiscarriageLeave());
    }

    @Bindable
    public boolean isVisiableWifeLaborLeave() {
        return mOffRequest.getWifeLaborLeave() != null && !"".equals(
                mOffRequest.getWifeLaborLeave());
    }

    public boolean isDetail() {
        return mActionType == ActionType.ACTION_DETAIL;
    }

    public boolean isAcceptStatus() {
        return StatusCode.ACCEPT_CODE.equals(mOffRequest.getStatus());
    }

    public boolean isPendingStatus() {
        return StatusCode.PENDING_CODE.equals(mOffRequest.getStatus());
    }

    public boolean isRejectStatus() {
        return StatusCode.REJECT_CODE.equals(mOffRequest.getStatus());
    }

    public boolean isForwardStatus() {
        return StatusCode.FORWARD_CODE.equals(mOffRequest.getStatus());
    }

    public String getTitleToolbar() {
        if (mActionType == ActionType.ACTION_CREATE) {
            return mContext.getString(R.string.confirm_request_off);
        }
        if (mActionType == ActionType.ACTION_DETAIL) {
            return mContext.getString(R.string.detail_request_off);
        }
        return mContext.getString(R.string.confirm_edit_request_off);
    }

    public String getStartDateHaveSalary() {
        if (mOffRequest != null && mOffRequest.getStartDayHaveSalary() != null) {
            if (!"".equals(mOffRequest.getStartDayHaveSalary().getOffPaidFrom())) {
                return mOffRequest.getStartDayHaveSalary().getOffPaidFrom()
                        + Constant.BLANK
                        + mOffRequest.getStartDayHaveSalary().getPaidFromPeriod();
            }
        }
        return "";
    }

    public String getEndDateHaveSalary() {
        if (mOffRequest != null && mOffRequest.getEndDayHaveSalary() != null) {
            if (!"".equals(mOffRequest.getEndDayHaveSalary().getOffPaidTo())) {
                return mOffRequest.getEndDayHaveSalary().getOffPaidTo()
                        + Constant.BLANK
                        + mOffRequest.getEndDayHaveSalary().getPaidToPeriod();
            }
        }
        return "";
    }

    public String getStartDateNoSalary() {
        if (mOffRequest != null && mOffRequest.getStartDayNoSalary() != null) {
            if (!"".equals(mOffRequest.getStartDayNoSalary().getOffFrom())) {
                return mOffRequest.getStartDayNoSalary().getOffFrom() + Constant.BLANK + mOffRequest
                        .getStartDayNoSalary()
                        .getOffFromPeriod();
            }
        }
        return "";
    }

    public String getEndDateNoSalary() {
        if (mOffRequest != null && mOffRequest.getEndDayNoSalary() != null) {
            if (!"".equals(mOffRequest.getEndDayNoSalary().getOffTo())) {
                return mOffRequest.getEndDayNoSalary().getOffTo()
                        + Constant.BLANK
                        + mOffRequest.getEndDayNoSalary().getOffToPeriod();
            }
        }
        return "";
    }

    public boolean isVisibleButtonSubmit() {
        return mActionType == ActionType.ACTION_CREATE || mOffRequest.getStatus()
                .equals(StatusCode.PENDING_CODE);
    }

    public void onCickArrowBack(View view) {
        mNavigator.finishActivity();
    }

    public void onClickSubmit(View view) {
        if (mOffRequest == null) {
            return;
        }
        mRequestOffRequest.setRequestOff(mOffRequest);
        if (mActionType == ActionType.ACTION_CREATE) {
            mPresenter.createFormRequestOff(mRequestOffRequest);
            return;
        }
        mPresenter.editFormRequestOff(mRequestOffRequest);
    }

    public void onClickDelete(View view) {
        if (mOffRequest == null) {
            return;
        }
        mDialogManager.dialogBasic(mContext.getString(R.string.confirm_delete),
                mContext.getString(R.string.do_you_want_delete_this_request),
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog,
                            @NonNull DialogAction dialogAction) {
                        mPresenter.deleteFormRequestOff(mOffRequest.getId());
                    }
                });
    }

    public void onClickEdit(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.EXTRA_ACTION_TYPE, ActionType.ACTION_EDIT);
        bundle.putParcelable(Constant.EXTRA_REQUEST_OFF, mOffRequest);
        mNavigator.startActivityForResult(RequestOffActivity.class, bundle,
                Constant.RequestCode.REQUEST_OFF);
    }
}
