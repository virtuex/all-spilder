package virtue.util;

import org.junit.Test;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
    //高新区
    public static String COMMON_XIN = "新";
    public static String COMMON_DI_5 = "第5";
    public static String GAO_XIN_QU = "高新区";

    //常熟
    public static String COMMON_CHANG = "常";
    public static String CHANG_SHU = "常熟";
    //吴中区
    public static String COMMON_WUZHONG = "吴中";
    public static String COMMON_DI_6 = "第6";
    public static String WU_ZHONG_QU = "吴中区";
    //相城
    public static String COMMON_XIANGCHENG = "相城";
    public static String COMMON_DI_7 = "第7";
    public static String XIANG_CHENG_QU = "相城区";
    //园区
    public static String COMMON_YUANQU = "园区";
    public static String YUAN_QU = "园区";
    //姑苏区
    public static String COMMON_SHIQU = "市区";
    public static String COMMON_DI_8 = "第8";
    public static String COMMON_GUSU = "姑苏";
    public static String GU_SU_QU = "姑苏区";
    //吴江区
    public static String COMMON_WUJIANG = "吴江";
    public static String COMMON_DI_9 = "第9";
    public static String WU_JIANG_QU = "吴江区";
    //
    public static String UNKOWN = "未知";
    public static String DI="第";
    public static String HAO="号";
    public static String SHU="虞山";




    public static String computeArea(String hisRight) {
        if(hisRight.contains(COMMON_XIN)||hisRight.contains(COMMON_DI_5)){
            return GAO_XIN_QU;
        }

        if(hisRight.contains(COMMON_CHANG)){
            return CHANG_SHU;
        }

        if(hisRight.contains(COMMON_WUZHONG)||hisRight.contains(COMMON_DI_6)){
            return WU_ZHONG_QU;
        }
        if(hisRight.contains(COMMON_XIANGCHENG)||hisRight.contains(COMMON_DI_7)){
            return XIANG_CHENG_QU;
        }
        if(hisRight.contains(COMMON_YUANQU)){
            return YUAN_QU;
        }
        if(hisRight.contains(COMMON_SHIQU)||hisRight.contains(COMMON_DI_8)||hisRight.contains(COMMON_GUSU)){
            return GU_SU_QU;
        }
        if(hisRight.contains(COMMON_WUJIANG)||hisRight.contains(COMMON_DI_9)){
            return WU_JIANG_QU;
        }
        return  UNKOWN;
    }

    /**
     * ������Ȩ֤���ж��ǲ�������
     */
    public static Map<String,Object> isNewCert(String securityNum){
        //System.out.println("处理 "+securityNum);
        Pattern p = Pattern.compile("[1-2][0-9][0-9][0-9]");//���2��ָ�������ֵ����ٸ���
        Matcher m = p.matcher(securityNum);
        Map<String,Object> values = new HashMap<>();
        int start = securityNum.indexOf(DI)+1;
        int end = securityNum.indexOf(HAO);
        int i = 0;
        if(m.find()&&securityNum.indexOf(m.group(0))<start){
            String year = m.group(0);
            String securiNum =null;
            if(securityNum.contains(HAO)) {
                securiNum = securityNum.subSequence(start, end).toString();
            }else{
                securiNum = securityNum.subSequence(start, securityNum.length()).toString();
            }
            values.put("isNew",true);
            values.put("year",year);
            values.put("securiNum",securiNum);
        }else{
            String securiNum = securityNum.subSequence(securityNum.indexOf(DI)+1, securityNum.indexOf(HAO)).toString();
           // System.out.println("aaa"+securiNum);
            values.put("isNew",false);
            values.put("securiNum",securiNum);
        }

        return values;
    }

    @Test
    public void test(){
        Pattern p = Pattern.compile("[1-2][0-9][0-9][0-9]");//���2��ָ�������ֵ����ٸ���
        Matcher m = p.matcher("苏房权证相城字第30171517号");
    }
}
