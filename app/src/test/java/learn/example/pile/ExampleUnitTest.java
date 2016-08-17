package learn.example.pile;


import com.google.gson.JsonArray;

import org.junit.Test;

import java.util.List;

import learn.example.pile.factory.CommentFactory;
import learn.example.pile.factory.OpenEyeCommentFactory;
import learn.example.pile.object.Comment;
import learn.example.pile.object.NetEase;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    public static final String json="{ 'urls':[{\"url\": " +
            "\"http://p3.pstatp.com/w356/78f002033060a15d691\"}, " +
            "{\"url\": \"http://pb2.pstatp.com/w356/78f002033060a15d691\"}, " +
            "{\"url\": \"http://pb3.pstatp.com/w356/78f002033060a15d691\"}]}";
    private JsonArray urls;

    @Test
    public void addition_isCorrect() {
        CommentFactory.newInstance().produceComment(OpenEyeCommentFactory.class,"{\"replyList\":[{\"id\":765710186055766016,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":20,\"message\":\"作为一个从来不看恐怖片的人，你给我推送这个☺\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471394509000,\"user\":{\"uid\":212604164,\"nickname\":\"阿斯巴甜\",\"avatar\":\"http://img.wdjimg.com/image/account/97a093c1325d05c4371f911ac676eb25_240_240.png\"},\"likeCount\":23,\"liked\":false,\"hot\":true},{\"id\":765680241367613440,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":13,\"message\":\"电影应该很震撼\\n但这支编年史截的…可以说…不用心阿\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471387369000,\"user\":{\"uid\":219416860,\"nickname\":\"午休的北极熊\",\"avatar\":\"http://wx.qlogo.cn/mmopen/L1QEH9wemVKJdANvmibpibt8FlSwzBAjSicOhv02Q6w1sicAIbJtpnfaDXdO7jzbf6uZcoG755vPd8zEV1uEvnkQuQ0aXf9bW7JP/0\"},\"likeCount\":14,\"liked\":false,\"hot\":true},{\"id\":765811272330547200,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":53,\"message\":\"竟然没有生化危机、寂静岭、万能钥匙、死寂、黑衣女人、潜伏、安娜贝尔、招魂、孤儿怨、花子、富江、鬼水凶灵、咒怨、裂口女、异度空间、见鬼、饺子、僵尸…\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471418610000,\"user\":{\"uid\":213701000,\"nickname\":\"Rayi\",\"avatar\":\"http://wx.qlogo.cn/mmopen/Qt51U1u6cZWMl6MyePvPpQXEWten9lz9b2PY4iaib2ZbjH9h161DxowK6wMSQHiaVQQSaqswnuNtAfAaBGsaGdAZnLgLLcMTFfH/0\"},\"likeCount\":10,\"liked\":false,\"hot\":true},{\"id\":765905907849658368,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":64,\"message\":\"还有《鬼娃大电影》两部我去，这明明是喜剧片啊\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471441172000,\"user\":{\"uid\":196119418,\"nickname\":\"卧槽昵称怎么都被占用了\",\"avatar\":\"http://tp3.sinaimg.cn/5579559502/180/5723132115/1\"},\"likeCount\":1,\"liked\":false,\"hot\":false},{\"id\":765893996932272128,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":63,\"message\":\"有很多不能算是恐怖片吧\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471438333000,\"user\":{\"uid\":195782838,\"nickname\":\"Azel\",\"avatar\":\"http://wx.qlogo.cn/mmopen/UY0uvpSI4sXjc5D1ptlFEjLiaIianGda8uAKDsoKBYKByEsEBg0fUhNTH1B5fewqklYTEuKL577q5hVgtEcIyahKbdQ2c6uzw1/0\"},\"likeCount\":0,\"liked\":false,\"hot\":false},{\"id\":765892601453772800,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":62,\"message\":\"剪的非常好，不要用自己觉得好看的去揣测电影史\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471438000000,\"user\":{\"uid\":214932798,\"nickname\":\"826828557\",\"avatar\":\"http://img.wdjimg.com/image/account/2fcb3a1052a14c2c801e1a4c7e41de3b_240_240.png\"},\"likeCount\":0,\"liked\":false,\"hot\":false},{\"id\":765891699762298880,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":61,\"message\":\"封面左是希区柯克的精神病患者（惊魂记），封面中是库布里克的闪灵，右不太清楚。\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471437785000,\"user\":{\"uid\":222253938,\"nickname\":\"封雨嘯 Tesla\",\"avatar\":\"http://wx.qlogo.cn/mmopen/L1QEH9wemVLx6icDal5f5opiaf4hhM9OE9HxibITLGAvPJRvkbW1qZzr8AxspgNbqazXvyg6mW7P2cyzM76ElI2ib49bX3WQnjq0/0\"},\"likeCount\":0,\"liked\":false,\"hot\":false},{\"id\":765882492749123584,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":60,\"message\":\"还是觉得亚洲人的恐怖片较为恐怖\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471435590000,\"user\":{\"uid\":205560104,\"nickname\":\"幽游之香\",\"avatar\":\"http://wx.qlogo.cn/mmopen/L1QEH9wemVJ0ibEY1oRnYF7fUrZJibA1ibSvUJ4bUJic8JIE6Mylwsac9VvG7x6RQicc1tjFW3SOrg3e5RxkmCkE3ZvPwudhLTJzb/0\"},\"likeCount\":0,\"liked\":false,\"hot\":false},{\"id\":765873304643862528,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":59,\"message\":\"哥斯拉 汉江怪物算什么恐怖片\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471433399000,\"user\":{\"uid\":222160794,\"nickname\":\"你想知道吗\",\"avatar\":\"http://img.wdjimg.com/image/account/7511e904c5bf88e338d86f846d7690fc_240_240.png\"},\"likeCount\":0,\"liked\":false,\"hot\":false},{\"id\":765870819376140288,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":58,\"message\":\"呵呵 汉江垃圾?你去拍一个?\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471432807000,\"user\":{\"uid\":224003332,\"nickname\":\"MaYoNaez\",\"avatar\":\"http://img.wdjimg.com/image/account/5f0f33865a742d77f47fcda4847bcd41_240_240.png\"},\"likeCount\":0,\"liked\":false,\"hot\":false},{\"id\":765866607292485632,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":57,\"message\":\"整个进化史就是从极速模式到流畅模式到标清模式到高清模式到超清模式到原话模式到会员模式。。。\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471431802000,\"user\":{\"uid\":226720764,\"nickname\":\"qzuser\",\"avatar\":\"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/100\"},\"likeCount\":2,\"liked\":false,\"hot\":false},{\"id\":765840593917087744,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":56,\"message\":\"还是觉得原来的恐怖片好看 始終给人一种压抑的感覺  鬼影实录不错\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471425600000,\"user\":{\"uid\":226297926,\"nickname\":\"尉多多已經超神啦\",\"avatar\":\"http://tva2.sinaimg.cn/crop.0.0.1424.1424.180/006tKgrTjw8f446s05xhvj313k13kq6w.jpg\"},\"likeCount\":1,\"liked\":false,\"hot\":false},{\"id\":765832902830227456,\"videoId\":7844,\"videoTitle\":\"中元节特条：恐怖片的历史\",\"parentReplyId\":0,\"sequence\":55,\"message\":\"这个剪辑里面也就招魂，电锯惊魂等几个还可以值得一看。没有驱魔人和死寂等几个经典电影，还有就是像汉江怪物等几个垃圾片也能算恐怖片？\",\"replyStatus\":\"PUBLISHED\",\"createTime\":1471423767000,\"user\":{\"uid\":199429214,\"nickname\":\"海纳\",\"avatar\":\"http://wx.qlogo.cn/mmopen/UY0uvpSI4sXjc5D1ptlFEquFiaBibAiaZKu68NrzkA9icXJnsOpKGdl4ybInyPaAqDpjrAzqxocRTr6bSwPhPib1y7CUU56Xz2M9j/0\"},\"likeCount\":1,\"liked\":false,\"hot\":false}],\"count\":13,\"total\":64,\"nextPageUrl\":\"http://baobab.wandoujia.com/api/v1/replies/video?lastId=55&id=7844&num=10\"}");
    }




    public String scriptTag(String js)
    {
        return "<script src='"+js+"'>"+"</script>";
    }



    public static class UrlBean {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}