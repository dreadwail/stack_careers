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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreadwail.android.Connectivity;
import com.dreadwail.stackcareers.feed.CareerFeedClient;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.inject.Inject;

public class JobResultsActivity extends RoboActivity {

	private static final int SEARCH_RADIUS_MILES = 20;
	
	@InjectView(R.id.search_results) 
	private ListView searchResults;
	@InjectView(R.id.search_progress) 
	private ProgressBar searchProgress;
	@InjectView(R.id.results_label) 
	private TextView resultsLabel;
	
	@Inject CareerFeedClient client;
	private final ArrayList<JobListing> listings;
	
	public JobResultsActivity() {
		this.listings = new ArrayList<JobListing>();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_jobresults);

		if(!Connectivity.isConnected(this)) {
			Toast
				.makeText(this, "NO INTERNET CONNECTION.", Toast.LENGTH_LONG)
				.show();
			return;
		}

		this.executeSearch();

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
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(ExtraKey.JOB_RESULTS, this.listings);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		super.onRestoreInstanceState(savedInstanceState);
		Serializable serializable = savedInstanceState.getSerializable(ExtraKey.JOB_RESULTS);
		if(serializable == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		ArrayList<JobListing> previousJobListings = (ArrayList<JobListing>)serializable;
		this.loadNewListings(previousJobListings);
		
	}

	private void executeSearch() {
		
		SearchQuery query = this.getQuery();
		
		SearchExecutor searchExecutor = new SearchExecutor();
		searchExecutor.execute(query);

	}

	private SearchQuery getQuery() {
		
		Intent intent = this.getIntent();

		String skill = intent.getStringExtra(ExtraKey.SKILL_KEY);
		String location = intent.getStringExtra(ExtraKey.LOCATION_KEY);
		int radiusMiles = intent.getIntExtra(ExtraKey.RADIUS_MILES_KEY, SEARCH_RADIUS_MILES);
		
		return new SearchQuery(skill, location, radiusMiles);
		
	}

	private class SearchExecutor extends AsyncTask<SearchQuery, Integer, List<JobListing>> {

		@Override
		protected List<JobListing> doInBackground(SearchQuery... queries) {
			
			List<JobListing> listings = new ArrayList<JobListing>();
			
			if(queries == null || queries.length == 0) {
				return listings;
			}
			
			for(SearchQuery query : queries) {
				List<JobListing> listingsForQuery = client.search(query);
				listings.addAll(listingsForQuery);
			}
			
			return listings;

		}

		@Override
		protected void onPostExecute(final List<JobListing> listings) {
			loadNewListings(listings);
		}

	}
	
	private void loadNewListings(List<JobListing> newListings) {
		
		this.listings.clear();
		this.listings.addAll(newListings);

		JobResultsAdapter adapter = new JobResultsAdapter(this, R.layout.result_row, listings);

		this.searchResults.setAdapter(adapter);
		this.searchResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View listView, int position, long id) {
				JobListing jobListing = listings.get(position);
				Intent jobListingIntent = new Intent(JobResultsActivity.this, JobActivity.class);
				jobListingIntent.putExtra(ExtraKey.JOB, jobListing);
				startActivity(jobListingIntent);
			}
		});
		
		if(this.listings.size() == 0) {
			this.resultsLabel.setText("No results!");
		} else {
			this.resultsLabel.setVisibility(View.INVISIBLE);
		}
		
		this.searchProgress.setVisibility(View.INVISIBLE);

	}

}
