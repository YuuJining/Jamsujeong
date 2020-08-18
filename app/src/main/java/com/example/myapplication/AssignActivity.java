package com.example.myapplication;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class AssignActivity extends AppCompatActivity {

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setPageTransformer(true, new DepthPageTransformer());
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        Fragment_snapzone_assign snapzone = new Fragment_snapzone_assign();
        pagerAdapter.addItem(snapzone);

        Fragment_forest_assign forest = new Fragment_forest_assign();
        pagerAdapter.addItem(forest);

        Fragment_library_assign library = new Fragment_library_assign();
        pagerAdapter.addItem(library);

        pager.setAdapter(pagerAdapter);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public PagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
                return "snapzone";
            } else if (position == 1) {
                return "forest";
            } else {
                return "library";
            }
        }
    }
}
