package edu.lewisu.cs.sangeetha.spinnerexample;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by sangeetha on 12/14/16.
 */

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        Toast.makeText(adapterView.getContext(),
//                "OnItemSelectedListener : " + adapterView.getItemAtPosition(i).toString(),
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
