package com.example.root.rsv.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.rsv.models.RSV;
import com.example.root.rsv.R;
import com.example.root.rsv.views.ActivitySetDates;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author: Ioannis Brant Ioannidis
 * @email: giannisbr@gmail.com
 * @date: 15/10/2019
 */
public class FragmentRSVs extends Fragment{
    private static final String TAG = FragmentRSVs.class.getName();
    private static final int LOADER_ID = 477;


    private static String response;
    private Context context;
    @BindView(R.id.fab_upload_rsv) FloatingActionButton btnUpload;
    public TabLayout tabLayout;
    public static ArrayList<RSV> list,list1;

    public FragmentRSVs(){}

    public static FragmentRSVs newInstance() {
        return new FragmentRSVs();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = Objects.requireNonNull(getActivity());

        ButterKnife.bind(this,Objects.requireNonNull(getView()));

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_main);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_feed);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_vertical);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_calendar);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume(){
        super.onResume();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_rsvs, container, false);
        btnUpload = (FloatingActionButton)  v.findViewById(R.id.fab_upload_rsv);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySetDates.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle bundle = new Bundle();
       // bundle.putInt("USERID", user_id);

        NestedFragmentFeed feedFragment = new NestedFragmentFeed();
        NestedFragmentRSVs rsvsFragment= new NestedFragmentRSVs();
        NestedFragmentCustomRSVs customRsvsFragment = new NestedFragmentCustomRSVs();

        adapter.addFrag(feedFragment, "Feed");
        adapter.addFrag(rsvsFragment,"RSVs");
        adapter.addFrag(customRsvsFragment, "Calendar");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}


