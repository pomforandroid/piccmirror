package skankhunt.com.piccmirror.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import skankhunt.com.piccmirror.AppExecutors;
import skankhunt.com.piccmirror.api.ApiResponse;
import skankhunt.com.piccmirror.api.GithubService;
import skankhunt.com.piccmirror.db.UserDao;
import skankhunt.com.piccmirror.vo.Resource;
import skankhunt.com.piccmirror.vo.User;


/**
 * Created by skankhunt on 2018/1/22.
 */

@Singleton
public class UserRepository {

    private final UserDao userDao;
    private final GithubService githubService;
    private final AppExecutors appExecutors;

    @Inject
    UserRepository(AppExecutors appExecutors, UserDao userDao, GithubService githubService){
        this.userDao = userDao;
        this.appExecutors = appExecutors;
        this.githubService = githubService;
    }

    public LiveData<Resource<User>> loadUser(){

        return new NetworkBoundResource<User,User>(appExecutors){

            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);

            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data==null;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin() ;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser();
            }
        }.asLiveData();

    }


}
