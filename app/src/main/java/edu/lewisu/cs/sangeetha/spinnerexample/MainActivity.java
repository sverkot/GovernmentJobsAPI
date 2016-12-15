package edu.lewisu.cs.sangeetha.spinnerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerState, spinnerLevel;
    private Button btnSubmit;

    private EditText searchTerm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItemsOnSpinnerLevel();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

    }


    //add items into spinner dynamically
    public void addItemsOnSpinnerLevel() {

        spinnerLevel = (Spinner) findViewById(R.id.spinnerLevel);
        List<String> list = new ArrayList<String>();
        list.add("All");
        list.add("Federal");
        list.add("State");
        list.add("City");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {

        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerState.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    //get the selected dropdown list value
    public void addListenerOnButton() {

        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerLevel = (Spinner) findViewById(R.id.spinnerLevel);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        searchTerm = (EditText) findViewById(R.id.search_term);


        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //https://api.usa.gov/jobs/search.json?size=100&query=nursing+jobs+ny&tags=federal


                String url = "https://api.usa.gov/jobs/search.json?size=100";
                String searchUrl = url;
                String levelQuery = "";
                if (spinnerLevel.getSelectedItem() != "" && spinnerLevel.getSelectedItem() != "All") {

                    levelQuery = "&tags=" + spinnerLevel.getSelectedItem();
                    levelQuery = levelQuery.toLowerCase();
                }

                searchUrl = searchUrl + levelQuery;

                String searchQuery = "";
                if (searchTerm.getText().toString() != "") {
                    searchQuery = searchTerm.getText().toString();
                }

                String state = String.valueOf(spinnerState.getSelectedItem());
                if (state != "") {
                    searchQuery += " in " + state;
                }

                try {
                    if (searchQuery != "") {
                        searchQuery = URLEncoder.encode(searchQuery, "UTF-8")
                                .replaceAll("\\+", "%20")
                                .replaceAll("\\%21", "!")
                                .replaceAll("\\%27", "'")
                                .replaceAll("\\%28", "(")
                                .replaceAll("\\%29", ")")
                                .replaceAll("\\%7E", "~");
                        ;


                        searchUrl += "&query=" + searchQuery;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this, JobsListActivity.class);
                intent.putExtra("url", searchUrl);


                startActivity(intent);
            }

        });

    }
}
