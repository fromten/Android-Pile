package learn.example.pile.adapters;

import android.widget.TextView;

import java.util.List;

import learn.example.pile.R;
import learn.example.pile.pojo.PhotosMessage;
import learn.example.pile.ui.PhotoWatcherLayout;

/**
 * Created on 2016/8/15.
 */
public class NetEasePhotoAdapter implements PhotoWatcherLayout.PhotoWatcherAdapter
{
    private List<PhotosMessage> mMessagesList;

    public NetEasePhotoAdapter(List<PhotosMessage> messagesList) {
        mMessagesList = messagesList;
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    @Override
    public String getUrl(int position) {
        return mMessagesList.get(position).getUrl();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position, PhotoWatcherLayout viewGroup) {

        PhotosMessage message=mMessagesList.get(position);

        TextView title= (TextView) viewGroup.findViewById(R.id.title);
        TextView content= (TextView) viewGroup.findViewById(R.id.content);
        TextView page= (TextView) viewGroup.findViewById(R.id.page);
        title.setText(message.getTitle());
        content.setText(message.getContent());
        page.setText(position+1+"/"+mMessagesList.size());
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
