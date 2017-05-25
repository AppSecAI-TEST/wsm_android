package com.framgia.wsm.data.source;

import com.framgia.wsm.data.model.User;
import com.framgia.wsm.data.source.remote.api.response.UserResponse;
import io.reactivex.Observable;

/**
 * Created by le.quang.dao on 10/03/2017.
 */

public interface UserDataSource {
    /**
     * LocalData For User
     */
    interface LocalDataSource extends BaseLocalDataSource {
        void saveUser(User user);

        Observable<User> getUser();

        void clearData();
    }

    /**
     * RemoteData For User
     */
    interface RemoteDataSource {
        Observable<UserResponse> login(String userName, String password);
    }
}
