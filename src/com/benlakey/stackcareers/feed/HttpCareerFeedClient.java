package com.benlakey.stackcareers.feed;

/*
Copyright 2012 Ben Lakey

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.benlakey.android.Connectivity;
import com.benlakey.android.LogTag;
import com.benlakey.stackcareers.JobListing;
import com.benlakey.stackcareers.SearchQuery;
import com.google.inject.Inject;

public class HttpCareerFeedClient implements CareerFeedClient {

	private static final int CONNECT_TIMEOUT_MILLIS = 7000;
	private static final int READ_TIMEOUT_MILLIS = 7000;

	private final HttpSearchRequestBuilder requestBuilder;
	private final HttpSearchResponseParser responseParser;

	@Inject
	public HttpCareerFeedClient(
			HttpSearchRequestBuilder requestBuilder, 
			HttpSearchResponseParser responseParser) {
		this.requestBuilder = requestBuilder;
		this.responseParser = responseParser;
		Connectivity.disableConnectionReuseIfNecessary();
	}

	@Override
	public List<JobListing> search(SearchQuery query) {

		URL url = this.constructURL(query);
		if(url == null) {
			return Collections.emptyList();
		}
		
		InputStream inputStream = this.fetchData(url);
		if(inputStream == null) {
			return Collections.emptyList();
		}

		return this.responseParser.parse(inputStream);

	}
	
	private URL constructURL(SearchQuery query) {
		try {
			return this.requestBuilder
				.setLocation(query.getLocation())
				.setSkill(query.getSkill())
				.build();
		} catch (MalformedURLException e) {
			Log.e(LogTag.ERROR_TAG, "URL couldn't be constructed for the query data.", e);
			return null;
		}
	}
	
	private InputStream fetchData(URL url) {

		try {
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			this.configureConnection(connection);
			connection.connect();
			
			int responseCode = connection.getResponseCode();
			if(responseCode != 200) {
				Log.e(LogTag.ERROR_TAG, "Received a non-200 response from server.");
				return null;
			}
			
			return connection.getInputStream();
			
		} catch (ProtocolException e) {
			Log.e(LogTag.ERROR_TAG, "Unable to configure the feed HTTP client.", e);
			return null;
		} catch (IOException e) {
			Log.e(LogTag.ERROR_TAG, "Unable to retrieve data from the server.", e);
			return null;
		}

	}
	
	private void configureConnection(HttpURLConnection connection) throws ProtocolException {
		connection.setReadTimeout(CONNECT_TIMEOUT_MILLIS);
		connection.setConnectTimeout(READ_TIMEOUT_MILLIS);
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
	}

}
