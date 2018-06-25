package barco.com.icontrolmetting;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    protected OfficeService officeService;
    private BaseActivity.MsgReceiver receiver;

    protected abstract void serverConnected();
    protected abstract void serverDisConnected();
    protected void msgReceived(String msg) {};
    protected void onServiceConnected() {}


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Office service connected");
            officeService = ((OfficeService.OfficeBinder)service).getService();
            BaseActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(OfficeService.OFFICE_SERVER_CONNECTED)) {
                serverConnected();
            }
            else if(intent.getAction().equals(OfficeService.OFFICE_SERVER_DISCONNECTED)) {
                serverDisConnected();
            }
            else if(intent.getAction().equals(OfficeService.OFFICE_SERVER_RESPONSE)) {
                String msg = intent.getStringExtra(OfficeService.OFFICE_ACTION_RESPONSE);
                msgReceived(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent bindIntent = new Intent(BaseActivity.this, OfficeService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

        receiver = new MsgReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(OfficeService.OFFICE_SERVER_CONNECTED);
        intentFilter.addAction(OfficeService.OFFICE_SERVER_DISCONNECTED);
        intentFilter.addAction(OfficeService.OFFICE_SERVER_RESPONSE);

        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
