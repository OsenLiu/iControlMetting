package barco.com.icontrolmetting;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Action {
    @SerializedName("Intent")
    private String intent;
    @Nullable
    private Integer page;
    private boolean isLaser = false;

    private String sender;

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    @Nullable
    public Integer getPage() {
        return page;
    }

    public void setPage(@Nullable Integer page) {
        this.page = page;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isLaser() {
        return isLaser;
    }

    public void setLaser(boolean laser) {
        isLaser = laser;
    }
}
