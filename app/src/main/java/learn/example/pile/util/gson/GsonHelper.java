package learn.example.pile.util.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Created on 2016/7/21.
 */
public class GsonHelper {

    public static String getAsString(JsonElement element,String defString)
    {
        try {
            return element==null||element.isJsonNull()?defString:element.getAsString();
        }catch (ClassCastException | IllegalStateException e)
        {
            e.printStackTrace();
        }
        return defString;
    }

    public static int getAsInteger(JsonElement element,int defInteger)
    {
        try {
            return element==null?defInteger:element.getAsInt();
        }catch (ClassCastException | IllegalStateException e)
        {
            e.printStackTrace();
        }
        return defInteger;
    }

    public static String serialize(Object o)
    {
        return new Gson().toJson(o);
    }

    public static<T> T deserialize(String string,Class<T> clazz)
    {
        return new Gson().fromJson(string,clazz);
    }

}
