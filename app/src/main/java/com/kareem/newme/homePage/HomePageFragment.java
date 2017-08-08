package com.kareem.newme.homePage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.newme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment {

   private SliderLayout sliderLayout;
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
        setSlider(sliderLayout);
        return view;
    }

    private void setSlider(final SliderLayout layout)
    {
        FirebaseDatabase.getInstance().getReference("News-2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                layout.removeAllSliders();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                        ) {
                    layout.addSlider(initTexSlider(snapshot.child("title").getValue().toString() , snapshot.child("image_url").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private TextSliderView initTexSlider(String text, String url)
    {
        TextSliderView textSliderView = new TextSliderView(getActivity());
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
}
