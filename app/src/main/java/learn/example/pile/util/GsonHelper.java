package learn.example.pile.util;

import com.google.gson.JsonElement;

/**
 * Created on 2016/7/21.
 */
public class GsonHelper {

    public static String getAsString(JsonElement element,String defString)
    {
        try {
            return element.getAsString();
        }catch (ClassCastException e)
        {
            e.printStackTrace();
        }catch (IllegalStateException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return defString;
    }

    public static int getAsInteger(JsonElement element,int defInteger)
    {
        try {
            return element.getAsInt();
        }catch (ClassCastException e)
        {
            e.printStackTrace();
        }catch (IllegalStateException e)
        {
            e.printStackTrace();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return defInteger;
    }
 }
