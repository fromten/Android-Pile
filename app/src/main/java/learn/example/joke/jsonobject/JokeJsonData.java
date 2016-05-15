package learn.example.joke.jsonobject;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/5/5.
 */
public class JokeJsonData {

    public int showapi_res_code=-1;
    public String showapi_res_error;
    public ShowResBody showapi_res_body;

    public static class ShowResBody
    {
        public int allNum;
        public int allPages;
        public List<ContentList> contentlist;
        public int currentPage;
        public int maxResult;

        public static class ContentList{
             public String ct;
             public String text;
             public String title;
             public String img;
             public int type;
        }
    }


}
