package com.benlakey.android;

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

import static android.content.Context.CONNECTIVITY_SERVICE;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public final class Connectivity {

	private Connectivity() {}
	
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivityManager = 
				(ConnectivityManager)context.getSystemService(CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();

	}
	
	// HTTP connection reuse was buggy pre-froyo. 
	// See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	@SuppressWarnings("deprecation")
	public static void disableConnectionReuseIfNecessary() {
	    if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
	        System.setProperty("http.keepAlive", "false");
	    }
	}
	
}
