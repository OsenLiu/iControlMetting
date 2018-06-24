package barco.com.icontrolmetting;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button connectBtn;

    @Override
    protected void serverConnected() {
        Intent controlIntent = new Intent(MainActivity.this, ControllerActivity.class);
        startActivity(controlIntent);
    }

    @Override
    protected void serverDisConnected() {
        Toast.makeText(MainActivity.this, "Failed to connect to server", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectBtn = (Button) findViewById(R.id.connect_button);
        connectBtn.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_button:
            {
                officeService.connectServer("192.168.1.88");

            }
                break;
        }
    }

}
