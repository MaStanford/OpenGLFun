package com.example.admin.openglpath;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.admin.openglpath.data.DataHolder;
import com.example.admin.openglpath.gestures.GestureHandler;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_card:
                DataHolder.getInstance().setmCurrentShapeToDraw(GestureHandler.CARD);
                break;
            case R.id.action_stroke:
                DataHolder.getInstance().setmCurrentShapeToDraw(GestureHandler.STROKE);
                break;
            case R.id.action_point:
                DataHolder.getInstance().setmCurrentShapeToDraw(GestureHandler.STROKE_POINTS);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
