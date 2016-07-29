package learn.example.pile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import learn.example.net.OkHttpRequest;
import learn.example.pile.R;
import learn.example.pile.adapters.ViewPagerAdapter;
import learn.example.pile.object.NetEase;
import learn.example.pile.util.GsonHelper;
import okhttp3.Call;
import okhttp3.Request;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created on 2016/7/29.
 */
public class PhotosWatcherFragment extends Fragment implements OkHttpRequest.RequestCallback<String> {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private Call mCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewPager = new ViewPager(getContext());
        return mViewPager;
    }

    public void setContent(String skipID) {
        try {
            int line = skipID.indexOf("|");
            String first = skipID.substring(4, line);
            String second = skipID.substring(line + 1, skipID.length());
            Request request = new Request.Builder().url(String.format(NetEase.PHOTOS_SET_URL, first, second)).build();
            mCall = OkHttpRequest.getInstance(getContext()).newStringRequest(request, this);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSuccess(String res) {
        JsonObject object = new JsonParser().parse(res).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("photos");
        if (array != null) {
            List<View> list = new ArrayList<>();
            String title = GsonHelper.getAsString(object.get("setname"), null);
            final int size = array.size();
            for (int i = 0; i < size; ++i) {
                JsonObject o = (JsonObject) array.get(i);
                String imgUrl = GsonHelper.getAsString(o.get("imgurl"), null);
                String content = GsonHelper.getAsString(o.get("note"), null);
                list.add(createView(imgUrl, title, content));
            }
            mViewPagerAdapter = new ViewPagerAdapter(list);
            mViewPagerAdapter.setOnViewShowListener(new ViewPagerAdapter.onViewShowListener() {
                @Override
                public void onBindView(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.number);
                    textView.setText(position+1+"/"+size);
                }
            });
            mViewPager.setAdapter(mViewPagerAdapter);
        }
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onDestroy() {
        if (mCall!=null)
        mCall.cancel();
        super.onDestroy();
    }

    public View createView(String url, String title, String content) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.photo_watcher, mViewPager, false);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.imageView);
        photoView.setMinimumScale(0.5f);
        photoView.setOnViewTapListener(mOnViewTapListener);
        Glide.with(this).load(url)
                .dontAnimate()
                .error(R.mipmap.img_error)
                .fitCenter()
                .into(photoView);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);
        TextView contentView = (TextView) view.findViewById(R.id.content);
        contentView.setText(content);
        return view;
    }
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener=new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            getActivity().finish();
        }
    };

}
