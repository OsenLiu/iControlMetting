package barco.com.icontrolmetting;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("Intent")
    private String intent;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}
