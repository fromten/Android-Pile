package learn.example.pile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class PhotosWatcherFragment extends Fragment implements OkHttpRequest.RequestCallback<String>,ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private RelativeLayout mRelativeLayout;
    private TextView title;
    private TextView number;
    private TextView content;

    private List<PhotosMessage> mMessagesList;

    private Call mCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_photowatcher,container,false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mRelativeLayout= (RelativeLayout) view.findViewById(R.id.container);
        title= (TextView) view.findViewById(R.id.title);
        number= (TextView) view.findViewById(R.id.number);
        content= (TextView) view.findViewById(R.id.content);
        return view;
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
            mMessagesList=new ArrayList<>();
            String title = GsonHelper.getAsString(object.get("setname"), null);
            final int size = array.size();
            for (int i = 0; i < size; ++i) {
                JsonObject o = (JsonObject) array.get(i);
                String imgUrl = GsonHelper.getAsString(o.get("imgurl"), null);
                String content = GsonHelper.getAsString(o.get("note"), null);
                list.add(createPhotoView());
                mMessagesList.add(new PhotosMessage(title,content,imgUrl));
            }
            mViewPagerAdapter = new ViewPagerAdapter(list);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(this);

            //手动调用此方法,初始化
            onPageSelected(0);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        PhotosMessage message=mMessagesList.get(position);
        ImageView view= (ImageView) mViewPagerAdapter.getView(position);
        if (view.getDrawable()==null)
        {
            Glide.with(this).load(message.mUrl).fitCenter().into(view);
        }
        title.setText(message.getTitle());
        content.setText(message.getContent());
        number.setText(position+1+"/"+mMessagesList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private View createPhotoView(){
        PhotoView photoView = new PhotoView(getContext());
        photoView.setMinimumScale(0.5f);
        photoView.setOnViewTapListener(mOnViewTapListener);
        photoView.setScaleType(ImageView.ScaleType.CENTER);
        photoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return photoView;
    }
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener=new PhotoViewAttacher.OnViewTapListener() {
        @Override
        public void onViewTap(View view, float v, float v1) {
            Activity activity=getActivity();
            if (mRelativeLayout.getVisibility() != View.VISIBLE) {
                mRelativeLayout.setVisibility(View.VISIBLE);
                if (activity instanceof AppCompatActivity)
                {
                    ((AppCompatActivity) activity).getSupportActionBar().show();
                }
            } else {
                mRelativeLayout.setVisibility(View.INVISIBLE);
                if (activity instanceof AppCompatActivity)
                {
                    ((AppCompatActivity) activity).getSupportActionBar().hide();
                }
            }
        }
    };



    private static class PhotosMessage{
        private String title;
        private String content;
        private String mUrl;

        public PhotosMessage(String title, String content,String url) {
            this.title = title;
            this.content = content;
            this.mUrl=url;
        }

        public String getTitle() {
            return title;
        }
        public String getContent() {
            return content;
        }

    }

}
