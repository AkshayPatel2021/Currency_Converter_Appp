package com.example.currencyconverterapp;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    TextView convertfromdropdowntextview,converttodropdowntextview, conversionratetext;
    EditText amounttoconvert;
    ArrayList<String> arrayList;
    Dialog fromDialog;
    Dialog toDialog;
    Button convertbutton;
    String convertfromvalue, converttovalue,conversionvalue;
    String[] country={"INR","USD","AUD","EUR"};




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convertfromdropdowntextview=findViewById(R.id.convertfromdropdownmenu);
        converttodropdowntextview=findViewById(R.id.converttodropdownmenu);
        convertbutton=findViewById(R.id.conversionbutton);
        conversionratetext=findViewById(R.id.conversionratetext);
        amounttoconvert=findViewById(R.id.amounttoconvertvalueedittext);

        arrayList = new ArrayList<>();
        for (String i : country){
            arrayList.add(i);
        }
        convertfromdropdowntextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDialog = new Dialog(MainActivity.this);
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(650,800);
                fromDialog.show();

                EditText editText = fromDialog.findViewById(R.id.edit_text);
                ListView listView=fromDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        convertfromdropdowntextview.setText(adapter.getItem(position));
                        fromDialog.dismiss();
                        convertfromvalue = adapter.getItem(position);

                    }
                });

            }
        });

        converttodropdowntextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDialog= new Dialog(MainActivity.this);
                toDialog.setContentView(R.layout.to_spinner);
                toDialog.getWindow().setLayout(650,800);
                toDialog.show();

                EditText editText = toDialog.findViewById(R.id.edit_text);
                ListView listView =toDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter= new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        converttodropdowntextview.setText(adapter.getItem(position));
                        toDialog.dismiss();
                        converttovalue = adapter.getItem(position);

                    }
                });
            }
        });

        convertbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double amounttoconvert = Double.valueOf(MainActivity.this.amounttoconvert.getText().toString());
                    getconversionrate(convertfromvalue, converttovalue, amounttoconvert);
                }catch (Exception e){

                }
            }
        });
    }

    public String getconversionrate(String convertfrom, String convertto, Double amounttoconvert) {
        RequestQueue queue= Volley.newRequestQueue(this);
        String url ="https://free.currencyconverterapi.com/"+convertfrom+"_"+convertto+"&compact=ultra&apiKey=e28c5bce9ae4c0b0c659";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Double conversionratevalue = round(((Double) jsonObject.get(convertfrom + "_" + convertto)), 2);
                    conversionvalue = "" + round((conversionratevalue * amounttoconvert), 2);
                    conversionratetext.setText(conversionvalue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);

        return null;
    }
    public static double round(Double value, int places){
        if(places<0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);bd= bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }
}
