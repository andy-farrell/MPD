package com.example.cw_mpd_19;
// Andrew Farrell - S1511335 \\

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemClickListener,
        OnMapReadyCallback
{
    private String separator = System.getProperty("line.separator");

    private Toolbar mToolbar;
    private String url1="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private EditText inputFilter;
    private TextView s2title;
    private TextView s2desc;
    private TextView s2date;
    private ViewSwitcher vwSwitch;
    private Button feedBtn;
    private GoogleMap mMap;
    private rssFeed currentItem;
    private String result = "";
    private ListView rssOutList;
    public LinkedList<rssFeed> rssFeedTop = new LinkedList<rssFeed>();
    ArrayList<String> listItemTitles = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        BaseAdapter listAdapter = new ArrayAdapter<String> (this, R.layout.custom_textview_item, listItemTitles);
        // Override the getView part of this adapter as we want to customise how items are shown

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.nav_bar);
        setSupportActionBar(mToolbar);

        /*d1 = (DrawerLayout)findViewById(R.id.dl);
        abdt1 = new ActionBarDrawerToggle(this,d1,R.string.Open, R.string.Close);
        abdt1.setDrawerIndicatorEnabled(true);

        d1.addDrawerListener(abdt1);

        abdt1.syncState();*/


        vwSwitch = (ViewSwitcher)findViewById(R.id.vwSwitch);
        if (vwSwitch == null)
        {
            Toast.makeText(getApplicationContext(), "Null ViewSwitcher", Toast.LENGTH_LONG);
            Log.e(getPackageName(), "null pointer");
        }

        s2title = (TextView)findViewById(R.id.s2title);
        s2desc = (TextView)findViewById(R.id.s2desc);
        s2date = (TextView)findViewById(R.id.s2date);
        inputFilter = (EditText)findViewById(R.id.inputFilter);
        feedBtn = (Button)findViewById(R.id.feedBtn);
        feedBtn.setOnClickListener(this);

        rssOutList = (ListView)findViewById(R.id.incidentList);

        rssOutList.setOnItemClickListener(this);
        rssOutList.setAdapter(listAdapter);
        rssOutList.forceLayout();

    } // End of onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchItem:
                // Start MapsActivity.class
                Intent mainIntent = new Intent(MainActivity.this,
                        MainActivity.class);
                startActivity(mainIntent);
                return true;
            case R.id.datePicker:
                Intent dateIntent = new Intent(MainActivity.this,
                        datePicker.class);
                startActivity(dateIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ViewGroup vg = findViewById(R.id.mainScreen);
        vg.invalidate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Double Lat = 0.0;
        Double Lng = 0.0;
        String markerTitle;
        // Assigns the Latitude and Longitude to relevant variables which are then
        // assigned to the google map feature.
        Lat = currentItem.getItemLat();
        Lng = currentItem.getItemLon();
        markerTitle = currentItem.getItemTitle();
        LatLng selected = new LatLng(Lat, Lng);
        mMap.addMarker(new MarkerOptions().position(selected).title(markerTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selected));
        Log.e("Map", "onMapReady called");

    }

    @Override
    //handling items clicked on the ListView
    public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
    {
        /* all of this method is only possible AFTER the required data is parsed and available
        /* achieved by using the previously created rssFeedTop LinkedList containing all the
        /* rssFeed objects


        /* if a user clicks on a earthquake 'title' find an item matching that title
         within the list of rss output (rssFeedTop) so that we can individually pull
         in data from them */


        for (rssFeed item : rssFeedTop) {
            Log.e("Info", "Looking for: " + parent.getItemAtPosition(pos).toString());

            //find out the title of the item the user clicked on in the ListView

            String itemCheck = parent.getItemAtPosition(pos).toString();

            if (item.getItemTitle() == itemCheck) {
                // when we find it, set the description to match that of the earthquake
                Log.e("Success", "Found Matching Item");
                // Set the following values for the new TextView
                currentItem = item;
                s2title.setText(item.getItemTitle());
                s2desc.setText(item.getItemDesc());
                if (!(item.getItemDesc().contains("Start Date"))) {
                    s2date.setText(separator + "Last updated on: " + item.getItemPubDate());
                }
                else s2date.setText("");
            }

        }
        /*because we are using another LinearLayout to hold the s2title, s2desc and s2date TextViews,
         switch to that view so that the user can see the output*/
        vwSwitch.showNext();
        Log.e("Map should be ready", "Below this line");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //The below method improves the search functionality in that the user does not need to
    //concern themselves with the specificity of their search term

    public String capitaliseStart(String caps)
    {
        Character c1 = caps.charAt(0);

        String c2 = c1.toString().toUpperCase();

        Character c3 = c2.charAt(0);

        caps = caps.replace(c1, c3);

        return caps;
    }

    public void onBackPressed()
    {
        if (vwSwitch.getNextView().getId() == R.id.mainScreen) {
            vwSwitch.showPrevious();
            mMap.clear();
        }
        else
            {
            finish();
        }
    }


    public void onClick(View aview)
    {
        startProgress(aview);
    }

    public void startProgress(View aview) {

        if (aview.getId() == (R.id.feedBtn)) { // load earthquake feed
            new Thread(new Task(url1)).start();
            rssOutList.clearChoices();

        }
    }//

    public LinkedList<rssFeed> parseXML(String inputData, String filter)
    {
        rssFeed feed = null;


        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(inputData));
            int eventType = xpp.getEventType();

            //if the container for all the RSS feed output is not empty, clear it
            if (!rssFeedTop.isEmpty())
            {
                rssFeedTop.clear();
            }
            while (eventType !=XmlPullParser.END_DOCUMENT)
            {
                //Found the start tag
                if(eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("channel")) {
                        //rssFeedTop is a global variable to allow access to it from within this
                        //class at any time, such as when clicking on an item in the ListView
                        rssFeedTop = new LinkedList<rssFeed>();
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Item Start Tag Found");
                        temp = temp.replaceAll("UK Earthquake alert : ", "");
                        feed = new rssFeed();
                        feed.setItemTitle(temp);
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Showing: + " + temp);
                        temp = temp.replaceAll("; ", " " + separator);
                        temp = temp.replaceAll("-", " -");
                        feed.setItemDesc(temp);
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Link to earthquake: " + temp);
                        feed.setItemLink(temp);
                    } else if (xpp.getName().equalsIgnoreCase("category")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Category: " + temp);
                        feed.setItemCategory(temp);
//
                    } else if (xpp.getName().equalsIgnoreCase("geo:lat")) {
                        //use this for geolocation?
                        String temp = xpp.nextText();
                        Double dTemp = Double.parseDouble(temp);
                        Log.e("MyTag", "Latitude: " + temp);
                        feed.setItemLat(dTemp);
                    } else if (xpp.getName().equalsIgnoreCase("geo:long")) {
                        //use this for geolocation?
                        String temp = xpp.nextText();
                        Double dTemp = Double.parseDouble(temp);
                        Log.e("MyTag", "Longitude: " + temp);
                        feed.setItemLon(dTemp);
                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        String temp = xpp.nextText();
                        Log.e("MyTag", "Updated on: " + temp);
                        feed.setItemPubDate(temp);
                    }

                }
                else if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        Log.e("MyTag", "Full channel output is" + feed.toString());
                        /* first check if the title or description of the incident/planned roadworks matches
                        what the user searches for in the EditText element on the MainView
                         */
                        //handle all potential cases to make it case insensitive for a better use
                        //experience
                        if(feed.getItemTitle().contains(filter.toUpperCase()) || feed.getItemDesc().contains(filter.toUpperCase()) || feed.getItemTitle().contains(filter) ||
                                feed.getItemDesc().contains(filter) || feed.getItemTitle().contains(capitaliseStart(filter)) || feed.getItemDesc().contains(capitaliseStart(filter)))

                        {
                            //if there is a match, then add this as it is one of the items we want
                            rssFeedTop.add(feed);
                        }

                    } else if (xpp.getName().equalsIgnoreCase("channel")) {
                        int size;
                        size = rssFeedTop.size();
                        Log.e("MyTag", "Number of feeds: " + size);
                    }
                }
                eventType = xpp.next();
            } // end of while loop
        }
        catch (XmlPullParserException ael)
        {
            Log.e("MyTag", "Error during parse" + ael.toString());
        }
        catch (IOException ael)
        {
            Log.e("MyTag", "IO Error encountered on parse attempt");
        }

        Log.e("MyTag", "End of feed");

        return rssFeedTop;
    }

    public LinkedList<rssFeed> getRssFeedTop() {
        return rssFeedTop;
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
        private String url;


        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run() {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                if (!result.equals("")) {
                    result = "";
                }
                while ((inputLine = in.readLine()) != null) {

                    result = result + inputLine;
                    Log.e("MyTag", inputLine);

                }
                in.close();
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception");
            }
            // Parse the resulting XML into a linked list
            final LinkedList<rssFeed> rssOutput = parseXML(result, inputFilter.getText().toString());

            // Now prepare the items we will add to the list of titles, obtained with getItemTitle()
            // from the OTHER FeedChannelItem list (currentIncidents) created during parsing through
            // a for loop use this list of titles to enable us to loop over each FeedChannelItem in
            // the LinkedList of them when clicking on one of these titles in the ListView

            MainActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    //If there are earthquakes found by the parser, do something
                    if (rssOutput != null) {
                        Log.e("MyTag", "Incidents reported...");
                        if (!listItemTitles.isEmpty()) {
                            listItemTitles.clear();

                        }

                        //for all earthquakes found, get the title of that item and add the returned
                        //string to the currently empty list of titles
                        for (rssFeed filter : rssOutput) {

                            // Add data to list
                            listItemTitles.add(filter.getItemTitle());
                        }
                        // tell the app that the adapter used to handle the list of titles within the ListView has changed
                        // this is what makes it update after you press any of the buttons so that the ListView contents will
                        // change when needed
                        Log.e("MyTag", "***PRE notifyDataSetChanged()***");
                        //
                        ((BaseAdapter) rssOutList.getAdapter()).notifyDataSetChanged();

                        }

                    else
                        {
                        Log.e("MyTag", "No Earthquakes found");
                         }

                    }



            });
        }

    }


}
