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

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import com.benlakey.stackcareers.R;
import com.google.inject.Inject;

public class HttpSearchRequestBuilder {

	@Inject 
	protected Context context;

	private static final String DEFAULT_ENDPOINT = "http://careers.stackoverflow.com/jobs/feed";
	private static final String QUERY_FORMAT = "?searchTerm=%s&location=%s";

	private String skill;
	private String location;

	public HttpSearchRequestBuilder setSkill(String skill) {
		this.skill = skill;
		return this;
	}
	
	public HttpSearchRequestBuilder setLocation(String location) {
		this.location = location;
		return this;
	}
	
	public URL build() throws MalformedURLException {
		String queryString = String.format(QUERY_FORMAT, this.skill, this.location);
		String endpoint = this.context.getString(R.string.feed_api);
		if(endpoint == null) {
			endpoint = DEFAULT_ENDPOINT;
		}
		return new URL(endpoint + queryString);
	}
	
}
