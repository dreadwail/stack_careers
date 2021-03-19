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
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class JobActivity extends RoboActivity {
	
	private static final String SHARE_FORMAT = "%s '%s' %s";

	@InjectView(R.id.job_listing_title) 
	private TextView titleTextView;
	@InjectView(R.id.job_listing_uri) 
	private TextView uriTextView;
	@InjectView(R.id.job_listing_postedtime) 
	private TextView postedTimeTextView;
	@InjectView(R.id.job_listing_updatedtime) 
	private TextView updatedTimeTextView;
	@InjectView(R.id.job_listing_description) 
	private TextView descriptionTextView;
	
	private JobListing listing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_job);

		Intent intent = this.getIntent();
		this.listing = (JobListing)intent.getSerializableExtra(ExtraKey.JOB);

		this.setTitle();
		this.setUri();
		this.setPostedTime();
		this.setUpdatedTime();
		this.setDescription();

		Linkify.addLinks(this.descriptionTextView, Linkify.ALL);
		Linkify.addLinks(this.uriTextView, Linkify.ALL);
		
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
	
	private void setTitle() {
		String titleText = this.listing.getTitle();
		this.titleTextView.setText(titleText);
		this.titleTextView.setContentDescription(titleText);
	}
	
	private void setUri() {
		String uriText = this.getString(R.string.web_link) + "\n" + this.listing.getUri();
		this.uriTextView.setText(uriText);
		this.uriTextView.setContentDescription(this.getString(R.string.web_link));
	}
	
	private void setPostedTime() {
		String postedTimeText = this.getString(R.string.posted_on) + " " + this.listing.getPostedDateTime();
		this.postedTimeTextView.setText(postedTimeText);
		this.postedTimeTextView.setContentDescription(this.getString(R.string.posted_on));
	}
	
	private void setUpdatedTime() {
		String updatedTimeText = this.getString(R.string.last_updated) + " " + this.listing.getUpdatedDateTime();
		this.updatedTimeTextView.setText(updatedTimeText);
		this.updatedTimeTextView.setContentDescription(this.getString(R.string.last_updated));
	}
	
	private void setDescription() {
		String descriptionLabel = this.getString(R.string.description_label);
		String encodedDescriptionMarkup = this.listing.getEncodedDescriptionMarkup();
		Spanned description = Html.fromHtml(encodedDescriptionMarkup);
		this.descriptionTextView.setText(description);
		this.descriptionTextView.setContentDescription(descriptionLabel);
	}

	public void share(View buttonView) {

		String blurb = this.getString(R.string.share_blurb);
		
		Intent shareIntent = new Intent();
		
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.share_blurb));
		shareIntent.putExtra(Intent.EXTRA_TEXT, 
				String.format(SHARE_FORMAT, blurb, this.titleTextView.getText(), this.uriTextView.getText()));

		shareIntent.setType("text/plain");
		
		CharSequence chooserTitle = this.getResources().getText(R.string.share_job);
		this.startActivity(Intent.createChooser(shareIntent, chooserTitle));
		
	}

}
