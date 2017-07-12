package com.framgia.wsm.data.source;

import com.framgia.wsm.data.model.LeaveRequest;
import com.framgia.wsm.data.model.OffRequest;
import com.framgia.wsm.data.model.RequestOverTime;
import com.framgia.wsm.data.source.remote.api.request.RequestLeaveRequest;
import com.framgia.wsm.data.source.remote.api.request.RequestOffRequest;
import com.framgia.wsm.data.source.remote.api.response.BaseResponse;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import java.util.List;

/**
 * Created by tri on 12/06/2017.
 */

public interface RequestDataSource {
    interface RemoteDataSource {
        Observable<Object> createFormRequestOverTime(@NonNull RequestOverTime requestOverTime);

        Observable<Object> createFormRequestOff(@NonNull RequestOffRequest requestOffRequest);

        Observable<Object> createFormRequestLeave(@NonNull RequestLeaveRequest requestLeaveRequest);

        Observable<BaseResponse<List<RequestOverTime>>> getListRequestOverTime();

        Observable<BaseResponse<List<OffRequest>>> getListRequestOff();

        Observable<BaseResponse<List<LeaveRequest>>> getListRequestLateEarly();

        Observable<BaseResponse<List<RequestOverTime>>> getListRequestOverTimeWithStatusAndTime(
                @NonNull int status, String time);

        Observable<BaseResponse<List<LeaveRequest>>> getListRequestLeaveWithStatusAndTime(
                @NonNull int status, String time);

        Observable<BaseResponse<List<LeaveRequest>>> getListRequestOffWithStatusAndTime(
                @NonNull int status, String time);

        Observable<Object> deleteFormRequestOff(@NonNull int requestOffId);

        Observable<BaseResponse<OffRequest>> editFormRequestOff(
                @NonNull RequestOffRequest requestOffRequest);

        Observable<BaseResponse<RequestOverTime>> editFormRequestOverTime(
                @NonNull RequestOverTime requestOverTime);

        Observable<Object> deleteFormRequestOverTime(@NonNull int requestOverTimeId);

        Observable<Object> deleteFormRequestLeave(@NonNull int requestLeaveId);

        Observable<Object> editFormRequestLeave(@NonNull int requestId,
                RequestLeaveRequest requestLeave);

        Observable<BaseResponse<List<LeaveRequest>>> getListRequesLeavetManage(String fromTime,
                String toTime);

        Observable<BaseResponse<List<RequestOverTime>>> getListRequesOvertimetManage(
                String fromTime, String toTime);

        Observable<BaseResponse<List<OffRequest>>> getListRequesOffManage(String fromTime,
                String toTime);

        Observable<BaseResponse<LeaveRequest>> approveRequestLeave(int requestId);

        Observable<BaseResponse<OffRequest>> approveRequestOff(int requestId);

        Observable<BaseResponse<RequestOverTime>> approveRequestOverTime(int requestId);

        Observable<BaseResponse<LeaveRequest>> rejectRequestLeave(int requestId);

        Observable<BaseResponse<OffRequest>> rejectRequestOff(int requestId);

        Observable<BaseResponse<RequestOverTime>> rejectRequestOverTime(int requestId);
    }
}
