package com.sanskrit.pmo.Activities;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanskrit.pmo.Adapters.FunctionalChartAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.MyGovCacheClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.FunctionalChart;
import com.sanskrit.pmo.network.mygov.models.FunctionalChartModel;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FunctionalChartActivity extends AppCompatActivity {
    FunctionalChartAdapter adapter;
    AppBarLayout appBarLayout;
    CardView cardView;
    List<FunctionalChartModel> charts = new ArrayList();
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    TextView description;
    FloatingActionButton fab;
    ImageView headerImage;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    List<String> tableColumns = new ArrayList();
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        //setFullscreen(true);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_functional_chart);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        this.appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        this.headerImage = (ImageView) findViewById(R.id.header_picture);
        this.description = (TextView) findViewById(R.id.description);
        this.cardView = (CardView) findViewById(R.id.cardView);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.collapsingToolbarLayout.setTitle(getString(R.string.list_of_officers));
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.adapter = new FunctionalChartAdapter(this, this.charts);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        fetchDetails();
    }

    private void fetchDetails() {
        MyGovCacheClient.getInstance(this).getPMOFunctionalChart(PreferencesUtility.getLanguagePrefernce(this), new GenericCallback() {


            @Override
            public void success(Object response) {
                if (response instanceof FunctionalChart) {
                    FunctionalChartActivity.this.progressBar.setVisibility(View.GONE);
                    FunctionalChart chart = (FunctionalChart) response;
                    FunctionalChartActivity.this.collapsingToolbarLayout.setTitle(chart.mTitle);
                    FunctionalChartActivity.this.parseHtml(chart.mContent);
                }
            }

            @Override
            public void failure() {

            }
        });
    }

    private void parseHtml(String html) {
        try {
            Elements rows = ((Element) Jsoup.parse(html).select("table").get(0)).select("tr");
            this.tableColumns.add(((Element) rows.get(0)).select("td").text());
            for (int i = 1; i < rows.size(); i++) {
                Elements cols = ((Element) rows.get(i)).select("td");
                Log.d("lol", cols.toString());
                try {
                    this.charts.add(new FunctionalChartModel(((Element) cols.get(1)).text(), ((Element) cols.get(0)).text(), ((Element) cols.get(2)).text()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.adapter.updateDataSet(this.charts);
    }

    private void setUpPaletteColors(Bitmap loadedImage) {
        try {
            Palette palette = Palette.generate(loadedImage);
            this.collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(Color.parseColor("#66000000")));
            this.collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(Color.parseColor("#66000000")));
            ColorStateList colorStateList = new ColorStateList(new int[][]{new int[0]}, new int[]{palette.getMutedColor(Color.parseColor("#66000000"))});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
