package learn.example.pile.object;

/**
 * Created on 2016/6/26.
 */
public class ChatInfo {
    public static final int GRAVITY_LEFT=1;
    public static final int GRAVITY_RIGHT=2;
    private int gravity;
    private String msg;

    public ChatInfo(int gravity, String msg) {
        this.gravity = gravity;
        this.msg = msg;
    }

    public int getType() {
        return gravity;
    }

    public void setType(int gravity) {
        this.gravity = gravity;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
