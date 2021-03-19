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

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;

import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends RoboActivity {

	@InjectView(R.id.skill)
	private EditText skillEditText;

	@InjectView(R.id.location)
	private EditText locationEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_main);

		this.setClearOnClick(this.skillEditText);
		this.setClearOnClick(this.locationEditText);

	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void setClearOnClick(final EditText editText) {
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					editText.setText("");
				}
			}
		});
	}

	public void executeSearch(View view) {

		String skill = getEditTextValue(this.skillEditText);
		String location = getEditTextValue(this.locationEditText);

		Intent searchIntent = new Intent(this, JobResultsActivity.class);

		searchIntent.putExtra(ExtraKey.SKILL_KEY, skill);
		searchIntent.putExtra(ExtraKey.LOCATION_KEY, location);

		this.startActivity(searchIntent);

	}

	private static String getEditTextValue(EditText editText) {
		Editable editableText = editText.getText();
		if (editableText == null) {
			return "";
		}
		return editableText.toString();
	}

}
