package mx.itesm.rueschan.moviles;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.User;

public class ImagesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private TextView tvUser, tvMail;

    //mensaje para indicar que hacer
    private TextView error_tv;

    private int numImagenes;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ClosetFragment.clicked);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewPagerPhotos);
        setUpView(viewPager);

        navigationView = (NavigationView) findViewById(R.id.nav_viewImages);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerImages);
        error_tv = findViewById(R.id.error_tv);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar supportActionBar = getSupportActionBar();

       /* navigationView.setNavigationItemSelectedListener(
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
                });*/
        tvUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsuario);
        tvMail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvMail);
        tvUser.setText(MainActivity.currentUser.getName());
        tvMail.setText(MainActivity.currentUser.getEmail());


        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
            fab.setImageDrawable(ContextCompat.getDrawable(ImagesActivity.this, R.drawable.ic_save));
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClosetFragment.origen == ClosetFragment.Origin.FAVORITOS) {
                    String selected = ClosetFragment.clicked;
                    int id = ImagesFragment.selectedID;

                    String[] types = getApplicationContext().getResources().getStringArray(R.array.types);
                    Log.i("TOUCH ",id + " " + selected);

                    if (id == -1) {
                        Toast.makeText(ImagesActivity.this, "Select an item before saving", Toast.LENGTH_SHORT).show();
                    } else {
                        if (selected.equals(types[0])) {
                            ClosetFragment.tempOutfit.setUpperID(id);
                            ImagesFragment.selectedID = -1;
                        } else if (selected.equals(types[1])) {
                            ClosetFragment.tempOutfit.setBottomID(id);
                            ImagesFragment.selectedID = -1;
                        } else if (selected.equals(types[2])) {
                            ClosetFragment.tempOutfit.setCoatID(id);
                            ImagesFragment.selectedID = -1;
                        } else if (selected.equals(types[3])) {
                            ClosetFragment.tempOutfit.setShoesID(id);
                            ImagesFragment.selectedID = -1;
                        }
                        onBackPressed();

                    }

                } else {
                    startActivity(new Intent(view.getContext(), TakePhoto.class));
                }
            }
        });

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
*/
    @Override
    protected void onStart() {
        super.onStart();
        new BDTarea().execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_color) {
            System.out.println("Clicked color");
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerImages);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private void setUpView(ViewPager viewPager) {

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ImagesFragment(), "Item");
        viewPager.setAdapter(adapter);

    }

    public void setAlert() {

        if(numImagenes == 0)
            error_tv.setVisibility(View.VISIBLE);
        else
            error_tv.setVisibility(View.INVISIBLE);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class BDTarea extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setAlert();

        }
    }

    private void cargarDatos() {

        DataBase db = DataBase.getInstance(getApplicationContext());
        User currentUser = MainActivity.currentUser;
        numImagenes = db.itemDAO().countByTypeAndUserID(ClosetFragment.clicked, currentUser.getIdUser());
        items = db.itemDAO().getAllItemsByType(ClosetFragment.clicked);
        DataBase.destroyInstance();
    }


}
