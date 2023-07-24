package com.sanskrit.pmo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    Adapter adapter;
    int position = 0;
    ViewPager viewPager;

    class Adapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitles = new ArrayList();
        private final List<Fragment> mFragments = new ArrayList();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragments.add(fragment);
            this.mFragmentTitles.add(title);
        }

        public Fragment getItem(int position) {
            return (Fragment) this.mFragments.get(position);
        }

        public int getCount() {
            return this.mFragments.size();
        }

        public CharSequence getPageTitle(int position) {
            return (CharSequence) this.mFragmentTitles.get(position);
        }
    }

    /*public static HomeFragment newInstance(int position) {
        HomeFragment f = new HomeFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.POSITION, position);
        f.setArguments(b);
        return f;
    }*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) rootView.findViewById(R.id.toolbar));
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getString(R.string.app_name));
        if (getArguments() != null) {
            this.position = getArguments().getInt(Constants.POSITION);
        }
        this.viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        if (this.viewPager != null) {
            setupViewPager(this.viewPager);
            this.viewPager.setOffscreenPageLimit(2);
        }
        ((TabLayout) rootView.findViewById(R.id.tabs)).setupWithViewPager(this.viewPager);
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        this.adapter = new Adapter(getChildFragmentManager());
        this.adapter.addFragment(new FeedFragment(), getString(R.string.tabs_latest_news));
        this.adapter.addFragment(new MediaCoverageFragment(), getString(R.string.tabs_media_coverage));
        this.adapter.addFragment(new SocialFeedFragment(), getString(R.string.tabs_social_feed));


        viewPager.setAdapter(this.adapter);
        viewPager.setCurrentItem(this.position);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = this.adapter.getItem(1);
            if (fragment != null && (fragment instanceof SocialFeedFragment)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
