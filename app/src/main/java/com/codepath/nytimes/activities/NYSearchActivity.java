package com.codepath.nytimes.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.codepath.nytimes.R;
import com.codepath.nytimes.Utils.Helper;
import com.codepath.nytimes.adapters.ArticleAdapter;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.serviceclient.NYSearchClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NYSearchActivity extends AppCompatActivity {

    private NYSearchClient client;
    private ListView lvArticles;
    private ArticleAdapter articleAdapter;
    private View view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nysearch);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_share);

        view = getCurrentFocus();
        final Activity activity = this;
        final Context context = this;

        lvArticles = (ListView) findViewById(R.id.lvArticle);
        final ArrayList<Article> articles = new ArrayList<>();
        // initialize the adapter
        articleAdapter = new ArticleAdapter(this, articles);
        // attach the adapter to the ListView


        lvArticles.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
//                Intent intent = new Intent(context, WebViewActivity.class);
//                intent.putExtra("article", articleAdapter.getItem(pos));
//                startActivity(intent);

                String url = "https://www.codepath.com/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorBG));

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, articleAdapter.getItem(pos).getWebUrl());

                int requestCode = 100;

                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

// set toolbar color and/or setting custom actions before invoking build()

// Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
                CustomTabsIntent customTabsIntent = builder.build();
// and launch the desired Url with CustomTabsIntent.launchUrl()
                customTabsIntent.launchUrl(activity, Uri.parse(articleAdapter.getItem(pos).getWebUrl()));
            }
        });

        lvArticles.setAdapter(articleAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.srch_toolbar);
        setSupportActionBar(toolbar);
    }


    private void search(final String query) {

        client = new NYSearchClient();

        client.searchArticle(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if(response != null) {
                        // Get the docs json array
                        docs = response.getJSONObject("response").getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Article> Articles = Article.fromJson(docs);
                        // Remove all books from the adapter
                        articleAdapter.clear();
                        // Load model objects into the adapter
                        for (Article article : Articles) {
                            articleAdapter.add(article); // add book through the adapter
                        }
                        articleAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                if(!Helper.isOnline()) {
                    Snackbar.make(view, R.string.snackbar_no_internet, Snackbar.LENGTH_LONG)
                            .setAction(R.string.snackbar_action, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    search(query);
                                }
                            })  // action text on the right side
                            .setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setDuration(3000).show();
                } else {
                    Snackbar.make(view, R.string.snackbar_service_down, Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setDuration(3000).show();
                }

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                search(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showSettingsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SettingsDialogFragment settingsDialogFragment = SettingsDialogFragment.newInstance("Some Title");
        settingsDialogFragment.show(fm, "fragment_settings");
    }
}
