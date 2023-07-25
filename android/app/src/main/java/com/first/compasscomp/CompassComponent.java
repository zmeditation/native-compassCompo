package com.first.compasscomp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.first.R;

public class CompassComponent extends RelativeLayout {

    private double degree = 0;
    private Compass compass;
    private ImageView arrowView;
    private ImageView backgroundView;
    private TextView sotwLabel;
    private float currentAzimuth;
    private float currentArrowAzimuth;
    private SOTWFormatter sotwFormatter;
    private Context mContext;
    public  int accuracy = 0;

    public CompassComponent(Context context) {
        super(context);


    }

    private void setupCompass() {
        compass = new Compass(mContext);
        Compass.CompassListener cl = getCompassListener();
        compass.setListener(cl);

        Compass.CompassAccuracyListener compassAccuracyListener = getAccuracyCompassListener();
        compass.setAccuracyListener(compassAccuracyListener);



    }

    public CompassComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setPadding(8, 8, 8, 8);

        sotwFormatter = new SOTWFormatter(context);
        backgroundView = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.getWidth()/5*2, this.getWidth()/5*2);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backgroundView.setLayoutParams(params);
        backgroundView.setImageResource(R.drawable.dial);
        arrowView = new ImageView(context);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(this.getWidth()/5*2 - 20, this.getWidth()/5*2 - 20);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        arrowView.setLayoutParams(params1);
        arrowView.setImageResource(R.drawable.hands);
        

        sotwLabel = new TextView(context);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.setMargins(20, 10, 20, 10);
        sotwLabel.setLayoutParams(params2);
        addView(backgroundView);
        addView(arrowView);
        addView(sotwLabel);
        this.mContext = context;
        setupCompass();
        start();
        requestLayout();
        requestFocus();
    }

    public CompassComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CompassComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public void setCompassWidth(double width) {
      
        backgroundView.getLayoutParams().width = (int)(convertPxToDp(this.mContext, (float) width));
        backgroundView.requestLayout();

        arrowView.getLayoutParams().width = (int) (convertPxToDp(this.mContext, (float) width-20));
        arrowView.requestLayout();
    }

    public void setCompassHeight(double height) {
        backgroundView.getLayoutParams().height = (int)(convertPxToDp(this.mContext, (float) height));;
        backgroundView.requestLayout();

        arrowView.getLayoutParams().height = (int) (convertPxToDp(this.mContext, (float) height-20));
        arrowView.requestLayout();
    }


    public int getAccuracy() {
        return accuracy;
    }

    public void onFinishInflate() {
        super.onFinishInflate();

    }
    private Compass.CompassListener getCompassListener() {
        return new Compass.CompassListener() {
            @Override
            public void onNewAzimuth(final float azimuth) {
                // UI updates only in UI thread

                UiThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustArrow(azimuth);
                        adjustBackground(azimuth);
                        adjustSotwLabel(azimuth);
                    }
                });
            }


        };
    }

    private Compass.CompassAccuracyListener getAccuracyCompassListener() {
        return new Compass.CompassAccuracyListener() {
            @Override
            public void onAccuracyChanged(int accuracy) {
                CompassComponent.this.accuracy = accuracy;
            }
        };
    }


    private void adjustArrow(float azimuth) {
        Animation an = new RotateAnimation( -currentArrowAzimuth, -azimuth ,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentArrowAzimuth = azimuth + (float) this.degree;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        arrowView.startAnimation(an);
    }

    private void adjustBackground(float azimuth) {


        Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        currentAzimuth = azimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        backgroundView.startAnimation(an);
    }

    private void adjustSotwLabel(float azimuth) {
        sotwLabel.setText(sotwFormatter.format(azimuth));
    }

    public void start(){
        compass.start();
    }

    public void stop(){
        compass.stop();
    }

    public float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }



}
