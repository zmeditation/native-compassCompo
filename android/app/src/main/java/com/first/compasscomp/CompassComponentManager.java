package com.first.compasscomp;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.first.R;

public class CompassComponentManager extends SimpleViewManager<CompassComponent> {

    public static final String REACT_CLASS = "RCTMyCustomView";
    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    protected CompassComponent createViewInstance(@NonNull @org.jetbrains.annotations.NotNull ThemedReactContext reactContext) {
        LayoutInflater inflater = (LayoutInflater) reactContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CompassComponent view = (CompassComponent)inflater.inflate(R.layout.custom_view, null);
        return view;
    }

    @ReactProp(name = "bearing")
    public void setBearing(CompassComponent view, double degree) {
        view.setDegree(degree);
    }

    @ReactProp(name = "width")
    public void setWidth(CompassComponent view, double width) {
        view.setCompassWidth(width);
    }

    @ReactProp(name = "height")
    public void setHeight(CompassComponent view, double height) {
        view.setCompassHeight(height);
    }

    @ReactMethod
    public void accuracyCallBack(CompassComponent view, Callback callback){
        double accuracy = (double)view.getAccuracy();
        callback.invoke(accuracy);
    }

}
