package cn.edu.cqupt.rubic_business.util.json;

/**
 * Created by LiuMian on 2015/12/17.
 * 将json字符串 添加 换行符和制表符使其能够直接在网页上按照通常的json格式显示
 */
public class JSONHandler {

    private static int tabLenth = 0;
    private static final String BRACKET_LEFT = "[";
    private static final String BRACKET_RIGHT = "]";
    private static final String BRACE_LEFT = "{";
    private static final String BRACE_RIGHT = "}";
    private static final String COMMA = ",";
    private static final String LINE_BREAK = "\n";
    private static final String TAB = "\t";

    public static String formatForHTML(String src){
        StringBuffer result = new StringBuffer();
        char[] srcArray = src.toCharArray();
        for(int index = 0;index < src.length();index++){
            result.append(srcArray[index]);
            //  {
            if(BRACE_LEFT.equals(String.valueOf(srcArray[index]))){
                result.append(appendLINE_BREAKAndTAB(++tabLenth));
            }

            //  }
            if(BRACE_RIGHT.equals(String.valueOf(srcArray[index]))){
                result.insert(result.length()-1,appendLINE_BREAKAndTAB(--tabLenth));
            }

            //  [
            if(BRACKET_LEFT.equals(String.valueOf(srcArray[index]))){
                result.append(appendLINE_BREAKAndTAB(++tabLenth));
            }

            //  ]
            if(BRACKET_RIGHT.equals(String.valueOf(srcArray[index]))){
                result.insert(result.length()-1,appendLINE_BREAKAndTAB(--tabLenth));
            }

            //  ,
            if(COMMA.equals(String.valueOf(srcArray[index]))){
                result.append(appendLINE_BREAKAndTAB(tabLenth));
            }
        }
        return result.toString();
    }

    private static String appendLINE_BREAKAndTAB(int TABTimes){
        StringBuffer temp = new StringBuffer();
        temp.append(appendLINE_BREAK());
        temp.append(appendTAB(TABTimes));
        return temp.toString();
    }

    private static String appendLINE_BREAK(){
        return LINE_BREAK;
    }

    private static String appendTAB(int TABTimes){
        StringBuffer temp = new StringBuffer();
        for(int i = 0;i<TABTimes;i++){
            temp.append(TAB);
        }
        return temp.toString();
    }

    public static void main(String[] args){
    	String src = "{\"status\": \"3\",\"message\": \"\",\"errCode\": \"0\",\"data\": [{\"time\": \"2013-02-26 16:47\",\"context\": \"客户 同事收发家人 已签收 派件员 张xx\"},{\"time\": \"2013-02-26 07:33\",\"context\": \"吉林省xx市xx公司 的派件员 张金达 派件中 派件员电话15xxx73xx87\"},{\"time\": \"2013-02-26 06:02\",\"context\": \"xx省xx市xx公司 已收入\"},{\"time\": \"2013-02-25 15:42\",\"context\": \"xx省xx转运中心公司已发出\"},{\"time\":\"2013-02-25 14:59\",\"context\":\"xx省xx转运中心公司已拆包\"},{\"time\": \"2013-02-24 18:11\",\"context\":\"辽宁省大连市中山区四部公司 已收件\"},{\"time\": \"2013-02-24 17:59\",\"context\":\"辽宁省大连市公司 已收入\"},{\"time\":\"2013-02-23 17:10\",\"context\":\"辽宁省大连市中山区xxxx公司 的收件员 王xx 已收件\" }],\"html\":\"\",\"mailNo\":\"71xxxxx624\",\"expTextName\":\"圆通快递\",\"expSpellName\":\"yuantong\",\"update\":\"1375155719\",\"cache\":\"33196560\",\"ord\":\"DESC\"}";
    	System.out.println(JSONHandler.formatForHTML(src));
    }

}
