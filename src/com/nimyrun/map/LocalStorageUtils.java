package com.nimyrun.map;

import java.lang.reflect.Type;
import java.util.List;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/* @formatter:off */
/**
 * must import Gson .jar into project
 * 1. download https://code.google.com/p/google-gson/downloads/detail?name=google-gson-2.2.4-release.zip&can=1&q=
 * 2. right click on project, then go to properties
 * 3. go to java build path
 * 4. click on libraries
 * 5. click on add external jar
 * 6. choose the gson-2.2.4.jar and add it
 * @author hhaider
 *
 */
/* @formatter:on */

public final class LocalStorageUtils {

	public LocalStorageUtils() {
		super();
	}

	/**
	 * Using Gson, converts object into a json string, and stores with
	 * sharedPreferences
	 * 
	 * @param sharedPreferences
	 * @param routes
	 */
	public static void storeRoutes(SharedPreferences sharedPreferences,
			List<Route> routes) {
		Editor editor = sharedPreferences.edit();
		String json = new Gson().toJson(routes);
		editor.putString("routes", json);
		editor.commit();
	}

	public static void addRunToRoute(SharedPreferences sharedPreferences,
			Run run,
			int routePosition) {
		// get old route list
		List<Route> routes = retrieveRoutes(sharedPreferences);

		// get the route to be updated
		Route route = routes.get(routePosition);

		// add run the the route
		route.addRun(run);

		// update route in the position
		routes.set(routePosition, route);

		// store updated list of routes
		storeRoutes(sharedPreferences, routes);
	}

	public static void addNewRouteFromRun(SharedPreferences sharedPreferences,
			Run run) {
		// create route from run
		Route route = new Route("blah");
		route.addRun(run);
		for (RunMetric runMetric : run.getRunMetrics()) {
			LatLng point = runMetric.getLatlng();
			route.addPoint(point.latitude, point.longitude);
		}

		// get old route list
		List<Route> routes = retrieveRoutes(sharedPreferences);

		// add new route to route list
		routes.add(route);

		// store updated list of routes
		storeRoutes(sharedPreferences, routes);
	}


	/**
	 * Retrieve json string from sharedPreferences, and using Gson converts the
	 * json string back to object
	 * 
	 * @param sharedPreferences
	 * @return
	 */
	public static List<Route> retrieveRoutes(SharedPreferences sharedPreferences) {
		String json = sharedPreferences.getString("routes", null);
		Type type = new TypeToken<List<Route>>() {
		}.getType();
		List<Route> routes = new Gson().fromJson(json, type);
		return routes;
	}

	public static Route retrieveRoute(SharedPreferences sharedPreferences,
			int routePosition) {
		List<Route> routes = retrieveRoutes(sharedPreferences);
		return routes.get(routePosition);
	}
}
