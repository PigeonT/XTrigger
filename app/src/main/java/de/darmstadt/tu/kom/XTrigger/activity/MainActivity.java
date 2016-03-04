package de.darmstadt.tu.kom.XTrigger.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.fragments.ConfigurationFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.HomeFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Config.setContext(getApplicationContext());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.pagerSlidingTabStrip);

        pagerAdapter = this.new XTriggerPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOffscreenPageLimit(3);
        tabs.setAllCaps(true);
        tabs.setShouldExpand(true);
        tabs.setBackgroundColor(Color.argb(100, 63, 81, 181));
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(1);
        getLastCustomNonConfigurationInstance();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_status:
                Intent i = new Intent(this, StatusActivity.class);
                startActivity(i);
                return true;
            default:
                return false;
        }

    }

    private final class XTriggerPagerAdapter extends FragmentPagerAdapter {
        public XTriggerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ConfigurationFragment();

                case 1:
                    return new HomeFragment();

                case 2:
                    return new SettingFragment();

                default:
                    return new HomeFragment();

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Konfiguration";
                case 1:
                    return "Home";
                case 2:
                    return "Einstellung";
                default:
                    return null;
            }
        }


    }
}
