package learn.example.pile.activity.normal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import learn.example.pile.R;
import learn.example.pile.activity.base.ToolBarActivity;
import learn.example.pile.adapters.ChatListAdapter;
import learn.example.pile.jsonbean.TuringMachineJson;
import learn.example.pile.net.IService;
import learn.example.pile.net.TuringMachineService;
import learn.example.pile.pojo.ChatInfo;

/**
 * Created on 2016/6/26.
 */
public class ChatActivity extends ToolBarActivity implements IService.Callback<TuringMachineJson>, View.OnClickListener{

    private static final String TAG = "ChatActivity";

    private ListView mListView;
    private EditText mMsgEdit;
    private Button mSendButton;
    private ChatListAdapter mChatListAdapter;
    private TuringMachineService mService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
    }

    private void init()
    {
        mListView= (ListView) findViewById(android.R.id.list);
        mMsgEdit= (EditText) findViewById(R.id.chat_msgedit);
        mSendButton= (Button) findViewById(R.id.chat_msgsend);
        mSendButton.setOnClickListener(this);
        mChatListAdapter=new ChatListAdapter();
        mListView.setAdapter(mChatListAdapter);
        mService=new TuringMachineService();

        mMsgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       smoothScrollToListBottom();
                    }
                },100);
            }
        });
    }


    @Override
    protected void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(TuringMachineJson data) {

        ChatInfo info=new ChatInfo(ChatInfo.GRAVITY_LEFT,data.getText());
        mChatListAdapter.addItem(info);

        smoothScrollToListBottom();
    }

    @Override
    public void onFailure(String msg) {
        Log.d(TAG, msg);
    }

    @Override
    public void onClick(View v) {

        String editText=mMsgEdit.getText().toString();
        if (!editText.isEmpty())
        {
            //将要发送的消息添加的List
            ChatInfo info=new ChatInfo(ChatInfo.GRAVITY_RIGHT,editText);
            mChatListAdapter.addItem(info);
            mMsgEdit.setText(null);
            mService.getMessage(editText,this);
        }
        collapseIME();

    }


    /**
     * 收缩关闭输入法
     */
    private void collapseIME(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMsgEdit.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void smoothScrollToListBottom()
    {
        mListView.smoothScrollToPosition(mChatListAdapter.getCount()-1);
    }



}
