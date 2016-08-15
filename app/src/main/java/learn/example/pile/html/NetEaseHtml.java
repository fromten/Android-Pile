package learn.example.pile.html;

import android.util.Log;

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

    public String getHtml()
    {
        StringBuilder builder=new StringBuilder();
        JsonObject object=new JsonParser().parse(res).getAsJsonObject();
        object=object.getAsJsonObject(docID);
        builder.append(insertHead());
        builder.append("<div>");
        String title= GsonHelper.getAsString(object.get("title"),null);
        builder.append(insertTitle(title));
        String body= GsonHelper.getAsString(object.get("body"),null);
        JsonArray array=object.getAsJsonArray("img");
        body=insertImages(body,array);
        builder.append(body);
        builder.append("</div>");
        return builder.toString();
    }
    private String insertHead()
    {
        String css="img{\n" +
                "  border: 0;\n" +
                "  vertical-align: middle;\n" +
                "  color: transparent;\n" +
                "  font-size: 0;\n" +
                "  max-width: 100%;\n" +
                "  display: block;\n" +
                "  marginTop: 20px auto;\n"+
                "}"+
                " p{" +
                " text-align: justify;\n"+
                " color: #333333;}\n"
                +"div div.p{\n" +
                " text-align:justify;\n" +
                " text-justify: inter-word;"+
                " overflow: hidden;"+
                " display: inline-block;"+
                " margin-left : 10px;\n" +
                " margin-right: 10px;\n" +
                " align : justify;\n" +
                " }";

        return HtmlTagBuild.headTag(HtmlTagBuild.styleTag(css));
    }

    private String insertTitle(String title)
    {
        return "<h2 color='#202020' >"+title+"</h2>";
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
            String pixel=GsonHelper.getAsString(object.get("pixel"),null);
            String ref=GsonHelper.getAsString(object.get("ref"),null);
            String src=GsonHelper.getAsString(object.get("src"),null);

            String imageTag=HtmlTagBuild.imageTag(0,0,src);
            imageTag+=HtmlTagBuild.tag("strong",null,alt);
            oldBody=oldBody.replace(ref,imageTag);

        }
        newBody=oldBody;
        return newBody;
    }
}
