package learn.example.pile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.R;
import learn.example.pile.jsonobject.BaseJokeData;

/**
 * Created on 2016/5/23.
 */
public class JokeListAdapter extends FooterAdapter<JokeListAdapter.JokeViewHolder> {

    private List<BaseJokeData> mItems;
    private Context mContext;
    public JokeListAdapter(Context context){
        mItems=new ArrayList<>();
        mContext=context;
    }
    public void addItem(BaseJokeData baseJokeData)
    {
        mItems.add(baseJokeData);
        notifyItemInserted(mItems.size());
    }
    public void addAllItem(List<BaseJokeData> baseJokeDatas)
    {
        mItems.addAll(baseJokeDatas);
        notifyItemInserted(mItems.size());
    }
    public void clearItem(){
        mItems.clear();
        notifyDataSetChanged();
    }


    public List<BaseJokeData> getAllItem(){
        return mItems;
    }
    @Override
    public int getItemSize() {
        return mItems.size();
    }

    @Override
    public JokeViewHolder onCreateItemHolder(ViewGroup parent) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.fragment_joke_adpter_view,parent,false);
        return new JokeViewHolder(view);
    }

    @Override
    public void onBindItemHolder(final JokeViewHolder holder, int position) {
         BaseJokeData jokeData=mItems.get(position);
         if(jokeData.getType()==BaseJokeData.TYPE_TEXT)
         {
             holder.img.setVisibility(View.GONE);
             holder.content.setVisibility(View.VISIBLE);
             holder.content.setText(jokeData.getText());
         }else {
             holder.img.setVisibility(View.VISIBLE);
             holder.content.setVisibility(View.GONE);
             Glide.with(mContext).load(jokeData.getImgUrl()).into(holder.img);
         }
        holder.title.setText(jokeData.getTitle());

    }
    public static class JokeViewHolder extends RecyclerView.ViewHolder
    {   public TextView title;
        public TextView content;
        public ImageView img;
        public JokeViewHolder(View view){
            super(view);
            title= (TextView) view.findViewById(R.id.joke_title);
            content= (TextView) view.findViewById(R.id.joke_content);
            img= (ImageView) view.findViewById(R.id.joke_img);
        }
    }
}
