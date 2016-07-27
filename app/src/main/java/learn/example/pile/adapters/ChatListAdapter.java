package learn.example.pile.adapters;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.R;
import learn.example.pile.object.ChatInfo;

/**
 * Created on 2016/6/26.
 */
public class ChatListAdapter extends BaseAdapter {
    private List<ChatInfo> mList;

    public ChatListAdapter() {
        mList=new ArrayList<>();
    }

    public void addItem(ChatInfo info)
    {
        if (info==null)return;

        mList.add(info);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null)
        {
            holder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_text,parent,false);
            holder.mChatText= (TextView) convertView.findViewById(R.id.chat_text);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        ChatInfo msg=mList.get(position);

        int gravity=msg.getType()==ChatInfo.GRAVITY_LEFT?Gravity.START:Gravity.END;
        FrameLayout.LayoutParams layoutParams= (FrameLayout.LayoutParams) holder.mChatText.getLayoutParams();
        layoutParams.gravity=gravity;
        holder.mChatText.setLayoutParams(layoutParams);
        holder.mChatText.setText(msg.getMsg());

        return convertView;
    }

    private static class ViewHolder
    {
        public TextView mChatText;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
