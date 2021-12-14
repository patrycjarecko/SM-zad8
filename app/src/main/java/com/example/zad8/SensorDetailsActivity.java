package com.example.zad8;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String KEY_EXTRA_SENSOR = "sensorId";
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView sensorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);
        int id = Integer.parseInt(String.valueOf(getIntent().getExtras().get(KEY_EXTRA_SENSOR)));

        sensorTextView = findViewById(R.id.light_sensor_label);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ALL).get(id);

        if (sensor.getName() == "Goldfish Light sensor"){
            sensorTextView = findViewById(R.id.light_sensor_label);
        }
        if (sensor.getName() == "Goldfish Ambient Temperature sensor"){
            sensorTextView = findViewById(R.id.temperature_sensor_label);
        }

        if (sensor == null){
            sensorTextView.setText(R.string.missing_sensor);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        float percent;

        switch (sensorType){
            case Sensor.TYPE_LIGHT:
                percent = (currentValue+2)/40000;
                if (currentValue < 1000){sensorTextView.setBackgroundColor(Color.argb(0, 0, 0, 0));}
                else {sensorTextView.setBackgroundColor(Color.argb(255, percent*255-25, 0, 0)); }
                sensorTextView.setText(getResources().getString(R.string.light_sensor_label, currentValue));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:

                if (currentValue < 0){
                    percent = (-1)*(currentValue+2)/273;
                    float s = (int)(255-percent*255);
                    sensorTextView.setBackgroundColor(Color.argb(150, 0, 0, (int)(255-percent*255)));
                }
                else{
                    percent = (currentValue+2)/100;
                    if (currentValue < 6) {sensorTextView.setBackgroundColor(Color.argb(0, 0, 0, 0));}
                    else{sensorTextView.setBackgroundColor(Color.argb(255, percent*255-25, 0, 0));}
                }
                sensorTextView.setText(getResources().getString(R.string.temperature_sensor_label, currentValue));
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}