package com.codepath.nytimes.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.codepath.nytimes.R;
import com.codepath.nytimes.adapters.ArticleAdapter;
import com.codepath.nytimes.adapters.ItemClickSupport;
import com.codepath.nytimes.decoration.DividerItemDecoration;
import com.codepath.nytimes.decoration.SpacesItemDecoration;
import com.codepath.nytimes.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.nytimes.listeners.NYSearchResponseHandler;
import com.codepath.nytimes.listeners.NYTopStoriesResponseHandler;
import com.codepath.nytimes.models.Article;
import com.codepath.nytimes.models.Settings;
import com.codepath.nytimes.serviceclient.NYSearchClient;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TopStoriesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private NYSearchClient client;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private View view;

    private Settings settings = new Settings();

    ArrayList<Article> articles = new ArrayList<>();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_stories);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_share);

        final Activity activity = this;
        context = this;

        recyclerView = (RecyclerView) findViewById(R.id.rvTopArticles);


        articleAdapter = new ArticleAdapter(this, articles,R.layout.item_top_article_result_1, R.layout.item_top_article_result_2);

        topStories();

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorBG));

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, articles.get(position).getWebUrl());

                        int requestCode = 100;

                        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                requestCode,
                                intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(activity, Uri.parse(articles.get(position).getWebUrl()));
                    }
                }
        );

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.addItemDecoration(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                System.out.println("******Firing nextpage***** "+page);
                //client.getNextPage(page, new NYSearchResponseHandler(articleAdapter, view, articles, false));
            }
        });

        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.setAdapter(articleAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
    }

    private void topStories() {
        client = new NYSearchClient(settings);
        recyclerView.scrollToPosition(0);
        client.topStories(new NYTopStoriesResponseHandler(articleAdapter, view, articles, true));
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
                Intent intent = new Intent(context, NYSearchActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showSettingsDialog() {
//        FragmentManager fm = getSupportFragmentManager();
        new MaterialDialog.Builder(this)
                .title("")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        List<String> checkedNews = new ArrayList<String>();

                        View view = dialog.getCustomView();
                        EditText et = (EditText)view.findViewById(R.id.etDate);
                        Spinner spinner = (Spinner)view.findViewById(R.id.spnSort);
                        CheckBox chkArt = (CheckBox)view.findViewById(R.id.checkbox_arts);
                        CheckBox chkFashion = (CheckBox)view.findViewById(R.id.checkbox_fashion);
                        CheckBox chkSport = (CheckBox)view.findViewById(R.id.checkbox_sports);
                        String sortTxt = (String)spinner.getSelectedItem();
                        if(chkArt.isChecked())
                            checkedNews.add("Arts");

                        if(chkFashion.isChecked())
                            checkedNews.add("Fashion & Style");

                        if(chkSport.isChecked())
                            checkedNews.add("Sports");

                        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                        Date date = null;
                        if(et.getText().toString() != null && et.getText().toString().length() >= 0) {
                            try {
                                date = format.parse(et.getText().toString());
                                System.out.println(date.toString());
                            } catch (Exception e) {

                            }
                        }

                        settings = new Settings(date, sortTxt, checkedNews);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .customView(R.layout.fragment_settings, true)
                .negativeText("Cancel")
                .positiveText("Save")
                .show();
    }

    private View dateView;
    public void onDateBtnClick(View v) {
        dateView = v;
        Calendar now = Calendar.getInstance();
        DatePickerDialog tpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });

        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = (++monthOfYear)+"/" +dayOfMonth+"/"+year;
        EditText editDateText = (EditText) dateView.findViewById(R.id.etDate);
        editDateText.setText(date);
    }
}
