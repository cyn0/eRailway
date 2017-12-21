package in.co.erailway.erailway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
		final String description1 = "Looking for Train Info?! eRailway provides you with live and detailed info about Trains to make your life simple and aweeesome!!";
		final String title2 = "Live Status";
		final String description2 = "Worried whether your Train will reach on time? eRailway provides you with running status of Trains.";
		final String title3 = "Train details, PNR Status and much more!";
		final String description3 = "eRailway gives you current PNR status, details about Trains and Railway station. Let's get started!";
		addSlide(SampleSlide.newInstance(R.layout.intro));
		addSlide(AppIntroFragment.newInstance(title1, description1, R.drawable.boy_intro, Color.parseColor("#33B5E5")));
		addSlide(AppIntroFragment.newInstance(title2, description2, R.drawable.live_icon, Color.parseColor("#5C6BC0")));
		addSlide(AppIntroFragment.newInstance(title3, description3, R.drawable.intro_three, Color.parseColor("#4CAF50")));
		showSkipButton(false);
		setFadeAnimation();
    }

    @Override
    public void onSkipPressed() {
    // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
		setFirstTimeStatus(false);
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

	public void setFirstTimeStatus(boolean status){
		Context context = getApplicationContext();
		SharedPreferences.Editor editor = context.getSharedPreferences(Constants.APP_SETTINGS, context.MODE_PRIVATE).edit();
		editor.putBoolean(Constants.FIRST_TIME, status);
		editor.commit();
	}
}
