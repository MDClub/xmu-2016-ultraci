package com.uitraci.hotel.hotel;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import com.uitraci.hotel.voice.LoginVoice;

/**
 * 当系统第一次启动时进入该页面，刷NFC卡跳转到主页面
 * @author 谢波
 * create by 2016/3/18 0018 12:21
 */
public class GuideActivity extends AppCompatActivity {

    //Guide界面的页数，如果设置大于1，左右滑动有不同页面
    private static int NUM_OF_GUIDEPAGE = 1;
    private AlertDialog dialog;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //刷客服的NFC卡跳转到主页面
        if (is_Validate_NFC())
        {
            enterHome();
        } else{
            //弹出对话框:刷卡失败
            show_fail_dialog();
        }
    }

    private void show_fail_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuideActivity.this);
        View view = View.inflate(GuideActivity.this, R.layout.failed_swingcard_dialog, null);
        Button scdialog_btn_ok = (Button) view.findViewById(R.id.scdialog_btn_ok);
        scdialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    //进入主页面
    private void enterHome() {
        Intent intent = new Intent(GuideActivity.this, LoginVoice.class);
        startActivity(intent);
        finish();
    }

    //TODO:NFC卡是否有效
    private boolean is_Validate_NFC() {
        return true;
//        return false;
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_guide, container, false);
            //   TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //    textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return NUM_OF_GUIDEPAGE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Splash 1";
                case 1:
                    return "Splash 2";
                case 2:
                    return "Splash 3";
            }
            return null;
        }
    }
}
