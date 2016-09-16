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

import android.util.Log;

import com.benlakey.android.Connectivity;
import com.benlakey.android.LogTag;
import com.benlakey.stackcareers.JobListing;
import com.benlakey.stackcareers.SearchQuery;
import com.google.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpCareerFeedClient implements CareerFeedClient {

	private static final int CONNECT_TIMEOUT_MILLIS = 7000;
	private static final int READ_TIMEOUT_MILLIS = 7000;

	private final HttpSearchRequestBuilder requestBuilder;
	private final HttpSearchResponseParser responseParser;
	private final OkHttpClient client;

	@Inject
	public HttpCareerFeedClient(
			HttpSearchRequestBuilder requestBuilder, 
			HttpSearchResponseParser responseParser) {
		this.requestBuilder = requestBuilder;
		this.responseParser = responseParser;
		this.client = new OkHttpClient().newBuilder().connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
				.readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS).build();
		Connectivity.disableConnectionReuseIfNecessary();
	}

	@Override
	public List<JobListing> search(SearchQuery query) {

		Request request = this.constructURL(query);
		if(request == null) {
			return Collections.emptyList();
		}

		InputStream inputStream = this.fetchData(request);
		if(inputStream == null) {
			return Collections.emptyList();
		}

		return this.responseParser.parse(inputStream);

	}

	private Request constructURL(SearchQuery query) {
		try {
			URL url = this.requestBuilder
					.setLocation(query.getLocation())
					.setSkill(query.getSkill())
					.build();
			return new Request.Builder().url(url).get().build();
		} catch (MalformedURLException e) {
			Log.e(LogTag.ERROR_TAG, "URL couldn't be constructed for the query data.", e);
			return null;
		}
	}

	private InputStream fetchData(Request request) {

		try {

			Response response = client.newCall(request).execute();
			if(response.code() != 200) {
				Log.e(LogTag.ERROR_TAG, "Received a non-200 response from server.");
				return null;
			}

			return response.body().byteStream();

		} catch (ProtocolException e) {
			Log.e(LogTag.ERROR_TAG, "Unable to configure the feed HTTP client.", e);
			return null;
		} catch (IOException e) {
			Log.e(LogTag.ERROR_TAG, "Unable to retrieve data from the server.", e);
			return null;
		}

	}

}
