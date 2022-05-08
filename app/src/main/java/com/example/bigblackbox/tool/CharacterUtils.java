package com.example.bigblackbox.tool;

import java.nio.ByteBuffer;
import java.util.TreeMap;

public class CharacterUtils {

    private static final TreeMap<Integer, String> spellTree = new TreeMap<>();

    static {
        initTreeMap();
    }

    private CharacterUtils() {
    }

    private static void initTreeMap() {
        spellTree.put(-20319, "a");
        spellTree.put(-20317, "ai");
        spellTree.put(-20304, "an");
        spellTree.put(-20295, "ang");
        spellTree.put(-20292, "ao");
        spellTree.put(-20283, "ba");
        spellTree.put(-20265, "bai");
        spellTree.put(-20257, "ban");
        spellTree.put(-20242, "bang");
        spellTree.put(-20230, "bao");
        spellTree.put(-20051, "bei");
        spellTree.put(-20036, "ben");
        spellTree.put(-20032, "beng");
        spellTree.put(-20026, "bi");
        spellTree.put(-20002, "bian");
        spellTree.put(-19990, "biao");
        spellTree.put(-19986, "bie");
        spellTree.put(-19982, "bin");
        spellTree.put(-19976, "bing");
        spellTree.put(-19805, "bo");
        spellTree.put(-19784, "bu");
        spellTree.put(-19775, "ca");
        spellTree.put(-19774, "cai");
        spellTree.put(-19763, "can");
        spellTree.put(-19756, "cang");
        spellTree.put(-19751, "cao");
        spellTree.put(-19746, "ce");
        spellTree.put(-19741, "ceng");
        spellTree.put(-19739, "cha");
        spellTree.put(-19728, "chai");
        spellTree.put(-19725, "chan");
        spellTree.put(-19715, "chang");
        spellTree.put(-19540, "chao");
        spellTree.put(-19531, "che");
        spellTree.put(-19525, "chen");
        spellTree.put(-19515, "cheng");
        spellTree.put(-19500, "chi");
        spellTree.put(-19484, "chong");
        spellTree.put(-19479, "chou");
        spellTree.put(-19467, "chu");
        spellTree.put(-19289, "chuai");
        spellTree.put(-19288, "chuan");
        spellTree.put(-19281, "chuang");
        spellTree.put(-19275, "chui");
        spellTree.put(-19270, "chun");
        spellTree.put(-19263, "chuo");
        spellTree.put(-19261, "ci");
        spellTree.put(-19249, "cong");
        spellTree.put(-19243, "cou");
        spellTree.put(-19242, "cu");
        spellTree.put(-19238, "cuan");
        spellTree.put(-19235, "cui");
        spellTree.put(-19227, "cun");
        spellTree.put(-19224, "cuo");
        spellTree.put(-19218, "da");
        spellTree.put(-19212, "dai");
        spellTree.put(-19038, "dan");
        spellTree.put(-19023, "dang");
        spellTree.put(-19018, "dao");
        spellTree.put(-19006, "de");
        spellTree.put(-19003, "deng");
        spellTree.put(-18996, "di");
        spellTree.put(-18977, "dian");
        spellTree.put(-18961, "diao");
        spellTree.put(-18952, "die");
        spellTree.put(-18783, "ding");
        spellTree.put(-18774, "diu");
        spellTree.put(-18773, "dong");
        spellTree.put(-18763, "dou");
        spellTree.put(-18756, "du");
        spellTree.put(-18741, "duan");
        spellTree.put(-18735, "dui");
        spellTree.put(-18731, "dun");
        spellTree.put(-18722, "duo");
        spellTree.put(-18710, "e");
        spellTree.put(-18697, "en");
        spellTree.put(-18696, "er");
        spellTree.put(-18526, "fa");
        spellTree.put(-18518, "fan");
        spellTree.put(-18501, "fang");
        spellTree.put(-18490, "fei");
        spellTree.put(-18478, "fen");
        spellTree.put(-18463, "feng");
        spellTree.put(-18448, "fo");
        spellTree.put(-18447, "fou");
        spellTree.put(-18446, "fu");
        spellTree.put(-18239, "ga");
        spellTree.put(-18237, "gai");
        spellTree.put(-18231, "gan");
        spellTree.put(-18220, "gang");
        spellTree.put(-18211, "gao");
        spellTree.put(-18201, "ge");
        spellTree.put(-18184, "gei");
        spellTree.put(-18183, "gen");
        spellTree.put(-18181, "geng");
        spellTree.put(-18012, "gong");
        spellTree.put(-17997, "gou");
        spellTree.put(-17988, "gu");
        spellTree.put(-17970, "gua");
        spellTree.put(-17964, "guai");
        spellTree.put(-17961, "guan");
        spellTree.put(-17950, "guang");
        spellTree.put(-17947, "gui");
        spellTree.put(-17931, "gun");
        spellTree.put(-17928, "guo");
        spellTree.put(-17922, "ha");
        spellTree.put(-17759, "hai");
        spellTree.put(-17752, "han");
        spellTree.put(-17733, "hang");
        spellTree.put(-17730, "hao");
        spellTree.put(-17721, "he");
        spellTree.put(-17703, "hei");
        spellTree.put(-17701, "hen");
        spellTree.put(-17697, "heng");
        spellTree.put(-17692, "hong");
        spellTree.put(-17683, "hou");
        spellTree.put(-17676, "hu");
        spellTree.put(-17496, "hua");
        spellTree.put(-17487, "huai");
        spellTree.put(-17482, "huan");
        spellTree.put(-17468, "huang");
        spellTree.put(-17454, "hui");
        spellTree.put(-17433, "hun");
        spellTree.put(-17427, "huo");
        spellTree.put(-17417, "ji");
        spellTree.put(-17202, "jia");
        spellTree.put(-17185, "jian");
        spellTree.put(-16983, "jiang");
        spellTree.put(-16970, "jiao");
        spellTree.put(-16942, "jie");
        spellTree.put(-16915, "jin");
        spellTree.put(-16733, "jing");
        spellTree.put(-16708, "jiong");
        spellTree.put(-16706, "jiu");
        spellTree.put(-16689, "ju");
        spellTree.put(-16664, "juan");
        spellTree.put(-16657, "jue");
        spellTree.put(-16647, "jun");
        spellTree.put(-16474, "ka");
        spellTree.put(-16470, "kai");
        spellTree.put(-16465, "kan");
        spellTree.put(-16459, "kang");
        spellTree.put(-16452, "kao");
        spellTree.put(-16448, "ke");
        spellTree.put(-16433, "ken");
        spellTree.put(-16429, "keng");
        spellTree.put(-16427, "kong");
        spellTree.put(-16423, "kou");
        spellTree.put(-16419, "ku");
        spellTree.put(-16412, "kua");
        spellTree.put(-16407, "kuai");
        spellTree.put(-16403, "kuan");
        spellTree.put(-16401, "kuang");
        spellTree.put(-16393, "kui");
        spellTree.put(-16220, "kun");
        spellTree.put(-16216, "kuo");
        spellTree.put(-16212, "la");
        spellTree.put(-16205, "lai");
        spellTree.put(-16202, "lan");
        spellTree.put(-16187, "lang");
        spellTree.put(-16180, "lao");
        spellTree.put(-16171, "le");
        spellTree.put(-16169, "lei");
        spellTree.put(-16158, "leng");
        spellTree.put(-16155, "li");
        spellTree.put(-15959, "lia");
        spellTree.put(-15958, "lian");
        spellTree.put(-15944, "liang");
        spellTree.put(-15933, "liao");
        spellTree.put(-15920, "lie");
        spellTree.put(-15915, "lin");
        spellTree.put(-15903, "ling");
        spellTree.put(-15889, "liu");
        spellTree.put(-15878, "long");
        spellTree.put(-15707, "lou");
        spellTree.put(-15701, "lu");
        spellTree.put(-15681, "lv");
        spellTree.put(-15667, "luan");
        spellTree.put(-15661, "lue");
        spellTree.put(-15659, "lun");
        spellTree.put(-15652, "luo");
        spellTree.put(-15640, "ma");
        spellTree.put(-15631, "mai");
        spellTree.put(-15625, "man");
        spellTree.put(-15454, "mang");
        spellTree.put(-15448, "mao");
        spellTree.put(-15436, "me");
        spellTree.put(-15435, "mei");
        spellTree.put(-15419, "men");
        spellTree.put(-15416, "meng");
        spellTree.put(-15408, "mi");
        spellTree.put(-15394, "mian");
        spellTree.put(-15385, "miao");
        spellTree.put(-15377, "mie");
        spellTree.put(-15375, "min");
        spellTree.put(-15369, "ming");
        spellTree.put(-15363, "miu");
        spellTree.put(-15362, "mo");
        spellTree.put(-15183, "mou");
        spellTree.put(-15180, "mu");
        spellTree.put(-15165, "na");
        spellTree.put(-15158, "nai");
        spellTree.put(-15153, "nan");
        spellTree.put(-15150, "nang");
        spellTree.put(-15149, "nao");
        spellTree.put(-15144, "ne");
        spellTree.put(-15143, "nei");
        spellTree.put(-15141, "nen");
        spellTree.put(-15140, "neng");
        spellTree.put(-15139, "ni");
        spellTree.put(-15128, "nian");
        spellTree.put(-15121, "niang");
        spellTree.put(-15119, "niao");
        spellTree.put(-15117, "nie");
        spellTree.put(-15110, "nin");
        spellTree.put(-15109, "ning");
        spellTree.put(-14941, "niu");
        spellTree.put(-14937, "nong");
        spellTree.put(-14933, "nu");
        spellTree.put(-14930, "nv");
        spellTree.put(-14929, "nuan");
        spellTree.put(-14928, "nue");
        spellTree.put(-14926, "nuo");
        spellTree.put(-14922, "o");
        spellTree.put(-14921, "ou");
        spellTree.put(-14914, "pa");
        spellTree.put(-14908, "pai");
        spellTree.put(-14902, "pan");
        spellTree.put(-14894, "pang");
        spellTree.put(-14889, "pao");
        spellTree.put(-14882, "pei");
        spellTree.put(-14873, "pen");
        spellTree.put(-14871, "peng");
        spellTree.put(-14857, "pi");
        spellTree.put(-14678, "pian");
        spellTree.put(-14674, "piao");
        spellTree.put(-14670, "pie");
        spellTree.put(-14668, "pin");
        spellTree.put(-14663, "ping");
        spellTree.put(-14654, "po");
        spellTree.put(-14645, "pu");
        spellTree.put(-14630, "qi");
        spellTree.put(-14594, "qia");
        spellTree.put(-14429, "qian");
        spellTree.put(-14407, "qiang");
        spellTree.put(-14399, "qiao");
        spellTree.put(-14384, "qie");
        spellTree.put(-14379, "qin");
        spellTree.put(-14368, "qing");
        spellTree.put(-14355, "qiong");
        spellTree.put(-14353, "qiu");
        spellTree.put(-14345, "qu");
        spellTree.put(-14170, "quan");
        spellTree.put(-14159, "que");
        spellTree.put(-14151, "qun");
        spellTree.put(-14149, "ran");
        spellTree.put(-14145, "rang");
        spellTree.put(-14140, "rao");
        spellTree.put(-14137, "re");
        spellTree.put(-14135, "ren");
        spellTree.put(-14125, "reng");
        spellTree.put(-14123, "ri");
        spellTree.put(-14122, "rong");
        spellTree.put(-14112, "rou");
        spellTree.put(-14109, "ru");
        spellTree.put(-14099, "ruan");
        spellTree.put(-14097, "rui");
        spellTree.put(-14094, "run");
        spellTree.put(-14092, "ruo");
        spellTree.put(-14090, "sa");
        spellTree.put(-14087, "sai");
        spellTree.put(-14083, "san");
        spellTree.put(-13917, "sang");
        spellTree.put(-13914, "sao");
        spellTree.put(-13910, "se");
        spellTree.put(-13907, "sen");
        spellTree.put(-13906, "seng");
        spellTree.put(-13905, "sha");
        spellTree.put(-13896, "shai");
        spellTree.put(-13894, "shan");
        spellTree.put(-13878, "shang");
        spellTree.put(-13870, "shao");
        spellTree.put(-13859, "she");
        spellTree.put(-13847, "shen");
        spellTree.put(-13831, "sheng");
        spellTree.put(-13658, "shi");
        spellTree.put(-13611, "shou");
        spellTree.put(-13601, "shu");
        spellTree.put(-13406, "shua");
        spellTree.put(-13404, "shuai");
        spellTree.put(-13400, "shuan");
        spellTree.put(-13398, "shuang");
        spellTree.put(-13395, "shui");
        spellTree.put(-13391, "shun");
        spellTree.put(-13387, "shuo");
        spellTree.put(-13383, "si");
        spellTree.put(-13367, "song");
        spellTree.put(-13359, "sou");
        spellTree.put(-13356, "su");
        spellTree.put(-13343, "suan");
        spellTree.put(-13340, "sui");
        spellTree.put(-13329, "sun");
        spellTree.put(-13326, "suo");
        spellTree.put(-13318, "ta");
        spellTree.put(-13147, "tai");
        spellTree.put(-13138, "tan");
        spellTree.put(-13120, "tang");
        spellTree.put(-13107, "tao");
        spellTree.put(-13096, "te");
        spellTree.put(-13095, "teng");
        spellTree.put(-13091, "ti");
        spellTree.put(-13076, "tian");
        spellTree.put(-13068, "tiao");
        spellTree.put(-13063, "tie");
        spellTree.put(-13060, "ting");
        spellTree.put(-12888, "tong");
        spellTree.put(-12875, "tou");
        spellTree.put(-12871, "tu");
        spellTree.put(-12860, "tuan");
        spellTree.put(-12858, "tui");
        spellTree.put(-12852, "tun");
        spellTree.put(-12849, "tuo");
        spellTree.put(-12838, "wa");
        spellTree.put(-12831, "wai");
        spellTree.put(-12829, "wan");
        spellTree.put(-12812, "wang");
        spellTree.put(-12802, "wei");
        spellTree.put(-12607, "wen");
        spellTree.put(-12597, "weng");
        spellTree.put(-12594, "wo");
        spellTree.put(-12585, "wu");
        spellTree.put(-12556, "xi");
        spellTree.put(-12359, "xia");
        spellTree.put(-12346, "xian");
        spellTree.put(-12320, "xiang");
        spellTree.put(-12300, "xiao");
        spellTree.put(-12120, "xie");
        spellTree.put(-12099, "xin");
        spellTree.put(-12089, "xing");
        spellTree.put(-12074, "xiong");
        spellTree.put(-12067, "xiu");
        spellTree.put(-12058, "xu");
        spellTree.put(-12039, "xuan");
        spellTree.put(-11867, "xue");
        spellTree.put(-11861, "xun");
        spellTree.put(-11847, "ya");
        spellTree.put(-11831, "yan");
        spellTree.put(-11798, "yang");
        spellTree.put(-11781, "yao");
        spellTree.put(-11604, "ye");
        spellTree.put(-11589, "yi");
        spellTree.put(-11536, "yin");
        spellTree.put(-11358, "ying");
        spellTree.put(-11340, "yo");
        spellTree.put(-11339, "yong");
        spellTree.put(-11324, "you");
        spellTree.put(-11303, "yu");
        spellTree.put(-11097, "yuan");
        spellTree.put(-11077, "yue");
        spellTree.put(-11067, "yun");
        spellTree.put(-11055, "za");
        spellTree.put(-11052, "zai");
        spellTree.put(-11045, "zan");
        spellTree.put(-11041, "zang");
        spellTree.put(-11038, "zao");
        spellTree.put(-11024, "ze");
        spellTree.put(-11020, "zei");
        spellTree.put(-11019, "zen");
        spellTree.put(-11018, "zeng");
        spellTree.put(-11014, "zha");
        spellTree.put(-10838, "zhai");
        spellTree.put(-10832, "zhan");
        spellTree.put(-10815, "zhang");
        spellTree.put(-10800, "zhao");
        spellTree.put(-10790, "zhe");
        spellTree.put(-10780, "zhen");
        spellTree.put(-10764, "zheng");
        spellTree.put(-10587, "zhi");
        spellTree.put(-10544, "zhong");
        spellTree.put(-10533, "zhou");
        spellTree.put(-10519, "zhu");
        spellTree.put(-10331, "zhua");
        spellTree.put(-10329, "zhuai");
        spellTree.put(-10328, "zhuan");
        spellTree.put(-10322, "zhuang");
        spellTree.put(-10315, "zhui");
        spellTree.put(-10309, "zhun");
        spellTree.put(-10307, "zhuo");
        spellTree.put(-10296, "zi");
        spellTree.put(-10281, "zong");
        spellTree.put(-10274, "zou");
        spellTree.put(-10270, "zu");
        spellTree.put(-10262, "zuan");
        spellTree.put(-10260, "zui");
        spellTree.put(-10256, "zun");
        spellTree.put(-10254, "zuo");
    }

    /**
     * 获得单个汉字的Ascii.
     *
     * @param cn char 汉字字符
     * @return int 错误返回 0,否则返回ascii
     */
    public static int getCnAscii(char cn) {
        try {
            byte[] bytes = (String.valueOf(cn)).getBytes("gb2312");
            if (bytes == null || bytes.length > 2 || bytes.length <= 0) { // 错误
                return 0;
            }
            if (bytes.length == 1) { // 英文字符
                return bytes[0];
            }
            if (bytes.length == 2) { // 中文字符
                ByteBuffer bf = ByteBuffer.wrap(bytes);
                return bf.getShort();
            }
        } catch (Exception e) {
        }

        return 0; // 错误
    }

    /**
     * 根据ASCII码到SpellMap中查找对应的拼音
     *
     * @param ascii int 字符对应的ASCII
     * @return String 拼音, 首先判断ASCII是否>0&<160,如果是返回对应的字符,
     * <p/>
     * 否则到SpellMap中查找,如果没有找到拼音,则返回null,如果找到则返回拼音.
     */
    public static String getSpellByAscii(int ascii) {
        if (ascii > 0 && ascii < 160) { // 单字符
            return String.valueOf((char) ascii);
        } else if (ascii < -20319 || ascii > -10247) { // 不知道的字符
            return null;
        }
        Integer key = spellTree.floorKey(ascii);
        if (key != null) {
            return spellTree.get(key);
        }
        return null;

    }

    /**
     * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
     *
     * @param cnStr String 字符串
     * @return String 转换成全拼后的字符串
     */
    public static String getFullSpell(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }
        char[] chars = cnStr.toCharArray();
        StringBuffer retuBuf = new StringBuffer();
        for (int i = 0, Len = chars.length; i < Len; i++) {
            int ascii = getCnAscii(chars[i]);
            if (ascii == 0) { // 取ascii时出错
                retuBuf.append(chars[i]);
            } else {
                String spell = getSpellByAscii(ascii);
                if (spell == null) {
                    retuBuf.append(chars[i]);
                } else {
                    retuBuf.append(spell);
                } // end of if spell == null
            } // end of if ascii <= -20400
        } // end of for

        return retuBuf.toString();
    }

    /**
     * 返回字符串的拼音的首字母,是汉字转化为全拼,其它字符不进行转换
     *
     * @param cnStr String 字符串
     * @return String 转换成全拼后的字符串的首字母
     */
    public static String getFirstSpell(String cnStr) {
        if (cnStr.substring(0, 1).equals("沣"))
            return "f";
        if (cnStr.substring(0, 1).equals("骊"))
            return "l";
        if (cnStr.substring(0, 1).equals("杈"))
            return "c";
        if (cnStr.substring(0, 1).equals("阿"))
            return "e";
        if (cnStr.substring(0, 1).equals("怡"))
            return "y";
        if (cnStr.substring(0, 1).equals("灞"))
            return "b";
        else
            return getFullSpell(cnStr).substring(0, 1);
    }

    /**
     * 返回字符串每个中文的首字母集合，其他字符不进行转换
     *
     * @param cnStr 转换字符串
     * @return String 每个中文首字母的字符串
     */
    public static String getEveryFirstSpell(String cnStr) {
        String result = "";
        for (int i = 0; i < cnStr.length(); i++) {
            result += getFirstSpell(cnStr.substring(i, i + 1));
        }
        return result;
    }
}