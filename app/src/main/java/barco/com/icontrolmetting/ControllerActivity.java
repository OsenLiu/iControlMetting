package barco.com.icontrolmetting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ControllerActivity extends BaseActivity implements View.OnClickListener {

    public Button nextBtn;
    public Button previousBtn;
    public Button firstBtn;
    public Button lastBtn;


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

    private void initView() {
        nextBtn =  findViewById(R.id.next_button);
        previousBtn =  findViewById(R.id.previous_button);
        firstBtn =  findViewById(R.id.first_button);
        lastBtn =  findViewById(R.id.last_button);
        nextBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        firstBtn.setOnClickListener(this);
        lastBtn.setOnClickListener(this);

    }

    @Override
    protected void serverConnected() {

    }

    @Override
    protected void serverDisConnected() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                officeService.nextPage();
                break;
            case R.id.previous_button:
                officeService.previousPage();
                break;
            case R.id.first_button:
                officeService.moveToFirst();
                break;
            case R.id.last_button:
                officeService.moveToLast();
                break;
        }
    }
}
