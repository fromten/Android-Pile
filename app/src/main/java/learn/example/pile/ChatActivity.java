package learn.example.pile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import learn.example.joke.R;
import learn.example.pile.adapters.ChatListAdapter;
import learn.example.pile.jsonobject.TuringMachineJson;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.net.VolleyRequestQueue;
import learn.example.pile.object.ChatInfo;

/**
 * Created on 2016/6/26.
 */
public class ChatActivity extends BaseActivity implements Response.ErrorListener,
        Response.Listener<TuringMachineJson>, View.OnClickListener{

    private static final String TAG = "ChatActivity";
    public static final String KEY="879a6cb3afb84dbf4fc84a1df2ab7319";
    public static final String USERID="eb2edb736";
    public static final String URL="http://apis.baidu.com/turing/turing/turing";


    private ListView mListView;
    private EditText mMsgEdit;
    private Button mSendButton;
    private ChatListAdapter mChatListAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mListView= (ListView) findViewById(android.R.id.list);
        mMsgEdit= (EditText) findViewById(R.id.chat_msgedit);
        mSendButton= (Button) findViewById(R.id.chat_msgsend);
        mSendButton.setOnClickListener(this);
        mChatListAdapter=new ChatListAdapter();
        mListView.setAdapter(mChatListAdapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(TuringMachineJson response) {
        if (response!=null)
        {
            ChatInfo info=new ChatInfo(ChatInfo.TYPE_LEFT,response.getText());
            mChatListAdapter.addItem(info);
            mListView.smoothScrollToPosition(mChatListAdapter.getCount()-1);
        }

    }

    @Override
    protected void onDestroy() {
        VolleyRequestQueue.getInstance(this).cancelAll(TAG);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        String editText=mMsgEdit.getText().toString();
        if (!editText.isEmpty())
        {
            String url=getUrl(editText);
            if (url!=null)
            {
                //请求网络数据
                GsonRequest<TuringMachineJson> request=new GsonRequest<TuringMachineJson>(url,TuringMachineJson.class,this,this,true);
                request.setTag(TAG);
                VolleyRequestQueue.getInstance(this).addToRequestQueue(request);
            }
            //将要发送的消息添加的List
            ChatInfo info=new ChatInfo(ChatInfo.TYPE_Right,editText);
            mChatListAdapter.addItem(info);
            mMsgEdit.setText(null);
        }
        mListView.smoothScrollToPosition(mChatListAdapter.getCount()-1);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMsgEdit.getWindowToken(), 0);
    }


    /**
     * 生成完整的请求Url
     * @param info 所需要的消息
     * @return 完整的Url
     */
    public  String getUrl(String info)
    {

        try {
            String encodeInfo= URLEncoder.encode(info,"UTF-8");
            String url=URL+"?"+"key="+KEY+"&"+"info="+encodeInfo+"&"+"userid="+USERID;
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
