package barco.com.icontrolmetting;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class ControllerActivity extends BaseActivity implements
        GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
    private static final String TAG = ControllerActivity.class.getSimpleName();

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    private GestureDetectorCompat mDetector;
    private TextView pageTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(officeService!=null && !officeService.isConnectServer()) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        if(officeService!=null) {
            officeService.disconnectServer();
        }
        super.onDestroy();
    }

    @Override
    protected void onServiceConnected() {
        officeService.getCurrentPage();
    }

    private void initView() {

        mDetector = new GestureDetectorCompat(this, this);
        mDetector.setOnDoubleTapListener(this);
        pageTextview = findViewById(R.id.page_textView);
    }

    @Override
    protected void serverConnected() {

    }

    @Override
    protected void serverDisConnected() {
        finish();
    }

    @Override
    protected void msgReceived(String msg) {
        Gson gson = new Gson();
        try {
            Message gMsg = gson.fromJson(msg, Message.class);
            if(gMsg.getIntent().equals("event_page_changed")) {
                PageEvent pe = gson.fromJson(msg, PageEvent.class);
                pageTextview.setText(String.valueOf(pe.getPage()));
            }
            else {
                Response res = gson.fromJson(msg, Response.class);
                if (res.getCode() == 0) {
                    if (res.getPage() != null) {
                        pageTextview.setText(String.valueOf(res.getPage()));
                    }
                } else {
                    Toast.makeText(this, "No powerpoint is open", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(TAG, ">>>onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d(TAG, ">>>onDoubleTapEvent");

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, ">>>onFling");
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        Log.d(TAG, "swipe right");
                        officeService.playOffice();
                    } else {
                        Log.d(TAG, "swipe left");
                        officeService.marker();
                    }
                    result = true;
                }
            }
            else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    Log.d(TAG, "swipe down");
                    officeService.nextPage();
                } else {
                    Log.d(TAG, "swipe up");
                    officeService.previousPage();
                }
                result = true;
            }
        } catch (Exception exception) {
            Log.e(TAG, Log.getStackTraceString(exception));
        }
        return result;
    }
}
