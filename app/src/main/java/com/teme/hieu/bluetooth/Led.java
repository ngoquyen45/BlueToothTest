package com.teme.hieu.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;



public class Led extends AppCompatActivity {
    Button btnOn , btnOff;
    SeekBar brightness , brightness2;
    TextView lumn,lumn2;
    String address = null;
    BluetoothAdapter myBlue = null;
    BluetoothSocket mySocket = null;
    private boolean isBTconnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS);
        // Anh xa
        btnOn=findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
        brightness = findViewById(R.id.brightness);
        brightness2 = findViewById(R.id.brightness2);
        lumn = findViewById(R.id.lumn);
        lumn2 = findViewById(R.id.lumn2);
        // connect
        connectBT();
        //set button
        btnOn.setOnClickListener(myclick);
        btnOff.setOnClickListener(myclick);
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    lumn.setText(String.valueOf(progress));
                    try
                    {
                        mySocket.getOutputStream().write(String.valueOf(progress).getBytes());
                    }
                    catch (IOException e) {
                        msg("Error");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightness2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    lumn2.setText(String.valueOf(progress));
                    try {
                        mySocket.getOutputStream().write(String.valueOf(-progress).getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("ERROR");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private   View.OnClickListener myclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnOn:
                {
                    turnOnLed();
                    break;
                }
                case R.id.btnOff:
                {
                    turnOffLed();
                    break;
                }
                default:break;
            }
        }
    };

    private void turnOffLed()
    {
        if (mySocket!=null)
        {
            try
            {
                mySocket.getOutputStream().write("TF".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (mySocket!=null)
        {
            try
            {
                mySocket.getOutputStream().write("TO".getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    void msg(String a)
    {
        Toast.makeText(Led.this,a,Toast.LENGTH_SHORT).show();
    }

     public void connectBT()
        {
            boolean Connected = true;
          try
          {
              if(mySocket == null || !isBTconnected)
              {
                  myBlue = BluetoothAdapter.getDefaultAdapter();
                  BluetoothDevice takeControl = myBlue.getRemoteDevice(address);
                  mySocket = takeControl.createInsecureRfcommSocketToServiceRecord(myUUID);
                  BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                  mySocket.connect();
              }
          }
          catch (IOException e)
          {
              Connected = false;
          }
        if (!Connected)
        {
            msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            finish();
        }
        else
        {
            msg("Connected.");
            isBTconnected = true;
        }
    }
}
