package com.framgia.wsm.screen.updateprofile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import com.framgia.wsm.BR;
import com.framgia.wsm.R;
import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.error.BaseException;
import com.framgia.wsm.data.source.remote.api.request.UpdateProfileRequest;
import com.framgia.wsm.utils.Constant;
import com.framgia.wsm.utils.FileUtils;
import com.framgia.wsm.utils.RequestPermissionManager;
import com.framgia.wsm.utils.common.DateTimeUtils;
import com.framgia.wsm.utils.navigator.Navigator;
import com.framgia.wsm.widget.dialog.DialogManager;
import java.io.File;

/**
 * Exposes the data to be used in the Updateprofile screen.
 */

public class UpdateProfileViewModel extends BaseObservable
        implements UpdateProfileContract.ViewModel, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "UpdateProfileViewModel";

    private UpdateProfileContract.Presenter mPresenter;

    private Context mContext;
    private User mUser;
    private DialogManager mDialogManager;
    private Navigator mNavigator;
    private String mEmployeeNameError;
    private String mBirthdayError;
    private String mAvatar;
    private String mBirthday;
    private RequestPermissionManager mPermissionManager;
    private UpdateProfileRequest mUpdateProfileRequest;
    private User mUserLocal;

    UpdateProfileViewModel(Context context, User user, Navigator navigator,
            DialogManager dialogManager, UpdateProfileContract.Presenter presenter,
            RequestPermissionManager requestPermissionManager) {
        mContext = context;
        mPresenter = presenter;
        mPresenter.setViewModel(this);
        mUser = user;
        mNavigator = navigator;
        mDialogManager = dialogManager;
        mDialogManager.dialogDatePicker(this);
        mPermissionManager = requestPermissionManager;
        initData(mUser);
    }

    private void initData(User user) {
        mUpdateProfileRequest = new UpdateProfileRequest();
        mUpdateProfileRequest.setBirthday(mUser.getBirthday());
        mAvatar = Constant.END_POINT_URL + mUser.getAvatar();
        mUserLocal = new User();
        mBirthday = DateTimeUtils.convertUiFormatToDataFormat(user.getBirthday(),
                DateTimeUtils.INPUT_TIME_FORMAT, DateTimeUtils.FORMAT_DATE);
        mPresenter.getUserLocal();
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
    public void onDismissProgressDialog() {
        mDialogManager.dismissProgressDialog();
    }

    @Override
    public void onShowIndeterminateProgressDialog() {
        mDialogManager.showIndeterminateProgressDialog();
    }

    @Override
    public void onGetUserLocalSuccess(User userLocal) {
        if (userLocal == null) {
            return;
        }
        mUserLocal = userLocal;
    }

    @Override
    public void onGetUserLocalError(BaseException exception) {
        Log.e(TAG, "onGetUserError", exception);
    }

    @Bindable
    public User getUser() {
        return mUser;
    }

    @Override
    public void onInputEmployeeNameError(String employeeName) {
        mEmployeeNameError = employeeName;
        notifyPropertyChanged(BR.employeeNameError);
    }

    @Override
    public void onInputBirthdayError(String birthday) {
        mBirthdayError = birthday;
        notifyPropertyChanged(BR.birthdayError);
    }

    @Override
    public void onUpdateProfileSuccess(User user) {
        mUserLocal.setBirthday(user.getBirthday());
        mUserLocal.setAvatar(user.getAvatar());
        mUserLocal.setName(user.getName());
        mPresenter.updateUserLocal(mUserLocal);
        mNavigator.finishActivityWithResult(Activity.RESULT_OK);
    }

    @Override
    public void onUpdateProfileError(BaseException exception) {
        mDialogManager.dialogError(exception);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = DateTimeUtils.convertDateToString(year, month, dayOfMonth);
        setBirthday(date);
    }

    @Override
    public void setAvatarUser(Uri avatarUser) {
        mAvatar = avatarUser.toString();
        File file = new File(FileUtils.getRealPathFromURI(mContext, avatarUser));
        mUpdateProfileRequest.setAvatar(file);
        notifyPropertyChanged(BR.avatar);
    }

    @Override
    public void pickAvatarUser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mNavigator.startActivityForResult(intent, Constant.RequestCode.REQUEST_SELECT_AVATAR);
    }

    @Bindable
    public String getBirthday() {
        return mBirthday;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
        mUpdateProfileRequest.setBirthday(birthday);
        notifyPropertyChanged(BR.birthday);
    }

    @Bindable
    public String getEmployeeNameError() {
        return mEmployeeNameError;
    }

    @Bindable
    public String getBirthdayError() {
        return mBirthdayError;
    }

    @Bindable
    public String getAvatar() {
        return mAvatar;
    }

    public void onClickArrowBack(View view) {
        mNavigator.finishActivity();
    }

    public void onPickDate(View view) {
        mDialogManager.showDatePickerDialog();
    }

    public void onClickPickAvatar(View view) {
        if (mPermissionManager.isPermissionWriteExternalStorageGranted()) {
            pickAvatarUser();
        }
    }

    public void onCickSubmit(View view) {
        if (mUser == null) {
            return;
        }
        if (!mPresenter.validateData(mUser)) {
            mDialogManager.dialogError(
                    mContext.getString(R.string.the_field_required_can_not_be_blank));
            return;
        }
        mUpdateProfileRequest.setUserId(mUser.getId());
        mUpdateProfileRequest.setName(mUser.getName());
        mPresenter.updateProfile(mUpdateProfileRequest);
    }
}
