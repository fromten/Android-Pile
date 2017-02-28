package learn.example.pile.html;

import android.support.v4.util.SimpleArrayMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;

import learn.example.pile.util.gson.GsonHelper;
import learn.example.pile.util.HtmlBuilder;

/**
 * Created on 2016/7/27.
 */
public class NetEaseHtml implements Html{
    private String res;
    private String docID;

    public NetEaseHtml(String docID,String res) {
        this.res = res;
        this.docID=docID;
    }



    /**
     * 对应的 键 替换成 值
     * @param originBody 原来的字符串
     * @param map 替换对应映射
     * @return 新的字符串
     */
    public String replaceBody(String originBody,SimpleArrayMap<String,String> map) {
         StringBuilder builder=new StringBuilder(originBody);
         for (int i=0,l=map.size();i<l;++i)
         {
             String key=map.keyAt(i);
             String value=map.valueAt(i);

             int start=builder.indexOf(key);
             int end=start+key.length();
             if (start>-1)
             builder.replace(start,end,value);
         }
        return builder.toString();
    }

    public void mapImages(SimpleArrayMap<String,String> out,JsonArray imageArray)
    {
        if (imageArray==null)return;

        for (JsonElement element:imageArray)
        {
            JsonObject object= (JsonObject) element;
            String alt=GsonHelper.getAsString(object.get("alt"),null);
            String ref=GsonHelper.getAsString(object.get("ref"),null);
            String src=GsonHelper.getAsString(object.get("src"),null);

            String tagAttr=HtmlBuilder.attr("src",src);
            String imageTag= HtmlBuilder.tag("image",tagAttr,null);
            imageTag+= HtmlBuilder.tag("small",null,"▲图片"+alt);
            out.put(ref,imageTag);
        }
    }

    public void mapVideos(SimpleArrayMap<String,String> out,JsonArray videoArray)
    {
        if (videoArray==null)return;

        for (JsonElement element:videoArray)
        {
            JsonObject object= (JsonObject) element;
            String alt=GsonHelper.getAsString(object.get("alt"),null);
            String ref=GsonHelper.getAsString(object.get("ref"),null);
            String cover=GsonHelper.getAsString(object.get("cover"),null);
            String mp4=GsonHelper.getAsString(object.get("mp4_url"),null);

            String image1Attr= HtmlBuilder.attrs("class","videoic","src","image_playbutton.png");
            String image1Tag= HtmlBuilder.tag("image",image1Attr,null);

            String image2Attr= HtmlBuilder.attrs("class","cover","src",cover);
            String image2Tag= HtmlBuilder.tag("image",image2Attr,null);

            String divAttr= HtmlBuilder.attrs("class","videowrap","mp4",mp4);
            String div= HtmlBuilder.tag("div",divAttr,image1Tag+image2Tag);

            div+= HtmlBuilder.tag("small",null,"▲视频"+alt);
            out.put(ref,div);
        }
    }

    @Override
    public String getHtml() {
        HtmlBuilder htmlBuilder=new HtmlBuilder();
        htmlBuilder.startHtml("lang='zh-cn'")
                   .startHead(null)
                   .endHead();
        JsonReader reader=new JsonReader(new StringReader(res));
        reader.setLenient(true);
        JsonObject object=new JsonParser().parse(reader).getAsJsonObject();

        object=object.getAsJsonObject(docID);

        htmlBuilder.startBody(null)
                    .append("<div class='main'>");

        String title= GsonHelper.getAsString(object.get("title"),null);
        htmlBuilder.appendTag("h2",null,title);

        String body= GsonHelper.getAsString(object.get("body"),null);
        JsonArray imageArray=object.getAsJsonArray("img");
        JsonArray videoArray=object.getAsJsonArray("video");
        SimpleArrayMap<String,String> map=new SimpleArrayMap<>();
        mapImages(map,imageArray);
        mapVideos(map,videoArray);
        body= replaceBody(body,map);

        htmlBuilder.append(body)
                    .append("</div>")
                    .endBody().endHtml();

        return htmlBuilder.toString().replaceAll("(?<=<p>)\\s*","");//消除<P>标签内的空格
    }
}
