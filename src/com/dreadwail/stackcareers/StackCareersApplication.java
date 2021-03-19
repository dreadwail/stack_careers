package com.dreadwail.stackcareers;

/*
Copyright 2012 Dreadwail

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import roboguice.RoboGuice;
import android.app.Application;

import com.google.inject.Module;

public class StackCareersApplication extends Application {

	@Override
	public void onCreate() {

		super.onCreate();
		
		Module roboGuiceInjectionModule = RoboGuice.newDefaultRoboModule(this);
		Module stackCareersInjectionModule = new InjectionModule();

		RoboGuice.setBaseApplicationInjector(
				this, RoboGuice.DEFAULT_STAGE, roboGuiceInjectionModule, stackCareersInjectionModule);

	}

}
