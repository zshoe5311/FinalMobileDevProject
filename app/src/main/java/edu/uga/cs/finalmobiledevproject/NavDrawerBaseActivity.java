package edu.uga.cs.finalmobiledevproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavDrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_nav_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer_base);
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        drawerLayout.closeDrawer(GravityCompat.START);
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_basket:
                intent = new Intent(this, BasketActivity.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_list:
                intent = new Intent(this, ViewShopListActivity.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_recentpurchase:
                intent = new Intent(this, RecentPurchaseActivity.class);
                intent.putExtra("currentUser", user);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0,0);
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }
}