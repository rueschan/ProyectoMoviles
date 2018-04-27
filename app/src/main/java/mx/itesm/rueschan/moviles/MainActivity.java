package mx.itesm.rueschan.moviles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import static xdroid.toaster.Toaster.toast;


import java.util.ArrayList;
import java.util.List;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.User;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TextView tvUser, tvMail;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int currentFragment;
    //private String currentUser;
    public static User currentUser;
    public static int numSize;
    private String currentName, currentEmail;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvUser = findViewById(R.id.tvUsuario);
        tvMail = findViewById(R.id.tvMail);
        new DBMain().execute();
        setSupportActionBar(toolbar);

        // Adding Floating Action Button to bottom right of main view
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.animate()
                .translationY(160)
                .alpha(1.0f)
                .setListener(null);
        fab.setVisibility(View.VISIBLE);
        fab.setClickable(false);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpView(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {

                switch (position) {
                    case 0:

                        fab.animate()
                                .translationY(fab.getHeight())
                                .alpha(1.0f)
                                .setListener(null);
                        fab.setVisibility(View.VISIBLE);
                        fab.setClickable(false);
                        break;
                    case 1:

                        fab.animate()
                                .translationY(0)
                                .alpha(1.0f)
                                .setListener(null);
                        fab.setVisibility(View.VISIBLE);
                        fab.setClickable(true);
                        break;
                    case 2:
                        if(SugeridosFragment.numSize == 0){
                            String errorMsg = SugeridosFragment.errorMessage;
                            MyAlertDialog dialog = new MyAlertDialog(errorMsg);
                            dialog.show(getFragmentManager(), "Sample Fragment");
                        }
                        fab.animate()
                                .translationY(fab.getHeight())
                                .alpha(1.0f)
                                .setListener(null);
                        fab.setVisibility(View.VISIBLE);
                        fab.setClickable(false);
                        break;
                }

            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar supportActionBar = getSupportActionBar();

        ClosetFragment.origen = ClosetFragment.Origin.MAIN;

        navigationView.setNavigationItemSelectedListener(this);


        // Adding Floating Action Button to bottom right of main view
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setAlpha(1.0f);

        currentFragment = viewPager.getCurrentItem();

        System.out.println(currentFragment);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentFragment = viewPager.getCurrentItem();
                Intent intent;
                switch (currentFragment) {
                    case 0:
                        intent = new Intent(v.getContext(), ImagesActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        if(FavoritosFragment.numSize == 0){
                            String errorMsg = "No hay suficiente ropa";
                            MyAlertDialog dialog = new MyAlertDialog(errorMsg);
                            dialog.show(getFragmentManager(), "Sample Fragment");
                        }else{

                        ClosetFragment.origen = ClosetFragment.Origin.FAVORITOS;
                        intent = new Intent(v.getContext(), SelectItemsActivity.class);
                        startActivity(intent);}
                        break;
                    case 2:
                        ClosetFragment.origen = ClosetFragment.Origin.SUGERIDOS;
                        intent = new Intent(v.getContext(), SugeridosActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //user = (User)getIntent().getSerializableExtra("userCurrent");
        View v = navigationView.getHeaderView(0);
        tvUser = (TextView ) v.findViewById(R.id.tvUsuario);
        tvMail = (TextView ) v.findViewById(R.id.tvMail);
        tvUser.setText(currentName);
        tvMail.setText(currentEmail);
    }

    private void setUpView(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ClosetFragment(), "Closet");
        adapter.addFragment(new FavoritosFragment(), "Favoritos");
        adapter.addFragment(new SugeridosFragment(), "Sugeridos");

        viewPager.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   /* @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_color) {
            Intent init = new Intent(this, PreferencesAct.class);
            init.putExtra("user", currentUser);
            init.putExtra("from",  "MainAct");
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    class DBMain extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            setMainUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Log.i("onPost", "Dato grabado ********************");
        }
    }

    private void setMainUser(){
        DataBase db = DataBase.getInstance(getApplicationContext());
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        currentUser = db.userDAO().searchByEmail(preferences.getString("currentUser", "User").toString());
        //    Log.i("Email", currentUser);
//        Log.i("hola",currentUser.getEmail() +"\n" + currentUser.getName() + "\n" + currentUser.getGender() + "\n" + currentUser.getAge() + "\n" + currentUser.getBirth() + "\n" + currentUser.getColor());
        //      Log.i("User", "Values: " + db.userDAO().countUsers());
        currentName = currentUser.getName().toString();
        currentEmail = currentUser.getEmail().toString();

    }
}

