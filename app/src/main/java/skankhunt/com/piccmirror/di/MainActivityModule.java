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



import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import skankhunt.com.piccmirror.MainActivity;


/**
 * 自动配置注解到activity
 */
@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

//    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
//    abstract OldMainActivity contributeOldMainActivity();
//
//    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
//    abstract LoginActivity contributeLoginActivity();
}
