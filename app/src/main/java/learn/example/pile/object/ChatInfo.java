package learn.example.pile.object;

/**
 * Created on 2016/6/26.
 */
public class ChatInfo {
    public static final int TYPE_LEFT=1;
    public static final int TYPE_Right=2;
    private int type;
    private String msg;

    public ChatInfo(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
