package in.co.erailway.erailway;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class IntroActivity extends AppIntro{
	@Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(first_fragment);
//        addSlide(second_fragment);
//        addSlide(third_fragment);
//        addSlide(fourth_fragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
//        addSlide(AppIntroFragment.newInstance("title", "description", R.drawable.ic_launcher, Color.parseColor("#2196F3")));
//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));
//
//        // Hide Skip/Done button.
//        showSkipButton(false);
//        setProgressButtonEnabled(false);
//
//        // Turn vibration on and set intensity.
//        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
		final String title1 = "Hey There!!";
		final String description1 = "Looking for Train Info?! eRailway provides you with live and detailed info about trains to make your life easy and aweeesome!!";
		final String title2 = "Live Status";
		final String description2 = "Looking for Train Info?! RailInfo provides you with live and detailed info about trains to make your life easy and aweeesome!!";
		final String title3 = "PNR Status";
		final String description3 = "Looking for Train Info?! RailInfo provides you with live and detailed info about trains to make your life easy and aweeesome!!";
		addSlide(SampleSlide.newInstance(R.layout.intro));
		addSlide(AppIntroFragment.newInstance(title1, description1, R.drawable.ic_slide1, Color.parseColor("#33B5E5")));
		addSlide(AppIntroFragment.newInstance(title2, description1, R.drawable.ic_slide1, Color.parseColor("#5C6BC0")));
		addSlide(AppIntroFragment.newInstance(title3, description1, R.drawable.ic_slide1, Color.parseColor("#4CAF50")));
		showSkipButton(false);
		setFadeAnimation();
    }

    @Override
    public void onSkipPressed() {
    // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		finish();
    }

        @Override
    public void onSlideChanged() {
    }

    @Override
    public void onNextPressed() {
    }

}
