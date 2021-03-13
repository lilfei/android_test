package com.zzx.okhttptest.opengl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class OpenGLActivity extends Activity {

    private GLSurfaceView gLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.

        Log.e("react", "onCreate: hahahah");

        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);

    }


}
