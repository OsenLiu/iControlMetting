package barco.com.icontrolmetting;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String IP_KEY = "com.barco.server.ip";

    private Button connectBtn;
    private EditText editText;

    private SharedPreferences sp;

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
        sp = getPreferences(MODE_PRIVATE);

        connectBtn = (Button) findViewById(R.id.connect_button);
        connectBtn.setOnClickListener(this);
        editText = findViewById(R.id.ip_editText);
        String ip = sp.getString(IP_KEY, "");
        if(!ip.isEmpty()) {
            editText.setText(ip);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onServiceConnected() {
        String ip = sp.getString(IP_KEY, "");
        if(!ip.isEmpty()) {
            officeService.connectServer(ip);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_button:
            {
                String ip = editText.getText().toString();
                if(ip.isEmpty()) {
                    Toast.makeText(this, "IP can't be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                sp.edit().putString(IP_KEY, ip).commit();
                officeService.connectServer(ip);
            }
                break;
        }
    }

}
