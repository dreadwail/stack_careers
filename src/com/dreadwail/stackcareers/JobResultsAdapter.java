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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JobResultsAdapter extends ArrayAdapter<JobListing> {

	private final LayoutInflater inflater;
	private final int rowViewId;
	private final List<JobListing> listings;

	public JobResultsAdapter(Context context, int rowViewId, List<JobListing> listings) {
		super(context, rowViewId, listings);
		this.inflater = LayoutInflater.from(context);
		this.rowViewId = rowViewId;
		this.listings = listings;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		JobListing jobListing = this.listings.get(position);
		
		View rowView = this.inflater.inflate(this.rowViewId, null);
		TextView titleTextView = (TextView)rowView.findViewById(R.id.job_listing_title);
		titleTextView.setText(jobListing.getTitle());
		titleTextView.setContentDescription(jobListing.getTitle());
		
		return rowView;
		
	}

}