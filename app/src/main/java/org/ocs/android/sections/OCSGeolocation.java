/*
 * Copyright 2013-2016 OCSInventory-NG/AndroidAgent contributors : mortheres, cdpointpoint,
 * Cédric Cabessa, Nicolas Ricquemaque, Anael Mobilia
 *
 * This file is part of OCSInventory-NG/AndroidAgent.
 *
 * OCSInventory-NG/AndroidAgent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * OCSInventory-NG/AndroidAgent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OCSInventory-NG/AndroidAgent. if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.ocs.android.sections;


import org.ocs.android.agent.R;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.lang.Boolean;
import java.lang.Number; 
import java.lang.Double;



import android.os.Build;
import android.text.format.DateFormat;

import android.content.Context;
import android.telephony.TelephonyManager;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

// NOTE html decode
/*
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;
*/
// NOTE html entities encode
import android.text.TextUtils;
import android.widget.TextView; 

//NOTE GPS
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria; 
//NOTE GEOCODE
import android.location.Geocoder;
import android.location.Address; 

//Number
import java.text.Format;
import java.text.NumberFormat;
// SimpleDateFormat
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
// DEBUG
import android.util.Log; 
import org.ocs.android.actions.OCSLog;

// NOTE Google api callback 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//note dialogs
import android.app.AlarmManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.app.PendingIntent;

//note 
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

//NOTE snackbar
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
//NOTE http operations 

//import okhttp3.*;

// NOTE Package
import android.content.pm.PackageManager; 
import android.content.ComponentName;
// EVAL

public class OCSGeolocation implements OCSSectionInterface {
    final private String sectionTag = "GEOLOCATION";

    private String name; 
    private String hostname; 
    private String ip;
    private String country;
    private String countrycode;
    private String region;
    private String regionname;
    private String city;
    private String zip;
    private Double lat;
    private String latitude;
    private Double lon;
    private String longitude;
    private String timezone;
    private String isp;
    private String osmap;
    private String google;
    private String aosmap;
    private String agoogle;
    private String linkOmap;
    private String linkGoogle;
    private String timehost;
// PHONE
    private String phonenumber;
    private String device_id;
    private String simcountry; 
    private String simoperator; 
    private String simopname;
    private String simserial;  
//GPS
    private String provider;
    private String msg;
//NOTE GEOCODE

   private String address ;
   private Integer addressline ;
   private String state ;
   private String postalCode ;
   private String subAdminArea ;
   private String subLocality ;
   private String thoroughfare ;
   private String subThoroughfare ;
   private String knownName ;

//NOTE Google Callback
   private List<Address> addresses = null;    

// SnackBar 
    private ProgressDialog dialog = null;
    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
// NOTE LOG
    private static final String TAG = "GeoDebugging";
    private Double result;
    private String finalresult;
//TextView tv;

//NOTE DEBUG
    private Boolean buggie = false;
/*
   public OCSSim(Context ctx) {
        OCSLog ocslog = OCSLog.getInstance();
        TelephonyManager mng = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        ocslog.debug("Get TelephonyManager infos");
        if (mng == null) {
            ocslog.error("TelephonyManager information not found");
        } else {
//            device_id = mng.getDeviceId();
//            simcountry = mng.getSimCountryIso();
//            simoperator = mng.getSimOperator();
//            simopname = mng.getSimOperatorName();
//            simserial = mng.getSimSerialNumber();
            phonenumber = mng.getLine1Number();
            ocslog.debug("device_id : " + device_id);
        }
    }
*/
    public OCSGeolocation(Context ctx) {


        Properties sp = System.getProperties();

        OCSLog ocslog = OCSLog.getInstance();
        TelephonyManager mng = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        ocslog.debug("Get TelephonyManager infos");
        if (mng == null) {
            ocslog.error("TelephonyManager information not found");
		phonenumber = "Telephone information not found";
        } else {
            device_id = mng.getDeviceId();
            simcountry = mng.getSimCountryIso();
            simoperator = TextUtils.htmlEncode(mng.getSimOperator().toString());
            simopname = TextUtils.htmlEncode(mng.getSimOperatorName().toString());
            simserial = mng.getSimSerialNumber();
            phonenumber = mng.getLine1Number();

            ocslog.debug("device_id : " + device_id);

	    Log.d(TAG, "phonenumber : " + phonenumber);
	    Log.d(TAG, "device_id : " + device_id);
	    Log.d(TAG, "simcountry : " + simcountry); 
	    Log.d(TAG, "simoperator : " + simoperator); 
	    Log.d(TAG, "simopname : " + simopname);
	    Log.d(TAG, "simserial : " + simserial);  

        }


//	osmap = "https://www.openstreetmap.org/?mlat="+latitude+"&mlon="+longitude;
//	google = "https://www.google.com/maps/search/?api=1&query="+latitude+','+longitude;
//
//	aosmap = "<a href=\""+ osmap +"\" target=\"_blank\" rel=\"noreferrer\">OpenStreetMap</a>";
//	agoogle = "<a href=\""+ google +"\" target=\"_blank\" rel=\"noreferrer\">GoogleMap</a>";

/*
	    String htmlString = aosmap;
	    Spanned spanned = HtmlCompat.fromHtml(htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT);
	    TextView tvOutput = (TextView) findViewById(R.id.text_view_id);
	    tvOutput.setText(spanned);
*/



//to decode Html String you can use Html.fromHtml()
//
//Html.fromHtml((String) htmlCode).toString();
//
//if you want reverse
//than you can use TextUtils.htmlEncode() 


//	link = Html.fromHtml((String) aosmap).toString();
//	link = 'OpenStreetMap'

//	linkOmap  = TextUtils.htmlEncode(aosmap.toString());
//	linkGoogle  = TextUtils.htmlEncode(agoogle.toString());

// NOTE GET-Ipaddress

//Context context = requireContext().getApplicationContext();
	Context context = ctx;
	WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	WifiInfo wifiInf = wifiMan.getConnectionInfo();
	int ipAddress = wifiInf.getIpAddress();
	ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));

//NOTE GET-GPS


            LocationManager localman = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
           //	Location location = localman.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (localman != null) {
            if (!localman.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {//check if network location provider context on

                Log.e(TAG, "GPS OFF -> network location provider not enabled");

                // TODO make a fallback logic for this issues like no LocationManager | Google Services


                Log.d(TAG,"GeoDebugging -> LocationManager is null ");
               
//                Location loc =  localman.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);     

//                setLocation(loc.getLatitude(),loc.getLongitude());
//                this.lat = 18.854129;
//                this.lon = -97.108440;                 
                this.lat = 00.000000;
                this.lon = 00.000000;                 


                //msg=" CallbackLocation Latitude Orizaba: "+lat + " fake Longitude: "+lon;
                msg="GPS Desactivado , se requiere que el sensor GPS este activado";

                Log.d(TAG, msg);

//		if(buggie){
                   Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
//		}

            } else {

                Criteria criteria = new Criteria();
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setSpeedRequired(true);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(false);
                provider = localman.getBestProvider(criteria, true);

                    Log.d(TAG, "GeoDebugging -> isLocationPresent latitude and longitude ");
                    Log.i(TAG, "GeoDebugging -> AllProviders = " + localman.getAllProviders().toString());
                    Log.i(TAG, "GeoDebugging -> Provider [" + provider + "] best criteria.");


                Location location = localman.getLastKnownLocation(provider);


                //TODO implement the GPS status is on
               // checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
               
               
                if (location != null) {
		    Log.e(TAG, "GPS is ON");

		    String gpsOn = new Boolean(localman.isProviderEnabled(LocationManager.GPS_PROVIDER)).toString();
		    String networkOn = new Boolean(localman.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).toString();

		     msg="location is NOT NULL , GpsStatus -> " + gpsOn +" NetworkStatus -> " + networkOn ;
		     Log.e(TAG, msg);
		    if(buggie){
		       Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
		    }

		    setLocation(location.getLatitude(),location.getLongitude());
    //                lat = location.getLatitude();
    //                lon = location.getLongitude();
    //                showMsg();

		    if(buggie){
		       Toast.makeText(ctx, "GPS ON :: latitude:" + this.lat + " longitude:" + this.lon, Toast.LENGTH_LONG).show();
		    }

//                searchNearestPlace(voice2text);

                } else {

		    String gpsOn = new Boolean(localman.isProviderEnabled(LocationManager.GPS_PROVIDER)).toString();
		    String networkOn = new Boolean(localman.isProviderEnabled(LocationManager.NETWORK_PROVIDER)).toString();

		     msg="location is NULL , GpsStatus -> " + gpsOn +" NetworkStatus -> " + networkOn ;
		     Log.e(TAG, msg);

		    if(buggie){
		       Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
		    }

    //                 Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
		     Log.d(TAG,"LocationListener -> send a request Location Update ");

			    Log.i(TAG,msg+ " -> AllProviders = " + localman.getAllProviders().toString());
			    Log.i(TAG,msg+ " -> Provider [" + provider + "] best criteria.");

		     lat = 00.000000;
		     lon = 00.000000;

		      localman.requestLocationUpdates(provider, 0, 0, locationListener);
    /*
		     if (location == null) {
		       msg="Testing with LOCATION NOTNULL NOW trying again";
		       Log.i(TAG, msg); 
		       Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();

		    //  triggerRebirth(ctx);
		    //  System.exit(0);
		      localman = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		      location = localman.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		      location = localman.getLastKnownLocation(provider);

		       setLocation(location.getLatitude(),location.getLongitude());

		     } else {

		       msg="Testing with LOCATION ->STILL NULL Reboot the app";
		       Log.d(TAG, msg); 
		       Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
		       setLocation(lat,lon);

		     }
    */                 
    //                 lat = 18.835330;
    //                 lon = -97.123827;
    //	            doRestart(ctx);
    //	            triggerRebirth(ctx);
			msg="Para actualizar el sistemas de coordenadas necesita reiniciar la aplicacion";
			Log.d(TAG, "River COORDINATES : " + msg); 

		       Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();

    //                Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
			//This is what you need:
    //                    LocationListener locationListener = new LocationListener() {...} // Define this...
    //                   localman.requestLocationUpdates(provider, 10, 0, locationListener);
			
			Log.d(TAG, "Updated COORDINATES : before requestLocationUpdates ..."); 
			
    //                    setLocation(lat,lon);
    //                    lat=location.getLatitude();
    //                    lon=location.getLongitude();
			
			msg="GPS OFF::Update Latitude: "+ lat + " Update Longitude: "+ lon ;  
			Log.d(TAG, "UPDATED COORDINATES : " + msg);
     
		    if(buggie){
		       Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
		    }

//                Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();

                   // getCurrentLocation.requestLocationUpdates(provider, 1000, 0, ctx);
                }

            }

            // myString = NumberFormat.getInstance().format(myNumber);
            //	latitude = NumberFormat.getInstance().format(lat);
            //	longitude = NumberFormat.getInstance().format(lon);
            // TODO make this a function:

             result = lat;    
             String finalresultlat = new Double(result).toString();
             NumberFormat nft = NumberFormat.getInstance();
             nft.setMinimumFractionDigits(6);
             nft.setMaximumFractionDigits(9);
             latitude = nft.format(result);

             result = lon;    
             String finalresultlon = new Double(result).toString();
             NumberFormat nfn = NumberFormat.getInstance();
             nfn.setMinimumFractionDigits(6);
             nfn.setMaximumFractionDigits(9);
             longitude = nfn.format(result);

                    msg="FINAL -> Update Latitude: "+latitude + " Update Longitude: "+longitude;  
                    Log.d(TAG, "Final COORDINATES : " + msg); 

		if(buggie){
                   Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
		}

//                Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
// TODO GEOFENCE

//Set Address
        try {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());


	       if (!Geocoder.isPresent()) {
		// Certain devices do not have an implementation of a geocoder - in that case there is
		// no point trying to get location updates because we cannot retrieve the country based
		// on the location anyway.
		
		Log.d(TAG, "GeoDebugging -> getAddress: NO Geocoder present"); 
		       address = "";
		       city = "";
		       state = "";
		       country = "";
		       postalCode = "";
		       subAdminArea = "";
		       subLocality = "";
		       thoroughfare = "";
		       subThoroughfare = "";
		       knownName = "";

	       } else {
		  
		Log.d(TAG, "GeoDebugging -> getAddress: Geocoder IS present"); 
			  

		    List<Address> addresses = geocoder.getFromLocation(lat,lon, 1);

		    if (addresses != null && addresses.size() > 0) {

		       address = TextUtils.htmlEncode(addresses.get(0).getAddressLine(0).toString()); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
		       addressline = addresses.get(0).getMaxAddressLineIndex(); //
		       city = TextUtils.htmlEncode(addresses.get(0).getLocality().toString());
		       state = TextUtils.htmlEncode(addresses.get(0).getAdminArea().toString());
		       country = addresses.get(0).getCountryName();
		       postalCode = addresses.get(0).getPostalCode();
    //                   phonenumber = addresses.get(0).getPhone();
		       subAdminArea = addresses.get(0).getSubAdminArea(); // Only if available else return NULL
		       subLocality = addresses.get(0).getSubLocality(); // Only if available else return NULL
		       thoroughfare = addresses.get(0).getThoroughfare(); // Only if available else return NULL
		       subThoroughfare = addresses.get(0).getSubThoroughfare(); // Only if available else return NULL
		       knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

			Log.d(TAG, "getAddress:  address " + address);
			Log.d(TAG, "getAddress:  addressline " + addressline.toString());
			Log.d(TAG, "getAddress:  city " + city);
			Log.d(TAG, "getAddress:  state " + state);
			Log.d(TAG, "getAddress:  postalCode " + postalCode);
      //                  Log.d(TAG, "getAddress:  phonenumber " + phonenumber);
			Log.d(TAG, "getAddress:  country " + country);
			Log.d(TAG, "getAddress:  subAdminArea " + subAdminArea);
			Log.d(TAG, "getAddress:  subLocality " + subLocality);
			Log.d(TAG, "getAddress:  getThoroughfare " + thoroughfare);
			Log.d(TAG, "getAddress:  houseNumber " + subThoroughfare);
			Log.d(TAG, "getAddress:  knownName " + knownName);
  

                        msg = address;
     //                   Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
			if(buggie){
			   Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
			}

		     } //NOTE End if geocoder is present
		}
           } catch (IOException e) {
            e.printStackTrace();
           }   

//	osmap = "https://www.openstreetmap.org/?mlat="+latitude+"&mlon="+longitude;
//	google = "https://www.google.com/maps/search/?api=1&query="+latitude+','+longitude;
//
//	aosmap = "<a href=\""+ osmap +"\" target=\"_blank\" rel=\"noreferrer\">OpenStreetMap</a>";
//	agoogle = "<a href=\""+ google +"\" target=\"_blank\" rel=\"noreferrer\">GoogleMap</a>";
//
//	linkOmap  = TextUtils.htmlEncode(aosmap.toString());
//	linkGoogle  = TextUtils.htmlEncode(agoogle.toString());
        
        hostname = TextUtils.htmlEncode(Build.MODEL.toString());
//        name = sp.getProperty("java.vm.name") + sp.getProperty("java.vm.version");
        name = "GEOLOCATION";
//        pathlevel = "";
//        classpath = sp.getProperty("java.class.path");
//        home = sp.getProperty("java.home");
//        hostname = Build.MODEL; 
//        ip = "201.108.196.140";
//        country = simcountry;
        countrycode = Locale.getDefault().getCountry();
        region = simcountry;
        regionname = state;
//        city = "XOrizaba";
        zip = postalCode;
//        latitude = "19.521100";
//        longitude = "-96.922310";
        timezone = "America/Mexico_City";
        isp = simopname;
//        timehost = (String) DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", System.currentTimeMillis());
        timehost = (String) DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis());
//        timehost = hour.ToUniversalTime().ToString("s");




	Log.d(TAG, "GET IP : " + ip); 
	Log.d(TAG, "GET TIMEHOST : " + timehost); 
	Log.d(TAG, "GET CLIENT : " + hostname); 
	Log.d(TAG, "GET LATITUDE : " + latitude); 
	Log.d(TAG, "GET LONGITUDE : " + longitude); 
//	Log.d(TAG, "GET LINK : " + linkOmap); 
//	Log.d(TAG, "GET LINK : " + linkGoogle); 


    } //END GEOLocation

//StackOverflow : https://stackoverflow.com/questions/15110528/service-not-available-while-calling-geocoder-getfromlocation


/*
public class apiGeocoder{

  final OkHttpClient client = new OkHttpClient();

      public String run(String url) throws IOException {
	Request request = new Request.Builder()
	    .url(url)
	    .build();

	try (Response response = client.newCall(request).execute()) {
	//  System.out.println(response);
	  return response.body().toString();

	} catch (IOException e ) {
	     e.printStackTrace();
	     return "Error"; 
        }
      }

} // END apiGeocoder()
*/

/*
public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity 
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called. 
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
}


 public static void triggerRebirth(Context context) {
    PackageManager packageManager = context.getPackageManager();
    Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
    ComponentName componentName = intent.getComponent();
    Intent mainIntent = Intent.makeRestartActivityTask(componentName);
    context.startActivity(mainIntent);
    Runtime.getRuntime().exit(0);
}

*/

    private void setLocation(double latitude , double longitude ){



        this.lat = latitude;
        this.lon = longitude;

        msg = "Set Locations to "+ this.lat +" and " + this.lon; 
        Log.d(TAG, msg); 
       // Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
    }


              //  Location location = localman.getLastKnownLocation(provider);


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
	       
                Log.d(TAG, "onLocationChanged : Initalize getting locations"); 

                lat = location.getLatitude();
                lon = location.getLongitude();

                setLocation(lat,lon);
                msg="New Latitude: "+ lat + " New Longitude: "+ lon;
               // Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    //            Toast.makeText(ctx,msg,Toast.LENGTH_LONG).show();
	        Log.d(TAG, "onLocationChanged : " + msg); 
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    /*
    <!ELEMENT GEOLOCATION (NAME | PATHLEVEL | COUNTRY | CLASSPATH | HOME)*>
     *
     */
//               'CLIENT' => {},
//               'IP' => {},
//               'PHONE' => {},
//               'IMEI' => {},
//               'COUNTRY' => {},
//               'COUNTRYCODE' => {},
//               'REGION' => {},
//               'REGIONNAME' => {},
//               'CITY' => {},
//               'ZIP' => {},
//               'LATITUDE' => {},
//               'LONGITUDE' => {},
//               'TIMEZONE' => {},
//               'ISP' => {},
//               'ORG' => {},
//               'ASLABEL' => {},
//               'OSMAP' => {},
//               'GOOGLE' => {},
//               'BING' => {},
//               'OSMOSE' => {},
//               'HERE' => {},
//               'SERVER_ONE' => {},
//               'SERVER_TWO' => {},
//               'TIMEHOST' => {}

    public OCSSection getSection() {
        OCSSection s = new OCSSection(sectionTag);

        s.setAttr("CLIENT", hostname);
        s.setAttr("IP", ip);
        s.setAttr("PHONE", phonenumber);
        s.setAttr("IMEI", device_id);
        s.setAttr("ADDRESS", address);
        s.setAttr("COUNTRY", country);
        s.setAttr("COUNTRYCODE", countrycode);
        s.setAttr("REGION", region);
        s.setAttr("REGIONNAME", regionname);
        s.setAttr("CITY", city);
        s.setAttr("ZIPCODE", zip);
        s.setAttr("LATITUDE", latitude );
        s.setAttr("LONGITUDE", longitude);
        s.setAttr("TIMEZONE", timezone);
        s.setAttr("ISP", isp);
        s.setAttr("ORG", simopname);
        s.setAttr("ASLABEL", simoperator);
//        s.setAttr("OSMAP", linkOmap);
//        s.setAttr("GOOGLE", linkGoogle);
//        s.setAttr("BING", "");
//        s.setAttr("OSMOSE", "");
//        s.setAttr("HERE", "");
//        s.setAttr("SERVER_ONE", "");
//        s.setAttr("SERVER_TWO", "");
        s.setAttr("TIMEHOST",timehost);

        s.setTitle(name);
        return s;
    }

    public ArrayList<OCSSection> getSections() {
        ArrayList<OCSSection> lst = new ArrayList<>();
        lst.add(getSection());
        return lst;
    }

    public String toString() {
        return getSection().toString();
    }

    public String toXML() {
// TODO we hacve a situation with the resulting xml in chomi phones
// xmllint --valid --encode utf-8 /tmp/geops.xml 2>&1 | tee /tmp/geops.log	
// /tmp/geops.xml:1: validity error : Validation failed: no DTD found !

//<GEOLOCATION>
//            ^
//<?xml version="1.0" encoding="utf-8"?>
//<GEOLOCATION>
// ...
//</GEOLOCATION>

// [perl:error] [pid 552] [client 192.168.0.125:44377] \nnot well-formed (invalid token) at line 76, column 15, byte 2350 at /usr/lib/
//       │ x86_64-linux-gnu/perl5/5.32/XML/Parser.pm line 187.\nXML::Simple called at /usr/local/share/perl/5.32.1/Apache/Ocsinventory.pm line 218.\n

	Log.d(TAG, "GET XML : " + getSection().toXML()); 
        return getSection().toXML();
    }

    public String getSectionTag() {
        return sectionTag;
    }
}
