package learn.example.joke;


import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import learn.example.joke.net.StringRequest;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    public String JSONDATA="{\n" +
            "\t\"showapi_res_code\": 0,\n" +
            "\t\"showapi_res_error\": \"\",\n" +
            "\t\"showapi_res_body\": {\n" +
            "\t\t\"allNum\": 2980,\n" +
            "\t\t\"allPages\": 149,\n" +
            "\t\t\"contentlist\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 13:10:26.149\",\n" +
            "\t\t\t\t\"text\": \"新人发帖求过…… 媳妇最近怀孕了…天天这也不想吃那也不想吃…有一天发脾气要我给他做想吃的，结果做了好多还是没有想吃的…最后着急了大喊:再做不出我想吃的我就去大街上要饭……我想说:你吃什么自己都不知道我怎么做啊…唉…想想男人女人都不容易啊…\",\n" +
            "\t\t\t\t\"title\": \"媳妇儿有了…\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 13:10:26.149\",\n" +
            "\t\t\t\t\"text\": \"为了让自己多活动，我把放在电脑桌上的零食拿到了外面的茶几上，这样最起码为了吃我也能走动走动。…………现在我的零食经常会过期……\",\n" +
            "\t\t\t\t\"title\": \"计划失败，吃货兼网虫的悲哀\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 13:10:26.148\",\n" +
            "\t\t\t\t\"text\": \"在家叫我滚出去，在外面喊我滚回家，不补课说我学习差，补课说我浪费钱，吃东西说我嘴巴馋，不吃说我要成仙，在家不学习说我不努力，在家一学习说我只会玩手机，不讲话说我闷得很，讲话说我屁话多，到底想让人怎么样。同意的??一个。\",\n" +
            "\t\t\t\t\"title\": \"内心是崩溃的\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:18.052\",\n" +
            "\t\t\t\t\"text\": \"吃货的最高境界是什么?</p><p>甲:看到什么能吃的都想吃</p><p>乙:看到屎都觉得饿</p><p>.......\",\n" +
            "\t\t\t\t\"title\": \"吃货的最高境界是什么\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:18.052\",\n" +
            "\t\t\t\t\"text\": \"小区锻炼的大爷对我说：“太极拳很厉害，可以以柔克钢。”</p><p>然后我拿了根钢管过去，对大爷说：“大爷您再说一遍！”</p><p>大爷什么也没说，跳起了钢管舞……\",\n" +
            "\t\t\t\t\"title\": \"大爷您再说一遍\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:18.052\",\n" +
            "\t\t\t\t\"text\": \"小时候犯错了，总被妈妈打。</p><p>出于男子汉的气概，我忍住不让眼泪流出来！</p><p>妈妈就说：“哟，还不哭，看来是我打轻了，你肯定不会长记性。”</p><p>完了往死里打。</p><p>实在忍不住哭了，妈妈又说：“还哭，看我不打死你。”\",\n" +
            "\t\t\t\t\"title\": \"还哭，看我不打死你\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:18.052\",\n" +
            "\t\t\t\t\"text\": \"小王：“我厌倦了这个世界，只是想趴在桌子上一个人静静。”</p><p>老湿：“上课睡觉你还有理了，滚粗去……”\",\n" +
            "\t\t\t\t\"title\": \"想趴在桌子上一个人静静\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:18.050\",\n" +
            "\t\t\t\t\"text\": \"老师：“今天我们谈谈理想吧！”</p><p>小红：“我的理想是当个科学家。”</p><p>小王：“我的理想是当作家。”</p><p>小明：“我想当个印刷工。”</p><p>老师：“小明啊，那个理想立志要高远。”</p><p>小明：“再搞一台印钞机。”\",\n" +
            "\t\t\t\t\"title\": \"不得了的志向\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:18.004\",\n" +
            "\t\t\t\t\"text\": \"老王去牙科诊所拔牙，牙医收费300元。</p><p>老王不满地说：“太过分了，你拔牙只用了2分钟，就要300元。”</p><p>牙医说：“如果你嫌太快了，我也可以慢慢帮你拔，拔到你满意为止。”\",\n" +
            "\t\t\t\t\"title\": \"拔到你满意为止\",\n" +
            "\t\t\t\t\"type\": 1\n" +
            "\t\t\t} \n" +
            "\t\t],\n" +
            "\t\t\"currentPage\": 1,\n" +
            "\t\t\"maxResult\": 20\n" +
            "\t}\n" +
            "}";
    public String IMGDATA="{\n" +
            "\t\"showapi_res_code\": 0,\n" +
            "\t\"showapi_res_error\": \"\",\n" +
            "\t\"showapi_res_body\": {\n" +
            "\t\t\"allNum\": 4760,\n" +
            "\t\t\"allPages\": 238,\n" +
            "\t\t\"contentlist\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 13:10:36.891\",\n" +
            "\t\t\t\t\"img\": \"http://img.hao123.com/data/3_1b72caa7998cf674fecb4f334cf9d356_430\",\n" +
            "\t\t\t\t\"title\": \"牙膏还有这技能。\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 13:10:36.890\",\n" +
            "\t\t\t\t\"img\": \"http://img2.hao123.com/data/3_43e3b8be20d9a50d59f388e15fac4dea_0\",\n" +
            "\t\t\t\t\"title\": \"这车技好牛啊\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 13:10:36.887\",\n" +
            "\t\t\t\t\"img\": \"http://img6.hao123.com/data/3_43308c5c7011d45ab72ae83c6c1faea8_430\",\n" +
            "\t\t\t\t\"title\": \"梦回唐朝？？是我眼瞎了吗\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.072\",\n" +
            "\t\t\t\t\"img\": \"http://img4.hao123.com/data/3_b125ff2c27ea4375038faa018125dfec_0\",\n" +
            "\t\t\t\t\"title\": \"穿裆大师。。\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.072\",\n" +
            "\t\t\t\t\"img\": \"http://img0.hao123.com/data/3_0346bb0109009f5c3cae75f00b7e5619_0\",\n" +
            "\t\t\t\t\"title\": \"有强迫症的人看完这图神清气爽！\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.071\",\n" +
            "\t\t\t\t\"img\": \"http://img6.hao123.com/data/3_2973561df4e228bdf65bfde2ba43749f_430\",\n" +
            "\t\t\t\t\"title\": \"马力自行车\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.070\",\n" +
            "\t\t\t\t\"img\": \"http://img5.hao123.com/data/3_ac9f9a9595d87645e25280e410e2aee7_430\",\n" +
            "\t\t\t\t\"title\": \"你先尝尝好吃不好吃\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.070\",\n" +
            "\t\t\t\t\"img\": \"http://img3.hao123.com/data/3_8cff92fbc4ad5af17ddfe26d84ac1ccd_430\",\n" +
            "\t\t\t\t\"title\": \"最霸气的情侣装\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.069\",\n" +
            "\t\t\t\t\"img\": \"http://img6.hao123.com/data/3_08aeeb5fca12cef115f7cf29ac0c05db_430\",\n" +
            "\t\t\t\t\"title\": \"这样还不开心？\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.048\",\n" +
            "\t\t\t\t\"img\": \"http://img6.hao123.com/data/3_ba63a743e66085112da979385377389d_430\",\n" +
            "\t\t\t\t\"title\": \"自恋已经不分种族了\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.047\",\n" +
            "\t\t\t\t\"img\": \"http://img0.hao123.com/data/3_21e98b289ac9819483e2f1d3ad11544d_430\",\n" +
            "\t\t\t\t\"title\": \"这VIP车位\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.047\",\n" +
            "\t\t\t\t\"img\": \"http://img4.hao123.com/data/3_b7a5f929908b720c008750b6096fe671_430\",\n" +
            "\t\t\t\t\"title\": \"这丫头是不有点二啊\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 12:10:33.046\",\n" +
            "\t\t\t\t\"img\": \"http://img.hao123.com/data/3_aace2511443d1b81122b21d894055dd4_430\",\n" +
            "\t\t\t\t\"title\": \"这个名字起得好，是什么计生工具呢\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 06:10:31.026\",\n" +
            "\t\t\t\t\"img\": \"http://img2.hao123.com/data/3_e7c7358db1679cbf4bd3268a405fce36_430\",\n" +
            "\t\t\t\t\"title\": \"这样真的能熟吗\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 05:10:28.884\",\n" +
            "\t\t\t\t\"img\": \"http://img6.hao123.com/data/3_f4c342a2d5497c7bcc620bab5675ddb1_0\",\n" +
            "\t\t\t\t\"title\": \"会挥鼻道别的大象\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 05:10:28.879\",\n" +
            "\t\t\t\t\"img\": \"http://img4.hao123.com/data/3_ba7e18f7fe1c333ef8d687b4f3dce9ef_430\",\n" +
            "\t\t\t\t\"title\": \"网购的无线耳机，老板真没骗我\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 05:10:28.879\",\n" +
            "\t\t\t\t\"img\": \"http://img1.hao123.com/data/3_b6580201a4cdf173a224bc77a84a132e_430\",\n" +
            "\t\t\t\t\"title\": \"女汉子的生活中好像没有男人的地位！\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 04:10:27.332\",\n" +
            "\t\t\t\t\"img\": \"http://img5.hao123.com/data/3_8f99653ddc750e8b12a616fa7960d5fa_430\",\n" +
            "\t\t\t\t\"title\": \"这年头，奇葩真多！\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 04:10:27.326\",\n" +
            "\t\t\t\t\"img\": \"http://img0.hao123.com/data/3_f05a32499b428b008b83eeb86c9d9a35_430\",\n" +
            "\t\t\t\t\"title\": \"请记住我的名字\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"ct\": \"2015-08-13 04:10:27.326\",\n" +
            "\t\t\t\t\"img\": \"http://img6.hao123.com/data/3_81706276cc04aff1c4ad8e3c8c3140f0_430\",\n" +
            "\t\t\t\t\"title\": \"哥啊你可真淡定！\",\n" +
            "\t\t\t\t\"type\": 2\n" +
            "\t\t\t}\n" +
            "\t\t],\n" +
            "\t\t\"currentPage\": 1,\n" +
            "\t\t\"maxResult\": 20\n" +
            "\t}\n" +
            "}";
    String NewsData="{\n" +
            "    \"errNum\": 0, // 错误码大于0，表示出错\n" +
            "    \"errMsg\": \"success\", // 错误信息\n" +
            "    \"retData\": [\n" +
            "        {\n" +
            "            \"title\": \"北京四分之三地域将划入生态红线区\", // 新闻标题\n" +
            "            \"url\": \"http://m2.people.cn/r/MV8xXzI3ODczNzczXzEwMDFfMTQ0ODkxNjk1Mw==\",  // 新闻详情页链接地址\n" +
            "            \"abstract\": \"本报北京11月30日电(记者朱竞若、余荣华)北京市规划委副主任王飞11月30日披露,正在修改的北京市总体规划,初步划定生态红线区面积约占市域面积的70%以上,远期生态红线区比例为75%左右。这意味着,未来北京将实现3/4市域为严禁与生态保护无关建设活动的生态红线区。\", // 新闻简介\n" +
            "            \"image_url\": \"\" // 图片链接地址\n" +
            "        }, \n" +
            "        {\n" +
            "            \"title\": \"身孕九个月了，还敢这样跳肚皮舞\", \n" +
            "            \"url\": \"http://toutiao.com/group/6222856594671436033/\", \n" +
            "            \"abstract\": \"52肚皮舞网：看着看着，真怕孩子突然就掉出来了……\", \n" +
            "            \"image_url\": \"http://p1.pstatp.com/list/9849/8223744749\"\n" +
            "        }, \n" +
            "        {\n" +
            "            \"title\": \"红白机魂斗罗超快速通关 小时候要是有这操作就是大红人了\", \n" +
            "            \"url\": \"http://toutiao.com/group/6222558547176063489/\", \n" +
            "            \"abstract\": \"\", \n" +
            "            \"image_url\": \"http://p1.pstatp.com/list/9831/218724483\"\n" +
            "        }, \n" +
            "        {\n" +
            "            \"title\": \"年仅9岁小女孩玩转周立波！这小人精反应太快了!\", \n" +
            "            \"url\": \"http://toutiao.com/group/6222777443272769794/\", \n" +
            "            \"abstract\": \"年仅9岁小女孩玩转周立波！这小人精反应太快了!\", \n" +
            "            \"image_url\": \"http://p3.pstatp.com/list/9841/6985299613\"\n" +
            "        }, \n" +
            "        {\n" +
            "            \"title\": \"陈冠希的采访火成这样，所以就成了新一代偶像？\", \n" +
            "            \"url\": \"http://toutiao.com/group/6222922104024629506/\", \n" +
            "            \"abstract\": \"陈冠希的纪录片今天开播，风头火过彦祖的美剧。一个明星的采访能够火成这样，而且名声反转，冠希哥也算是独家了。vice给陈冠希做的这个纪录片有三集，第一集讲的是陈冠希的嘻哈生意。陈冠希现在的角色更多是一个商人，他在美国的合作伙伴说：He knows his shit,he is a \", \n" +
            "            \"image_url\": \"http://p3.pstatp.com/list/9864/1705453645\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    @Test
    public void addition_isCorrect() {
        String url="http://slide.news.sina.com.cn/s/slide_1_2841_99109.html#p=1";
        String json= StringRequest.request("http://apis.baidu.com/showapi_open_bus/extract/extract","url="+url);
        System.out.println(json);

    }
    public void getException() throws Exception {
         throw new Exception();
    }

}