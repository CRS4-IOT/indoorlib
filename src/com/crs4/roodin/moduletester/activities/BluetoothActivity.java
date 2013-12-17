package com.crs4.roodin.moduletester.activities;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.crs4.roodin.moduletester.R;
import android.bluetooth.*;


public class BluetoothActivity  extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;

    ListView listDevicesFound;
    Button btnScanDevice;
    TextView stateBluetooth;
    BluetoothAdapter bluetoothAdapter;

    ArrayAdapter<String> btArrayAdapter;
    private TextView nearDeviceName;
    private TextView nearDeviceSignal;
    private TextView scanTime;
    private boolean scanSession = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_prova);

        btnScanDevice = (Button)findViewById(R.id.scandevice);

        stateBluetooth = (TextView)findViewById(R.id.bluetoothstate);
        nearDeviceName = (TextView)findViewById(R.id.near_device_name);
        nearDeviceSignal = (TextView)findViewById(R.id.near_device_signal);
        scanTime = (TextView)findViewById(R.id.scan_time);
        scanTime.setTag("0");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listDevicesFound = (ListView)findViewById(R.id.devicesfound);
        btArrayAdapter = new ArrayAdapter<String>(BluetoothActivity.this, android.R.layout.simple_list_item_1);
        listDevicesFound.setAdapter(btArrayAdapter);

        checkBluetoothState();

        btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(foundReceiver, filter);

        // BLUETOOTH LOW ENERGY CHECK
        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "bluetooth low energy not supported", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }
    int i = 0;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(foundReceiver);
    }

    private void checkBluetoothState(){
        if (bluetoothAdapter == null){
            stateBluetooth.setText("Bluetooth NOT support");
        }else{
            if (bluetoothAdapter.isEnabled()){
                if(bluetoothAdapter.isDiscovering()){
                    stateBluetooth.setText("Bluetooth is currently in device discovery process.");
                }else{
                    stateBluetooth.setText("Bluetooth is Enabled.");
                    btnScanDevice.setEnabled(true);
                }
            }else{
                stateBluetooth.setText("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private Button.OnClickListener btnScanDeviceOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            discoverDevices();
        }
    };

    private void discoverDevices(){
        btArrayAdapter.clear();
        scanSession = true;

        bluetoothAdapter.startDiscovery();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while(scanSession)
                {
                    try {
                        Log.d("--btdevice", "" + i++);
                        runOnUiThread(new Runnable() {
                            public void run() {
                              scanTime.setText("Attempt: " + Integer.toString(i));
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            checkBluetoothState();
        }
    }

    private final BroadcastReceiver foundReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("--btdevice " , "scanning..." + action);

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("--btdevice " , "new decvice found");

                //new TrackDevice(BluetoothActivity.this).execute(intent);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("--btdevice " , "Near Device: " + device.getName() + " (" + device.getAddress() + ")");
                int  rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) Integer.MIN_VALUE);
                Log.d("--btdevice " , "Signal: " + rssi + " dBm");

                btArrayAdapter.add(device.getName() + "\n" + device.getAddress() + "\n" + " RSSI: " + rssi + "dBm");
                btArrayAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("--btdevice " , "scan finiscsssssss");
                scanSession = false;
                i = 0;

                try {
                    Thread.sleep(1000);
                    discoverDevices();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }};

}

class TrackDevice extends AsyncTask<Intent, Void, Boolean> {
    private Activity mActivity;
    public TrackDevice(Activity context) {
        //super(context, true);
        mActivity = context;
    }


    @Override
    protected Boolean doInBackground(Intent... intents) {
        Log.d("--btdevice " , "in background");

        try
        {
            while (true){
                BluetoothDevice device = intents[0].getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("--btdevice " , "Near Device: " + device.getName() + " (" + device.getAddress() + ")");
                int  rssi = intents[0].getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) Integer.MIN_VALUE);
                Log.d("--btdevice " , "Signal: " + rssi + " dBm");

                Thread.sleep(1500);
            }

            //Toast.makeText(mActivity, "Near Device: " + device.getName(), Toast.LENGTH_LONG).show();
            //((TextView)(mActivity.findViewById(R.id.near_device_name))).setText("Near Device: " + device.getName() + " (" + device.getAddress() + ")");
            //((TextView)(mActivity.findViewById(R.id.near_device_signal))).setText("Signal: " + rssi + " dBm");

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


}