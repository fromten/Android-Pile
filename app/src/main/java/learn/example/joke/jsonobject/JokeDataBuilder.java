package learn.example.joke.jsonobject;

/**
 * Created on 2016/5/14.
 */
public class JokeDataBuilder {
    private BaseJokeData mJokeData;
    public JokeDataBuilder()
    {
        mJokeData=new BaseJokeData();
    }
    public JokeDataBuilder setTitle(String title)
    {
        mJokeData.setTitle(title);
        return this;
    }
    public JokeDataBuilder setImgUrl(String url)
    {
        mJokeData.setImgUrl(url);
        return this;
    }
    public JokeDataBuilder setText(String text)
    {
        mJokeData.setText(text);
        return this;
    }
    public JokeDataBuilder setType(int type)
    {
        mJokeData.setType(type);
        return this;
    }
    public JokeDataBuilder setCurrentPage(int page)
    {
        mJokeData.setCurrentPage(page);
        return this;
    }
    public JokeDataBuilder setLastTime(String  time)
    {
        mJokeData.setLastTime(time);
        return this;
    }
    public BaseJokeData build()
    {
        return mJokeData;
    }

}
