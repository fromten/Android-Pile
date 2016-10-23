package learn.example.pile.html;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import learn.example.pile.util.GsonHelper;
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

    @Override
    public String toString()
    {
//        StringBuilder builder=new StringBuilder();
//        JsonObject object=new JsonParser().parse(res).getAsJsonObject();
//        object=object.getAsJsonObject(docID);
//
//        builder.append("<html lang=\"zh-cn\">\n");
//        builder.append(insertHead());
//
//        builder.append("<body>");
//        builder.append("<div class='main'>");
//        String title= GsonHelper.getAsString(object.get("title"),null);
//        builder.append(insertTitle(title));
//        String body= GsonHelper.getAsString(object.get("body"),null);
//        JsonArray array=object.getAsJsonArray("img");
//        body= replaceImages(body,array);
//        body= replaceVideo(body,object.getAsJsonArray("video"));
//        builder.append(body);
//        builder.append("</div>");
//        builder.append("</body>");
//        builder.append("</html>");

        return  null;//最后把所有的p标签内空格去掉
    }


    private String replaceImages(String oldBody, JsonArray array)
    {
        if (array==null)
        {
            return oldBody;
        }
        String newBody;
        for (JsonElement element:array)
        {
            JsonObject object= (JsonObject) element;
            String alt=GsonHelper.getAsString(object.get("alt"),null);
            String ref=GsonHelper.getAsString(object.get("ref"),null);
            String src=GsonHelper.getAsString(object.get("src"),null);

            String tagAttr=HtmlBuilder.attr("src",src);
            String imageTag= HtmlBuilder.tag("image",tagAttr,null);
            imageTag+= HtmlBuilder.tag("b",null,"(图片)"+alt);
            oldBody=oldBody.replace(ref,imageTag);
        }
        newBody=oldBody;
        return newBody;
    }

    private String replaceVideos(String oldBody, JsonArray array)
    {
        if (array==null)
        {
            return oldBody;
        }
        String newBody;
        for (JsonElement element:array)
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

            div+= HtmlBuilder.tag("b",null,"(视频)"+alt);
            oldBody=oldBody.replace(ref,div);
        }
        newBody=oldBody;
        return newBody;
    }

    @Override
    public String getHtml() {
        HtmlBuilder htmlBuilder=new HtmlBuilder();
        htmlBuilder.startHtml("lang='zh-cn'");

        JsonObject object=new JsonParser().parse(res).getAsJsonObject();
        object=object.getAsJsonObject(docID);

        htmlBuilder.startBody(null)
                    .append("<div class='main'>");

        String title= GsonHelper.getAsString(object.get("title"),null);
        htmlBuilder.appendTag("h2",null,title);

        String body= GsonHelper.getAsString(object.get("body"),null);
        JsonArray array=object.getAsJsonArray("img");
        body= replaceImages(body,array);
        body= replaceVideos(body,object.getAsJsonArray("video"));

        htmlBuilder.append(body)
                    .append("</div>")
                    .endBody().endHtml();;

        return htmlBuilder.toString().replaceAll("(?<=<p>)\\s*","");
    }
}
