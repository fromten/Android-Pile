package learn.example.pile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import learn.example.joke.R;
import learn.example.pile.adapters.ChatListAdapter;
import learn.example.pile.jsonobject.TuringMachineJson;
import learn.example.pile.net.TuringMachineService;
import learn.example.pile.object.ChatInfo;

/**
 * Created on 2016/6/26.
 */
public class ChatActivity extends BaseActivity implements TuringMachineService.ServiceListener<TuringMachineJson>, View.OnClickListener{

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
        mListView= (ListView) findViewById(android.R.id.list);
        mMsgEdit= (EditText) findViewById(R.id.chat_msgedit);
        mSendButton= (Button) findViewById(R.id.chat_msgsend);
        mSendButton.setOnClickListener(this);
        mChatListAdapter=new ChatListAdapter();
        mListView.setAdapter(mChatListAdapter);

        mService=new TuringMachineService(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mService.setListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mService.removeListener(this);
    }

    @Override
    public void onSuccess(TuringMachineJson data) {
        ChatInfo info=new ChatInfo(ChatInfo.TYPE_LEFT,data.getText());
        mChatListAdapter.addItem(info);
        mListView.smoothScrollToPosition(mChatListAdapter.getCount()-1);
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
            ChatInfo info=new ChatInfo(ChatInfo.TYPE_Right,editText);
            mChatListAdapter.addItem(info);
            mMsgEdit.setText(null);

            mService.getMessage(editText);
        }
        mListView.smoothScrollToPosition(mChatListAdapter.getCount()-1);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMsgEdit.getWindowToken(), 0);
    }



}