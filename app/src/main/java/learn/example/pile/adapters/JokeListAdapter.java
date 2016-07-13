package learn.example.pile.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import learn.example.joke.R;
import learn.example.pile.jsonbean.JokeJsonData;
import learn.example.pile.util.ActivityLauncher;
import learn.example.pile.util.UrlCheck;

/**
 * Created on 2016/5/23.
 */
public class JokeListAdapter extends SaveStateAbleAdapter<RecyclerView.ViewHolder,JokeJsonData.JokeResBody.JokeItem> implements View.OnClickListener {


    private static final int TEXT_TYPE=1;
    private static final int IMG_TYPE=2;

    @Override
    public void updaterItemView(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof JokeTextHolder)
        {
            setTextJoke((JokeTextHolder) holder,position);
        }else if(holder instanceof  JokeImgHolder)
        {
            setImgJoke((JokeImgHolder) holder,position);
        }
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent, int type) {
        RecyclerView.ViewHolder holder = null;
        View view;
        if(type==TEXT_TYPE)
        {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_joke_text_type,parent,false) ;
            holder=new JokeTextHolder(view);
        }else if (type==IMG_TYPE){
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_joke_img_type,parent,false);
            holder=new JokeImgHolder(view);
        }
        return holder;
    }

    @Override
    public int getViewType(int position) {
        JokeJsonData.JokeResBody.JokeItem item=getItem(position);
        int type;
        switch (item.getType())
        {
            case 1:type=TEXT_TYPE;break;
            case 2:type=IMG_TYPE;break;
            default:type=super.getViewType(position);
        }
        return type;
    }



    private void setTextJoke(JokeTextHolder holder,int position){
        JokeJsonData.JokeResBody.JokeItem item=getItem(position);
        holder.content.setText(Html.fromHtml(item.getText()));
        String html="<p>"+item.getTitle()+"<small>"+item.getCreateTime().substring(0,10)+"</small></p>";
        holder.title.setText(Html.fromHtml(html));
    }

    private void setImgJoke(JokeImgHolder holder,int position){
        JokeJsonData.JokeResBody.JokeItem item=getItem(position);
        String url= UrlCheck.fixUrl(item.getImg());
        Context context=holder.itemView.getContext();
        if(url!=null)
        {
            String type=UrlCheck.isGifImg(url)?" gif ":null;
            holder.imgtype.setText(type);
            Glide.with(context).load(url)
                    .asBitmap()
                    .centerCrop()
                    .into(holder.img);
            holder.img.setTag(R.id.link,url);
            holder.img.setOnClickListener(this);
        }
        String time="<small>  "+item.getCreateTime().substring(0,10)+"</small>";
        String html="<p>"+item.getTitle()+time+"</p>";
        holder.title.setText(Html.fromHtml(html));
    }


    @Override
    public void onClick(View v) {
        String url= (String) v.getTag(R.id.link);
        Bundle anim=ActivityLauncher.openAnimation(v.getContext());
        ActivityLauncher.startPhotoActivity(v.getContext(),url,anim);
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
