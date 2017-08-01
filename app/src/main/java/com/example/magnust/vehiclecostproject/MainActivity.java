package com.example.magnust.vehiclecostproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnCalc;
    private TextView txtTotCost, txtAvgCostPrKm, txtDailyCost, txtMarginalCost, txtMarginalFuelCost;
    private EditText edtxtValue, edtxtNumberOfYears, edtxtDistance;
    private CheckBox newcar;
    private int yrs;
    private int value = 0;
    private int carSize = 0;
    private int distance = 0;
    private boolean newCar = false;
    private VehicleCost car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.spinner_car_size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
                spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        //button
        btnCalc = (Button) findViewById(R.id.btnCalc);

        // textview
        txtTotCost = (TextView) findViewById(R.id.txtTotCost);
        txtAvgCostPrKm = (TextView) findViewById(R.id.txtAvgCostPrKm);
        txtDailyCost = (TextView) findViewById(R.id.txtDailyCost);
        txtMarginalCost = (TextView) findViewById(R.id.txtMarginalCost);
        txtMarginalFuelCost = (TextView) findViewById(R.id.txtMarginalFuelCost);
        // edittext
        edtxtValue = (EditText) findViewById(R.id.edtxtValue);
        edtxtNumberOfYears = (EditText) findViewById(R.id.edtxtYears);
        edtxtDistance = (EditText) findViewById(R.id.edtxtDistance);

        //Checkbox
        newcar = (CheckBox) findViewById(R.id.checkBox);

        addCalc();

    }


    public void addCalc() {
        btnCalc.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        yrs = Integer.parseInt(edtxtNumberOfYears.getText().toString());
                        value = Integer.parseInt(edtxtValue.getText().toString());
                        distance = Integer.parseInt(edtxtDistance.getText().toString());
                        newCar = newcar.isChecked();
                        car = new VehicleCost(yrs, carSize, value, distance, newCar);
                        txtTotCost.setText(String.format("%.1f", car.getTotCost())+ " kr");
                        txtAvgCostPrKm.setText(String.format("%.2f", car.getAvgCostPrKm())+ " kr");
                        txtDailyCost.setText(String.format("%.1f", car.getAvgCostPrDay())+ " kr");
                        txtMarginalCost.setText(String.format("%.2f", car.getMarginalCostPr1Km())+ " kr");
                        txtMarginalFuelCost.setText(String.format("%.2f", car.getMarginalCostFuel())+ " kr");
                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View v,
                               int position, long id) {
        carSize = position;
        Toast.makeText(this,Integer.toString(position),Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
