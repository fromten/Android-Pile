package learn.example.pile.util.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created on 2016/12/16.
 */

public class SafeJsonObject{
    private JsonObject mObject;

    public SafeJsonObject(JsonObject object) {
        mObject = object;
    }

    public JsonObject getSource()
    {
        return mObject;
    }

    public String getString(String member)
    {
       JsonElement element=mObject.get(member);
       return isSafety(element)?element.getAsString():null;
    }

    public int getInteger(String member)
    {
        JsonElement element=mObject.get(member);
        return isSafety(element)?element.getAsInt():0;
    }

    public long getLong(String member)
    {
        JsonElement element=mObject.get(member);
        return isSafety(element)?element.getAsLong():0;
    }

    public float getFloat(String member)
    {
        JsonElement element=mObject.get(member);
        return isSafety(element)?element.getAsFloat():0;
    }

    public double getDouble(String member)
    {
        JsonElement element=mObject.get(member);
        return isSafety(element)?element.getAsDouble():0;
    }

    public boolean isSafety(JsonElement element)
    {
        return element!=null&&!element.isJsonNull();
    }

}
