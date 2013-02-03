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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.benlakey.android.LogTag;
import com.benlakey.stackcareers.JobListing;

public class HttpSearchResponseParser {

	public List<JobListing> parse(InputStream inputStream) {

		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, null);
			parser.nextTag();
			return this.readFeed(parser);
		} catch (XmlPullParserException e) {
			Log.e(LogTag.ERROR_TAG, "Couldn't parse feed from server.", e);
			return Collections.emptyList();
		} catch (IOException e) {
			Log.e(LogTag.ERROR_TAG, "Unable to read response from server.", e);
			return Collections.emptyList();
		}

	}

	private List<JobListing> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		List<JobListing> listings = new LinkedList<JobListing>();

		parser.require(XmlPullParser.START_TAG, null, "rss");
		parser.next();
		parser.require(XmlPullParser.START_TAG, null, "channel");

		while (parser.next() != XmlPullParser.END_TAG) {
			
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String elementName = parser.getName();
			
			if (elementName.equals("item")) {
				JobListing listing = this.readEntry(parser);
				listings.add(listing);
			} else {
				this.skipElement(parser);
			}

		}
		
		return listings;

	}

	private JobListing readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		parser.require(XmlPullParser.START_TAG, null, "item");
		
		JobListing listing = new JobListing();

		while (parser.next() != XmlPullParser.END_TAG) {
			
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			
			String elementName = parser.getName();
			if (elementName.equals("title")) {
				String title = this.readTagText(parser, "title");
				listing.setTitle(title);
			} else if (elementName.equals("link")) {
				String link = this.readTagText(parser, "link");
				listing.setUri(link);
			} else if (elementName.equals("description")) {
				String description = this.readTagText(parser, "description");
				listing.setEncodedDescriptionMarkup(description);
			} else if (elementName.equals("pubDate")) {
				String pubDate = this.readTagText(parser, "pubDate");
				listing.setPostedDateTime(pubDate);
			} else if (elementName.equals("updated")) {
				String updated = this.readTagText(parser, "updated");
				listing.setUpdatedDateTime(updated);
			} else {
				skipElement(parser);
			}
			
		}

		return listing;
		
	}

	private String readTagText(XmlPullParser parser, String expectedTagName) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, null, expectedTagName);
		String title = this.readTagText(parser);
		parser.require(XmlPullParser.END_TAG, null, expectedTagName);
		return title;
	}

	private String readTagText(XmlPullParser parser) throws IOException, XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skipElement(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG: depth--; break;
				case XmlPullParser.START_TAG: depth++; break;
			}
		}
	}

}
