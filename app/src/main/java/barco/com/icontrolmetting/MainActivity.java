package barco.com.icontrolmetting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectBtn = (Button) findViewById(R.id.connect_button);
        connectBtn.setOnClickListener(this);
    }

    private void initWebSocket() {
        AsyncHttpClient.getDefaultInstance().websocket("ws://192.168.1.88:8089", null, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    Log.e(TAG, Log.getStackTraceString(ex));
                    return;
                }
                Action action = new Action();
                action.setIntent("page_down");
                webSocket.send(new Gson().toJson(action));
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.d(TAG, "receiver msg from server: "+ s);
                    }
                });
            }


        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_button:
            {
                initWebSocket();
            }
                break;
        }
    }
}
