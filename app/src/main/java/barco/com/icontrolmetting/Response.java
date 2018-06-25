package barco.com.icontrolmetting;

import android.support.annotation.Nullable;

public class Response {
    private int code;
    private String message;
    @Nullable
    private Integer page;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public Integer getPage() {
        return page;
    }

    public void setPage(@Nullable Integer page) {
        this.page = page;
    }
}
