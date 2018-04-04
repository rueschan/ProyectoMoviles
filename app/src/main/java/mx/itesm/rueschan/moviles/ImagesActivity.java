package mx.itesm.rueschan.moviles;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ClosetFragment.clicked);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewPagerPhotos);
        setUpView(viewPager);

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

                    if (selected.equals(types[0])) {
                        ClosetFragment.tempOutfit.setUpperID(id);
                    } else if (selected.equals(types[1])) {
                        ClosetFragment.tempOutfit.setBottomID(id);
                    } else if (selected.equals(types[2])) {
                        ClosetFragment.tempOutfit.setCoatID(id);
                    } else if (selected.equals(types[3])) {
                        ClosetFragment.tempOutfit.setShoesID(id);
                    }
                    onBackPressed();

                } else {
                    startActivity(new Intent(view.getContext(), TakePhoto.class));
                }
            }
        });

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


}
