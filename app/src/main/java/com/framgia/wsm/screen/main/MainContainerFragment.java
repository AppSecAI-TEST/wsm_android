package com.framgia.wsm.screen.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.wsm.R;
import com.framgia.wsm.screen.BaseFragment;
import com.framgia.wsm.screen.holidaycalendar.HolidayCalendarFragment;
import com.framgia.wsm.screen.listrequest.ListRequestFragment;
import com.framgia.wsm.screen.managelistrequests.ManageListRequestsFragment;
import com.framgia.wsm.screen.profile.ProfileFragment;
import com.framgia.wsm.screen.setting.SettingProfileFragment;
import com.framgia.wsm.screen.statistics.StatisticsFragment;
import com.framgia.wsm.screen.timesheet.TimeSheetFragment;
import com.framgia.wsm.utils.RequestType;
import com.framgia.wsm.utils.navigator.NavigateAnim;
import com.framgia.wsm.utils.navigator.Navigator;

import static com.framgia.wsm.screen.main.MainViewModel.Page.COME_LATE_LEAVE_EARLY;
import static com.framgia.wsm.screen.main.MainViewModel.Page.HOLIDAY_CALENDAR;
import static com.framgia.wsm.screen.main.MainViewModel.Page.MANAGE_COME_LATE_LEAVE_EARLY;
import static com.framgia.wsm.screen.main.MainViewModel.Page.MANAGE_OFF;
import static com.framgia.wsm.screen.main.MainViewModel.Page.MANAGE_OVERTIME;
import static com.framgia.wsm.screen.main.MainViewModel.Page.OFF;
import static com.framgia.wsm.screen.main.MainViewModel.Page.OVERTIME;
import static com.framgia.wsm.screen.main.MainViewModel.Page.PERSONAL;
import static com.framgia.wsm.screen.main.MainViewModel.Page.SETUP_PROFILE;
import static com.framgia.wsm.screen.main.MainViewModel.Page.STATISTIC_OF_PERSONAL;
import static com.framgia.wsm.screen.main.MainViewModel.Page.WORKING_CALENDAR;
import static com.framgia.wsm.screen.main.MainViewModel.Page.WORKSPACE_DATA;

/**
 * Created by tri on 25/05/2017.
 */

public class MainContainerFragment extends BaseFragment {
    private static final String EXTRA_PAGE = "EXTRA_ITEM";
    private Navigator mNavigator;

    public static MainContainerFragment newInstance(@MainViewModel.Page int item) {
        MainContainerFragment fragment = new MainContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PAGE, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        mNavigator = new Navigator(this);
        int containerViewId = R.id.layout_container;

        @MainViewModel.Page int page = getArguments().getInt(EXTRA_PAGE);
        switch (page) {
            case PERSONAL:
                mNavigator.goNextChildFragment(containerViewId, ProfileFragment.newInstance(),
                        false, NavigateAnim.NONE, ProfileFragment.TAG);
                break;
            case SETUP_PROFILE:
                mNavigator.goNextChildFragment(containerViewId,
                        SettingProfileFragment.newInstance(), false, NavigateAnim.NONE,
                        SettingProfileFragment.TAG);
                break;
            case WORKING_CALENDAR:
                mNavigator.goNextChildFragment(containerViewId, TimeSheetFragment.newInstance(),
                        false, NavigateAnim.NONE, TimeSheetFragment.TAG);
                break;
            case HOLIDAY_CALENDAR:
                mNavigator.goNextChildFragment(containerViewId,
                        HolidayCalendarFragment.newInstance(), false, NavigateAnim.NONE,
                        HolidayCalendarFragment.TAG);
                break;
            case STATISTIC_OF_PERSONAL:
                mNavigator.goNextChildFragment(containerViewId, StatisticsFragment.newInstance(),
                        false, NavigateAnim.NONE, StatisticsFragment.TAG);
                break;
            case OVERTIME:
                mNavigator.goNextChildFragment(containerViewId,
                        ListRequestFragment.newInstance(RequestType.REQUEST_OVERTIME), false,
                        NavigateAnim.NONE, ListRequestFragment.TAG);
                break;
            case OFF:
                mNavigator.goNextChildFragment(containerViewId,
                        ListRequestFragment.newInstance(RequestType.REQUEST_OFF), false,
                        NavigateAnim.NONE, ListRequestFragment.TAG);
                break;
            case COME_LATE_LEAVE_EARLY:
                mNavigator.goNextChildFragment(containerViewId,
                        ListRequestFragment.newInstance(RequestType.REQUEST_LATE_EARLY), false,
                        NavigateAnim.NONE, ListRequestFragment.TAG);
                break;
            case WORKSPACE_DATA:
                break;
            case MANAGE_OVERTIME:
                mNavigator.goNextChildFragment(containerViewId,
                        ManageListRequestsFragment.newInstance(RequestType.REQUEST_OVERTIME), false,
                        NavigateAnim.NONE, ManageListRequestsFragment.TAG);
                break;
            case MANAGE_OFF:
                mNavigator.goNextChildFragment(containerViewId,
                        ManageListRequestsFragment.newInstance(RequestType.REQUEST_OFF), false,
                        NavigateAnim.NONE, ManageListRequestsFragment.TAG);
                break;
            case MANAGE_COME_LATE_LEAVE_EARLY:
                mNavigator.goNextChildFragment(containerViewId,
                        ManageListRequestsFragment.newInstance(RequestType.REQUEST_LATE_EARLY),
                        false, NavigateAnim.NONE, ManageListRequestsFragment.TAG);
                break;
            default:
                break;
        }
        return view;
    }

    public boolean onBackPressed() {
        return mNavigator.goBackChildFragment();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isAdded()) {
            return;
        }
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.layout_container);
        if (fragment == null) {
            return;
        }
        fragment.setUserVisibleHint(isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }
}
