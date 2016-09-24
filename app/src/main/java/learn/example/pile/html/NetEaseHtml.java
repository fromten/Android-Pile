package learn.example.pile.html;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import learn.example.pile.util.GsonHelper;
import learn.example.pile.util.HtmlTagBuild;

/**
 * Created on 2016/7/27.
 */
public class NetEaseHtml {
    private String res;
    private String docID;

    public NetEaseHtml(String docID,String res) {
        this.res = res;
        this.docID=docID;
    }

    @Override
    public String toString()
    {
        StringBuilder builder=new StringBuilder();
        JsonObject object=new JsonParser().parse(res).getAsJsonObject();
        object=object.getAsJsonObject(docID);

        builder.append("<html lang=\"zh-cn\">\n");
        builder.append(insertHead());

        builder.append("<body>");
        builder.append("<div class='main'>");
        String title= GsonHelper.getAsString(object.get("title"),null);
        builder.append(insertTitle(title));
        String body= GsonHelper.getAsString(object.get("body"),null);
        JsonArray array=object.getAsJsonArray("img");
        body=insertImages(body,array);
        body=insertVideo(body,object.getAsJsonArray("video"));
        builder.append(body);
        builder.append("</div>");
        builder.append("</body>");
        builder.append("</html>");

        return  builder.toString().replaceAll("(?<=<p>)\\s*","");//最后把所有的p标签内空格去掉
    }
    private String insertHead()
    {
        return HtmlTagBuild.headTag("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
    }

    private String insertTitle(String title)
    {
        return "<h2>"+title+"</h2>";
    }


    private String insertImages(String oldBody,JsonArray array)
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

            String imageTag=HtmlTagBuild.imageTag(0,0,src);
            imageTag+=HtmlTagBuild.tag("b",null,"(图片)"+alt);
            oldBody=oldBody.replace(ref,imageTag);
        }
        newBody=oldBody;
        return newBody;
    }

    private String insertVideo(String oldBody,JsonArray array)
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

            String image1Attr=HtmlTagBuild.attrs("class","videoic","src","image_playbutton.png");
            String image1Tag=HtmlTagBuild.tag("image",image1Attr,null);

            String image2Attr=HtmlTagBuild.attrs("class","cover","src",cover);
            String image2Tag=HtmlTagBuild.tag("image",image2Attr,null);

            String divAttr=HtmlTagBuild.attrs("class","videowrap","mp4",mp4);
            String div=HtmlTagBuild.tag("div",divAttr,image1Tag+image2Tag);

            div+=HtmlTagBuild.tag("b",null,"(视频)"+alt);
            oldBody=oldBody.replace(ref,div);
        }
        newBody=oldBody;
        return newBody;
    }
}
