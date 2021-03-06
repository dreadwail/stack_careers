package com.benlakey.stackcareers;

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

import java.io.Serializable;

public class JobListing implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String encodedDescriptionMarkup;
	private String postedDateTime;
	private String updatedDateTime;
	private String uri;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEncodedDescriptionMarkup() {
		return encodedDescriptionMarkup;
	}

	public void setEncodedDescriptionMarkup(String encodedDescriptionMarkup) {
		this.encodedDescriptionMarkup = encodedDescriptionMarkup;
	}

	public String getPostedDateTime() {
		return postedDateTime;
	}

	public void setPostedDateTime(String postedDateTime) {
		this.postedDateTime = postedDateTime;
	}

	public String getUpdatedDateTime() {
		return updatedDateTime;
	}

	public void setUpdatedDateTime(String updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	public String toString() {
		return this.title;
	}

}
