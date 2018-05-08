package mx.itesm.rueschan.moviles;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;
import mx.itesm.rueschan.moviles.EntidadesBD.Outfit;


public class SelectItemsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ConstraintLayout selectedLayout;
    private FloatingActionButton fab;
    private TextView tvMail, tvUser;
    private Item coat;
    private Item upper;
    private Item lower;
    private Item shoes;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_items);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpView(viewPager);
        tvMail = findViewById(R.id.tvMail);
        tvUser = findViewById(R.id.tvUsuario);
//        tvMail.setText(MainActivity.currentUser.getEmail());
        selectedLayout = (ConstraintLayout) findViewById(R.id.selectedLayout);
        selectedLayout.setVisibility(View.VISIBLE);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setVisibility(View.INVISIBLE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
       /* LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_about, null, false);
        mDrawerLayout.addView(contentView, 0);*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        ActionBar supportActionBar = getSupportActionBar();

        tvUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsuario);
        tvMail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvMail);
        tvUser.setText(MainActivity.currentUser.getName());
        tvMail.setText(MainActivity.currentUser.getEmail());

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

        ClosetFragment.origen = ClosetFragment.Origin.FAVORITOS;

        // Adding Floating Action Button to bottom right of main view
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setAlpha(1.0f);

        fab.animate()
                .translationY(fab.getHeight())
                .alpha(1.0f)
                .setListener(null);
        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(SelectItemsActivity.this, R.drawable.ic_save));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClosetFragment.tempOutfit != null && ClosetFragment.tempOutfit.isFull()) {

                    new AlertDialog.Builder(SelectItemsActivity.this)
                        .setMessage("Are you sure you want to save these clothes?")
                        .setTitle("Ready to save")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                new BDTarea().execute();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        })
                        .create().show();

                } else {

                    new AlertDialog.Builder(SelectItemsActivity.this)
                        .setMessage("You are still missing some clothes!")
                        .setTitle("Wait!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .create().show();

                }

            }
        });

        createItems();
    }

    private void createItems() {
        coat = new Item();
        coat.setId(-1);
        upper = new Item();
        upper.setId(-1);
        lower = new Item();
        lower.setId(-1);
        shoes = new Item();
        shoes.setId(-1);
    }

    private void disposeItems() {
        coat = null;
        upper = null;
        lower = null;
        shoes = null;
    }

    private void guardarOutfit(){
        Outfit guardable = ClosetFragment.tempOutfit;

        DataBase dataBase = DataBase.getInstance(this);
        dataBase.outfitDAO().insert(guardable);

        //Log.i("SelectItemsActivity", "...................Guardado en BD: " + guardable.toString());

        ClosetFragment.tempOutfit = null;
        DataBase.destroyInstance();

    }

    private void fillCard() {
        Outfit temp = ClosetFragment.tempOutfit;
        DataBase dataBase = DataBase.getInstance(this);

        if (temp.getCoatID() != -1 && coat.getId() != temp.getCoatID()) {
            //Log.i("SelectItemsActivity", "Coat Selected");
            coat = new Item();
            coat.setId(temp.getCoatID());
            coat.setFoto(dataBase.itemDAO().getItemById(coat.getId()).getFoto());

        }
        if (temp.getUpperID() != -1 && upper.getId() != temp.getUpperID()) {
            //Log.i("SelectItemsActivity", "Upper Selected");
            upper = new Item();
            upper.setId(temp.getUpperID());
            upper.setFoto(dataBase.itemDAO().getItemById(upper.getId()).getFoto());
            //System.out.println(upper.toString());

        }
        if (temp.getBottomID() != -1 && lower.getId() != temp.getBottomID()) {
            //Log.i("SelectItemsActivity", "Bottom Selected");
            lower = new Item();
            lower.setId(temp.getBottomID());
            lower.setFoto(dataBase.itemDAO().getItemById(lower.getId()).getFoto());

        }
        if (temp.getShoesID() != -1 && shoes.getId() != temp.getShoesID()) {
            //Log.i("SelectItemsActivity", "Shoes Selected");
            shoes = new Item();
            shoes.setId(temp.getShoesID());
            shoes.setFoto(dataBase.itemDAO().getItemById(shoes.getId()).getFoto());

        }
    }

    private void pasteImages() {
        if (coat != null && coat.getId() != -1) {
            //Log.i("SelectItemsActivity", "Coat Displayed");
            ImageView selectedCoat = findViewById(R.id.selectedCoat);
            selectedCoat.setImageBitmap(decodificarImagen(coat));
        }
        //System.out.println("-------------" + upper.toString());
        if (upper != null && upper.getId() != -1) {
            //Log.i("SelectItemsActivity", "Upper Displayed");
            ImageView selectedUpper = findViewById(R.id.selectedUpper);
            selectedUpper.setImageBitmap(decodificarImagen(upper));
        }
        if (lower != null && lower.getId() != -1) {
            //Log.i("SelectItemsActivity", "Bottom Displayed");
            ImageView selectedBottom = findViewById(R.id.selectedBottom);
            selectedBottom.setImageBitmap(decodificarImagen(lower));
        }
        if (shoes != null && shoes.getId() != -1) {
            //Log.i("SelectItemsActivity", "Shoes Displayed");
            ImageView selectedShoes = findViewById(R.id.selectedShoes);
            selectedShoes.setImageBitmap(decodificarImagen(shoes));
        }
    }

    @NonNull
    private Bitmap decodificarImagen(Item item) {
        Bitmap bm = null;

        try {
            InputStream ent = getResources().getAssets().open("temp.png");
            bm = BitmapFactory.decodeStream(ent);
        } catch (IOException e) {
            Log.i("BD (FavoritosFragment)", "Error: " + e.getMessage());
        }
        int width = 128;
        int height = 128;
        Bitmap.Config configBmp = Bitmap.Config.valueOf(bm.getConfig().name());
        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);

        ByteBuffer buffer = ByteBuffer.wrap(item.getFoto());

        bitmap_tmp.copyPixelsFromBuffer(buffer);
        //Log.i("FavoritosFragment", "Image Decoded: " + bitmap_tmp);
        return bitmap_tmp;
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        disposeItems();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ClosetFragment.tempOutfit != null && ClosetFragment.tempOutfit.hasItems()) {
            new BDFillCard().execute();
        }
    }

    class BDFillCard extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            fillCard();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pasteImages();
        }
    }

    class BDTarea extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            guardarOutfit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            onBackPressed();
            finish();
        }
    }
}
