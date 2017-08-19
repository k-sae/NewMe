package com.kareem.newme.homePage;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.newme.Model.News;
import com.kareem.newme.NavigationActivityCallBack;
import com.kareem.newme.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements View.OnClickListener, ViewPagerEx.OnPageChangeListener
{

   private SliderLayout sliderLayout;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> descriptionString = new ArrayList<>();
    private TextView description;
    private TextView title;
    private Activity parent;
    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
          sliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.addOnPageChangeListener(this);
        description = (TextView) view.findViewById(R.id.home_slider_description);
        title = (TextView) view.findViewById(R.id.home_slider_title);
        setSlider(sliderLayout);
        setListeners(view);
        return view;
    }

    private void setSlider(final SliderLayout layout)
    {
        FirebaseDatabase.getInstance().getReference("News-2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                layout.removeAllSliders();
                titles.clear();
                descriptionString.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                        ) {
                    News news = snapshot.getValue(News.class);
                    String title = news.getTitle();
                    String Description = news.getContent();
                    titles.add(title);
                    descriptionString.add(Description);
                    layout.addSlider(initTexSlider(title, news.getImage_url()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private DefaultSliderView initTexSlider(String text, String url)
    {
        DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
        // initialize a SliderLayout
        textSliderView
                .description(text).image(url)
                .setScaleType(BaseSliderView.ScaleType.Fit).error(R.mipmap.default_image_news);

        //add your extra information
        textSliderView.bundle(new Bundle());
        textSliderView.getBundle()
                .putString("extra","text");
        return textSliderView;
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    public void setListeners(View view) {
        view.findViewById(R.id.nav_FAQََ).setOnClickListener(this);
        view.findViewById(R.id.nav_news).setOnClickListener(this);
        view.findViewById(R.id.nav_login).setOnClickListener(this);
        view.findViewById(R.id.nav_contact_us).setOnClickListener(this);
        view.findViewById(R.id.nav_about).setOnClickListener(this);
        view.findViewById(R.id.nav_newme);

        //whatsapp
        view.findViewById(R.id.home_whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:" + "+965 6667 9725");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.putExtra("sms_body", "");
                i.setPackage("com.whatsapp");
                startActivity(i);

            }
        });
        //instagram
        view.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://instagram.com/dr.hanadialbadir");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/dr.hanadialbadir")));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        ((NavigationActivityCallBack) getActivity()).setActive(v.getId());

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        description.setText(descriptionString.get(position));
        title.setText(titles.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
    }
}
