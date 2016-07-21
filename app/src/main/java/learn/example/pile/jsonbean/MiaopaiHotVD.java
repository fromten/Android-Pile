package learn.example.pile.jsonbean;

import java.util.List;

/**
 * Created on 2016/7/21.
 */
public class MiaopaiHotVD {

    private int status;
    private String msg;
    private int per;
    private int page;
    private int total;
    private String date;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getPer() {
        return per;
    }

    public int getPage() {
        return page;
    }

    public int getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public static class ResultBean {
        private String type;
        private ChannelBean channel;


        public static class ChannelBean {
            private String scid;
            private int type;
            private int liveStatus;
            private StatBean stat;
            private StreamBean stream;
            private ExtBean ext;

            public String getScid() {
                return scid;
            }

            public int getType() {
                return type;
            }

            public int getLiveStatus() {
                return liveStatus;
            }

            public StatBean getStat() {
                return stat;
            }

            public StreamBean getStream() {
                return stream;
            }

            public ExtBean getExt() {
                return ext;
            }

            public static class StatBean {
                private int vcnt;
                private String vcntNice;
                private int ccnt;
                private int scnt;
                private int dcnt;
                private int lcnt;
                private int hcnt;

                public int getVcnt() {
                    return vcnt;
                }

                public String getVcntNice() {
                    return vcntNice;
                }

                public int getCcnt() {
                    return ccnt;
                }

                public int getScnt() {
                    return scnt;
                }

                public int getDcnt() {
                    return dcnt;
                }

                public int getLcnt() {
                    return lcnt;
                }

                public int getHcnt() {
                    return hcnt;
                }
            }

            public static class PicBean{
                private String base;
                private String m;
                private String mr;
                private String s;

                public String getBase() {
                    return base;
                }

                public String getM() {
                    return m;
                }

                public String getMr() {
                    return mr;
                }

                public String getS() {
                    return s;
                }
            }


            public static class StreamBean {
                private String base;
                private String ios;
                private String and;
                private String vend;
                private String ver;
                private String sign;


                public String getBase() {
                    return base;
                }

                public String getIos() {
                    return ios;
                }

                public String getAnd() {
                    return and;
                }

                public String getVend() {
                    return vend;
                }

                public String getVer() {
                    return ver;
                }

                public String getSign() {
                    return sign;
                }
            }

            public static class ExtBean {

                private String ft;
                private OwnerBean owner;

                public OwnerBean getOwner() {
                    return owner;
                }

                public String getFt() {
                    return ft;
                }

                public static class OwnerBean {
                    private String suid;
                    private String loginName;
                    private String nick;
                    private String icon;
                    private String oldIcon;
                    private boolean v;
                    private int org_v;
                    private int top_num;
                    private String info;
                    private int status;
                    private int gold;
                    private int talent_v;
                    private String talent_name;
                    private int talent_signed;
                    private String signed_info;


                    public String getSuid() {
                        return suid;
                    }

                    public String getLoginName() {
                        return loginName;
                    }

                    public String getNick() {
                        return nick;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public String getOldIcon() {
                        return oldIcon;
                    }

                    public boolean isV() {
                        return v;
                    }

                    public int getOrg_v() {
                        return org_v;
                    }

                    public int getTop_num() {
                        return top_num;
                    }

                    public String getInfo() {
                        return info;
                    }

                    public int getStatus() {
                        return status;
                    }

                    public int getGold() {
                        return gold;
                    }

                    public int getTalent_v() {
                        return talent_v;
                    }

                    public String getTalent_name() {
                        return talent_name;
                    }

                    public int getTalent_signed() {
                        return talent_signed;
                    }

                    public String getSigned_info() {
                        return signed_info;
                    }
                }
            }

        }
    }
}
