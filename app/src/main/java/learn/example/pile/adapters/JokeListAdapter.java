package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import learn.example.joke.R;
import learn.example.pile.jsonobject.BaseJokeData;
import learn.example.pile.jsonobject.JokeJsonData;
import learn.example.pile.util.UrlCheck;

/**
 * Created on 2016/5/23.
 */
public class JokeListAdapter extends FooterAdapter {



    private static final int TEXT_TYPE=1;
    private static final int IMG_TYPE=2;
    private List<JokeJsonData.JokeResBody.JokeItem> mItems;
    private Context mContext;
    public JokeListAdapter(Context context){
        mItems=new ArrayList<>();
        mContext=context;
    }


    public void addAllItem(List<JokeJsonData.JokeResBody.JokeItem> items)
    {
        mItems.addAll(items);
        notifyItemInserted(mItems.size());
    }

    public void clearItem(){
        mItems.clear();
        notifyDataSetChanged();
    }


    public List<JokeJsonData.JokeResBody.JokeItem> getAllItem(){
        return mItems;
    }
    @Override
    public int getSelfItemSize() {
        return mItems.size();
    }
    @Override
    public int getSelfViewType(int position) {
        return mItems.get(position).getType()==1?TEXT_TYPE:IMG_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder createSelfViewHolder(ViewGroup parent, int type) {
        RecyclerView.ViewHolder holder = null;
        View view;
        if(type==TEXT_TYPE)
        {
            view=LayoutInflater.from(mContext).inflate(R.layout.fragment_joke_text_type,parent,false) ;
            holder=new JokeTextHolder(view);
        }else if (type==IMG_TYPE){
            view=LayoutInflater.from(mContext).inflate(R.layout.fragment_joke_img_type,parent,false);
            holder=new JokeImgHolder(view);
        }
        return holder;
    }

    @Override
    public void bindSelfViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof JokeTextHolder)
        {
            setTextJoke((JokeTextHolder) holder,position);
        }else if(holder instanceof  JokeImgHolder)
        {
            setImgJoke((JokeImgHolder) holder,position);
        }
    }

    private void setTextJoke(JokeTextHolder holder,int position){
        JokeJsonData.JokeResBody.JokeItem item=mItems.get(position);
        holder.content.setText(Html.fromHtml(item.getText()));
        holder.title.setText(item.getTitle()+"\t"+item.getCreateTime());
    }

    private void setImgJoke(JokeImgHolder holder,int position){
        JokeJsonData.JokeResBody.JokeItem item=mItems.get(position);
        String url=UrlCheck.fixUrl(item.getImg());
        if(url!=null)
        {
            String type=UrlCheck.isGifImg(url)?"gif":null;
            holder.imgtype.setText(type);
            Glide.with(mContext).load(url)
                    .asBitmap()
                    .dontAnimate()
                    .centerCrop()
                    .into(holder.img);
        }
        holder.title.setText(item.getTitle()+"\t"+item.getCreateTime());
    }

    public static class JokeTextHolder extends RecyclerView.ViewHolder
    {   public TextView title;
        public TextView content;
        public JokeTextHolder(View view){
            super(view);
            title= (TextView) view.findViewById(R.id.joke_title);
            content= (TextView) view.findViewById(R.id.joke_content);
        }
    }
    public static class JokeImgHolder extends RecyclerView.ViewHolder
    {   public TextView title;
        public TextView imgtype;
        public ImageView img;
        public JokeImgHolder(View view){
            super(view);
            title= (TextView) view.findViewById(R.id.joke_title);
            imgtype= (TextView) view.findViewById(R.id.joke_imgtype);
            img= (ImageView) view.findViewById(R.id.joke_img);
        }
    }
}
