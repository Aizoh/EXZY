package com.example.exigent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.exigent.Model.EmergencyGuide;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements EmergencyGuideAdapter.EmergencyGuideAdapterListener {

    private RecyclerView recyclerView;
    private List<EmergencyGuide> emergencyGuideList;
    private EmergencyGuideAdapter guideAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarGuide);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerViewEmergency);
        emergencyGuideList = new ArrayList<>();
        whiteNotificationBar(recyclerView);
        guideAdapter = new EmergencyGuideAdapter(this,emergencyGuideList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(guideAdapter);
        emergencyguidePopulate();


    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                guideAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                guideAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }



    /**TODO add a view for the emergency guide **/
    public void emergencyguidePopulate(){
        String stringTerrorism = getString(R.string.guideTerrorism);
        String stringFire = getString(R.string.guideFire);
        String stringFlood = getString(R.string.guideFloods);
        String stringKidnapping = getString(R.string.guideKidnapping);
        String stringRobbery = getString(R.string.guideRobbery);
        int[] images = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5};
        emergencyGuideList.add(new EmergencyGuide(images[0],"Fire",stringFire));
        emergencyGuideList.add(new EmergencyGuide(images[1],"Floods",stringFlood));
        emergencyGuideList.add(new EmergencyGuide(images[2],"Robbery",stringRobbery));
        emergencyGuideList.add(new EmergencyGuide(images[3],"Terrorism",stringTerrorism));
        emergencyGuideList.add(new EmergencyGuide(images[4],"Kidnapping",stringKidnapping));
    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onEmergencyGuideSelected(EmergencyGuide emergencyGuide) {

    }
}
