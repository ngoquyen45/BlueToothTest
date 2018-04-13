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

public class control extends AppCompatActivity {
    int status = 0;
    Button btnOn, btnOff;
    TextView txtvl;
    SeekBar brightness;
    String address = null;
    BluetoothAdapter myBlue = null;
    BluetoothSocket mySocket = null;
    private boolean isBTconnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent nhan = getIntent();
        address = nhan.getStringExtra(MainActivity.EXTRA_ADDRESS);
        connectBT();
        btnOn = findViewById(R.id.on);
        btnOff = findViewById(R.id.off);
        txtvl = findViewById(R.id.txtvl);
       brightness = findViewById(R.id.seekBar);
        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               turnOnLed();
            }
        });
        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               turnOffLed();
            }
        });
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    if(status==1)
                    {
                        if (mySocket != null) {
                              try{
                                mySocket.getOutputStream().write(getString(progress).getBytes());
                                txtvl.setText(getString(progress));}
                                catch (IOException e)
                                {}
                        }
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
        /*if(status==1)
            txtvl.setText(R.string.turned);
        else
            txtvl.setText(R.string.unturned);*/
    }
    private void turnOffLed ()
        {
            if (mySocket != null) {
                try {
                    mySocket.getOutputStream().write("ON".getBytes());

                } catch (IOException e) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        private void turnOnLed ()
        {
            if (mySocket != null) {
                try {
                    mySocket.getOutputStream().write("OFF".getBytes());

                } catch (IOException e) {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
        public void connectBT ()
        {
            boolean Connected = true;
            try {
                if (mySocket == null || !isBTconnected) {
                    myBlue = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice takeControl = myBlue.getRemoteDevice(address);
                    mySocket = takeControl.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mySocket.connect();
                }
            } catch (IOException e) {
                Connected = false;
            }
            if (!Connected) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected.");
                isBTconnected = true;
            }
        }
        public void msg(String a)
        {
            Toast.makeText(this,a,Toast.LENGTH_SHORT).show();
        }
}
