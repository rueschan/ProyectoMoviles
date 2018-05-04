package mx.itesm.rueschan.moviles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private TextView tvUser, tvMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        tvUser = findViewById(R.id.tvUsuarioSelected);
        tvMail = findViewById(R.id.tvMailSelected);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_viewAbout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerAbout);
       /* LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_about, null, false);
        mDrawerLayout.addView(contentView, 0);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar supportActionBar = getSupportActionBar();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tvUser.setText(MainActivity.currentUser.getName());
//        tvMail.setText(MainActivity.currentUser.getEmail());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_color) {
            Intent init = new Intent(this, PreferencesAct.class);
            init.putExtra("user", MainActivity.currentUser);
            init.putExtra("from", "MainAct");
            startActivity(init);
        } else if (id == R.id.nav_Info) {
            Intent init = new Intent(this, AboutActivity.class);
            startActivity(init);
        } else if (id == R.id.nav_logout) {
            Intent init = new Intent(this, LoginAct.class);
            startActivity(init);
            SharedPreferences preferences = getSharedPreferences("Log", MODE_PRIVATE);
            SharedPreferences.Editor pref = preferences.edit();
            pref.putBoolean("sesion", false);
            pref.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerAbout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
