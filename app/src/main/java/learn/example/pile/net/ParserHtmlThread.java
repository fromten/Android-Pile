package learn.example.pile.net;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;


import learn.example.pile.jsonobject.VideoJsonData;
import learn.example.pile.util.VideoParser;

/**
 * Created on 2016/6/1.
 */
public class ParserHtmlThread implements Runnable{
        private VideoJsonData.VideoItem mItem;
        private boolean fromWeibo;
        private boolean fromMiaopai;
        private String srcUrl;
        public ParserHtmlThread(@NonNull VideoJsonData.VideoItem item) {
            mItem=item;
            srcUrl=item.getSrcUrl();
            fromWeibo=srcUrl.contains("weibo.com");
            fromMiaopai=srcUrl.contains("miaopai.com");
        }

        @Override
        public void run() {
            if(!fromMiaopai&&!fromWeibo)
            {
                return;
            }
            String fileUrl=null;
            String imgUrl=null;
            if (fromWeibo)
            {
                String res=StringRequest.request(srcUrl);
                if(res!=null)
                {    fileUrl= VideoParser.getWeiboVideoFileUrl(res);
                     imgUrl=VideoParser.getWeiboVideoImgUrl(res);
                }
            }else {
                fileUrl=VideoParser.getMPVidFileFromUrl(srcUrl);
                imgUrl=VideoParser.getMPVidImgFromUrl(srcUrl);
            }
            mItem.setFileUrl(fileUrl);
            mItem.setImgUrl(imgUrl);
            EventBus.getDefault().post(mItem);
        }

    public interface ParserListener{
        void parserComplete(String... value);
    }
}
