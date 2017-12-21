package in.co.erailway.erailway.Animation;

/**
 * Created by paln on 18/5/2017.
 */

public class MyBounceAnimation implements android.view.animation.Interpolator {
	double mAmplitude = 1;
	double mFrequency = 10;

	public MyBounceAnimation(double amplitude, double frequency) {
		mAmplitude = amplitude;
		mFrequency = frequency;
	}

	public float getInterpolation(float time) {
		return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
				Math.cos(mFrequency * time) + 1);
	}
}
