package mx.itesm.rueschan.moviles;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mx.itesm.rueschan.moviles.BD.DataBase;
import mx.itesm.rueschan.moviles.EntidadesBD.Item;

public class SugeridosActivity extends AppCompatActivity {

    //verificar cantidad datos
    private int tipoItems[] = {0,0,0,0};
    String item[] = {"Shoes", "Bottom","Top","Coats"};

    //Controlador
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    //Verificar cantidad de datos
    private String camafeo[]; //colores cercanos
    //combinar prendas de colores cercanos en el círculo cromático, como por ejemplo el beige y el marrón.

    private String circuloCromatico[] = {"#FFE600", "#FFCE00", "#FFB300", "#FF8300", "#FF4701", "#FA1037", "#81F000", "#00BC4A"
            , "#00952D", "#0088E1", "#00B9FF", "#1046C7", "#D971FF", "#C415C9", "#8D08B5"};
    //amarillo, rojo, rojo osc, morado, azul osc, azul calro, verde osc;
    private String contraste[]; //colores opuestos
    private String universales[] = {"#000000", "#FFFFFF", "#C3C3C3"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpView(viewPager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBar supportActionBar = getSupportActionBar();

        ClosetFragment.origen = ClosetFragment.Origin.SUGERIDOS;
        String errorMsg = "You don't have enough ";
        for (int i = 0; i < tipoItems.length ; i++) {
            if(tipoItems[i] < 1)
                errorMsg += item[i] + " ";
        }

        new AlertDialog.Builder(this)
                .setMessage(errorMsg + " items")
                .setTitle("Sorry")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                })
                .create().show();
    }

    private void cargarDatos() {
        // BD
        DataBase bd = DataBase.getInstance(this);
        List<Item> items = bd.itemDAO().getAllItemsByUserId(MainActivity.currentUser.getIdUser());

        //System.out.println("*********" + items.size());
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getTipo().equalsIgnoreCase("Shoes"))
                tipoItems[0]++;
            if (items.get(i).getTipo().equalsIgnoreCase("Bottom"))
                tipoItems[1]++;
            if (items.get(i).getTipo().equalsIgnoreCase("Top"))
                tipoItems[2]++;
            if (items.get(i).getTipo().equalsIgnoreCase("Coats"))
                tipoItems[3]++;
        }

    }

    private ArrayList calcularCombinaciones(String color, int index) {

        for (int i = 0; i < universales.length; i++) {
            if (color.equalsIgnoreCase(universales[i]))
                return null;
        }
        ArrayList<String> combinacionesArray = new ArrayList<>(5);
        for (int i = 0; i < circuloCromatico.length; i++) {
            if (i >= index - 2 && i <= index + 2) {
                combinacionesArray.add(circuloCromatico[i]);
            }
        }

        return combinacionesArray;
    }

    private int getIndex(String color) {
        for (int i = 0; i < circuloCromatico.length; i++) {
            if (circuloCromatico[i].equalsIgnoreCase(color))
                return i;
        }

        return -1;
    }




    private void setUpView(ViewPager viewPager) {
        SelectItemsActivity.Adapter adapter = new SelectItemsActivity.Adapter(getSupportFragmentManager());
        adapter.addFragment(new ClosetFragment(), "Select your clothes");

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
        ClosetFragment.origen = ClosetFragment.Origin.MAIN;
        finish();
    }

    // Para cargar los datos en segundo plano
    class BDOutfit extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            cargarDatos();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Nuevos datos para el adaptador
            //FavoritosFragment.ControllerAdapter adapt = (FavoritosFragment.ControllerAdapter)recyclerView.getAdapter();
            //adapt.setDatos(arrIDs, arrNames, arrCoats, arrUppers, arrBottoms, arrShoes);
            //adapt.notifyDataSetChanged();
        }
    }
}
