/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package skankhunt.com.piccmirror.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import skankhunt.com.piccmirror.api.GithubService;
import skankhunt.com.piccmirror.db.GithubDb;
import skankhunt.com.piccmirror.db.LoginDao;
import skankhunt.com.piccmirror.db.UserDao;
import skankhunt.com.piccmirror.util.LiveDataCallAdapterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton @Provides
    GithubService provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl("http://wxplay.swzcn.com/hello/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    @Singleton @Provides
    GithubDb provideDb(Application app) {
        return Room.databaseBuilder(app, GithubDb.class,"github.db")
                .fallbackToDestructiveMigration() //更新版本后清空数据库
                .build();
    }
    @Singleton @Provides
    UserDao provideUserDao(GithubDb db) {
        return db.userDao();
    }

    @Singleton @Provides
    LoginDao provideLoginDao(GithubDb db) {
        return db.loginDao();
    }

   /* @Singleton @Provides
    UserDao provideUserDao(GithubDb db) {
        return db.userDao();
    }

    @Singleton @Provides
    RepoDao provideRepoDao(GithubDb db) {
        return db.repoDao();
    }*/
}
