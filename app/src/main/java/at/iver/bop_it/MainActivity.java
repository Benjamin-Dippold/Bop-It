package at.iver.bop_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;

    private List<WifiP2pDevice> deviceList;
    private ArrayAdapter<String> deviceListAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Wi-Fi Direct
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);

        // Initialize UI components
        listView = findViewById(R.id.listView);
        deviceList = new ArrayList<>();
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(deviceListAdapter);

        ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_WIFI_STATE,
                        android.Manifest.permission.CHANGE_WIFI_STATE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE}
                , PERMISSIONS_REQUEST_CODE);

        // Set up broadcast receiver for Wi-Fi Direct events
        receiver = new WiFiDirectBroadcastReceiver();
        intentFilter = new IntentFilter();
        // Indicates a change in the Wi-Fi Direct status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi Direct connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void discoverPeers(View v) {
        // Check Wi-Fi Direct availability
        if (wifiP2pManager == null) {
            Toast.makeText(MainActivity.this, "Wi-Fi Direct is not supported on this device.", Toast.LENGTH_SHORT).show();
            return;
        }

        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Discovery started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                String errorMessage;
                switch (reasonCode) {
                    case WifiP2pManager.ERROR:
                        errorMessage = "Discovery failed due to an internal error.";
                        break;
                    case WifiP2pManager.P2P_UNSUPPORTED:
                        errorMessage = "Wi-Fi Direct is not supported on this device.";
                        break;
                    case WifiP2pManager.BUSY:
                        errorMessage = "Discovery failed because the system is busy.";
                        break;
                    default:
                        errorMessage = "Discovery failed with error code: " + reasonCode;
                        break;
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null) {
                switch (action) {
                    case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                        wifiP2pManager.requestPeers(channel, peers -> {
                            deviceList.clear();
                            deviceList.addAll(peers.getDeviceList());
                            deviceListAdapter.clear();
                            for (WifiP2pDevice device : deviceList) {
                                deviceListAdapter.add(device.deviceName);
                            }
                            deviceListAdapter.notifyDataSetChanged();
                        });
                        break;
                    case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                        WifiP2pInfo info = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                        if (info != null && info.groupFormed) {
                            Toast.makeText(MainActivity.this, "Connected to a device.", Toast.LENGTH_SHORT).show();
                            // Perform actions when connected to a device
                        } else {
                            Toast.makeText(MainActivity.this, "Disconnected from a device.", Toast.LENGTH_SHORT).show();
                            // Perform actions when disconnected from a device
                        }
                        break;
                }
            }
        }
    }
}