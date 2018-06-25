package barco.com.icontrolmetting;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.WebSocket;

public class OfficeService extends Service {
    private static final String TAG = OfficeService.class.getSimpleName();
    private static final String SENDER = "F208F146F3C856B184AE48A69C018704F96218464C240009A0BEA7DB0A2C9610";
    public static final String OFFICE_SERVER_CONNECTED = "com.barco.OfficeService.connected";
    public static final String OFFICE_SERVER_DISCONNECTED = "com.barco.OfficeService.disconnected";
    public static final String OFFICE_SERVER_RESPONSE = "com.barco.OfficeService.response";

    public static final String OFFICE_ACTION_RESPONSE = "com.barco.OfficeService.action.response";

    public static final int TIMEOUT =5000;


    private IBinder OfficeBinder;
    private WebSocket server = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(server!=null) {
            server.close();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        OfficeBinder = new OfficeBinder();
        return OfficeBinder;
    }

    public class OfficeBinder extends Binder {
        public OfficeService getService(){
            return OfficeService.this;
        }
    }

    public void connectServer(String ip) {
        String url = String.format("http://%s:8089", ip);
        Log.d(TAG, "server is "+url);
        AsyncHttpGet request = new AsyncHttpGet(url);
        request.setTimeout(TIMEOUT);
        AsyncHttpClient.getDefaultInstance().websocket(request, null, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                Log.d(TAG, ">>>onCompleted");
                if (ex != null) {
                    Log.e(TAG, Log.getStackTraceString(ex));
                    Intent intent = new Intent();
                    intent.setAction(OFFICE_SERVER_DISCONNECTED);
                    sendBroadcast(intent);
                    return;
                }
                server = webSocket;
                server.setClosedCallback(new CompletedCallback() {
                    @Override
                    public void onCompleted(Exception ex) {
                        Log.i(TAG, "connection is closed");
                        //broadcast message to others
                        Intent intent = new Intent();
                        intent.setAction(OFFICE_SERVER_DISCONNECTED);
                        sendBroadcast(intent);
                    }
                });
                server.setStringCallback(new WebSocket.StringCallback() {
                    @Override
                    public void onStringAvailable(String s) {
                        Log.i(TAG, "new msg: "+s);
                        handleMessage(s);
                    }
                });
                Intent intent = new Intent();
                intent.setAction(OFFICE_SERVER_CONNECTED);
                sendBroadcast(intent);
            }


        });
    }

    public void disconnectServer() {
        server.close();
    }

    private void handleMessage(String msg) {
        Intent intent = new Intent();
        intent.setAction(OFFICE_SERVER_RESPONSE);
        intent.putExtra(OFFICE_ACTION_RESPONSE, msg);
        sendBroadcast(intent);

    }

    public boolean isConnectServer() {
        if(server == null) {
            return false;
        }
        if(server.isOpen()) {
            return true;
        }
        return false;
    }

    public void nextPage() {
        Action action = new Action();
        action.setIntent("page_down");
        action.setSender(SENDER);
        Gson gson = new Gson();
        server.send(gson.toJson(action));
    }

    public void previousPage() {
        Action action = new Action();
        action.setIntent("page_up");
        action.setSender(SENDER);
        Gson gson = new Gson();
        server.send(gson.toJson(action));
    }

    public void moveToFirst() {
        Action action = new Action();
        action.setIntent("first");
        action.setSender(SENDER);
        Gson gson = new Gson();
        server.send(gson.toJson(action));
    }

    public void moveToLast() {
        Action action = new Action();
        action.setIntent("last");
        action.setSender(SENDER);
        Gson gson = new Gson();
        server.send(gson.toJson(action));
    }

    public void getCurrentPage() {
        Action action = new Action();
        action.setIntent("get_page");
        action.setSender(SENDER);
        Gson gson = new Gson();
        server.send(gson.toJson(action));
    }

    public void playOffice() {
        Action action = new Action();
        action.setIntent("play");
        action.setSender(SENDER);
        Gson gson = new Gson();
        server.send(gson.toJson(action));
    }
}
