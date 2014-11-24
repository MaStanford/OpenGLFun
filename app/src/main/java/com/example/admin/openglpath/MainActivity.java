package com.example.admin.openglpath;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.admin.openglpath.data.DrawableStateManager;
import com.example.admin.openglpath.gestures.GestureHandler;

public class MainActivity extends Activity implements NumberPicker.OnValueChangeListener{

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
                DrawableStateManager.getInstance().setCurrentShapeToDraw(GestureHandler.CARD);
                break;
            case R.id.action_stroke:
                DrawableStateManager.getInstance().setCurrentShapeToDraw(GestureHandler.STROKE);
                break;
            case R.id.action_line_size:
                showDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Line Size");
        dialog.setContentView(R.layout.dialog);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setValue((int)DrawableStateManager.getInstance().getCurrentLineWidth());
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        ((Button)dialog.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DrawableStateManager.getInstance().setCurrentLineWidth(np.getValue());
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i2) {
        //Here is the changes
    }
}
