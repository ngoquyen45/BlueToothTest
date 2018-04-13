package com.teme.hieu.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    Button btnPaired;
    //Bluetooth
    private BluetoothAdapter myBlue = null;
    private Set<BluetoothDevice> paired;
    public static String EXTRA_ADDRESS = "devices_address";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // call the widgets
        lv = (ListView)findViewById(R.id.lv);
        btnPaired = (Button)findViewById(R.id.btnPD);
        // set Bluetooth
        myBlue = BluetoothAdapter.getDefaultAdapter();
        if(myBlue == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else if(!myBlue.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevices();
            }
        });

    }
    private void pairedDevices()
    {
      paired = myBlue.getBondedDevices();
      ArrayList list = new ArrayList();
      if (paired.size()>0)
          for (BluetoothDevice device : paired)
          {
              list.add(device.getName()+"\n"+device.getAddress());
          }
       else
      { Toast.makeText(getApplicationContext(),"Nothing to show",Toast.LENGTH_LONG).show();}
      final ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,list);
      lv.setAdapter(adapter);
      lv.setOnItemClickListener(myClick);
    }
    private AdapterView.OnItemClickListener myClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //get MAC address
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);
            // creat intent
            Intent go = new Intent(getApplicationContext(),control.class);
            // chuyen man hinh
            go.putExtra(EXTRA_ADDRESS,address);
            startActivity(go);
        }
    };
}
