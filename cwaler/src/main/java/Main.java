
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import virtue.dto.DbOpFactory;
import virtue.pojo.Mortgage;
import virtue.util.DataUtil;
import virtue.util.PoiUtils;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Mortgage> mortgages = PoiUtils.importExcel("file/info.xlsx", 0, 1, Mortgage.class);
        //对每一条数据进行处理，并添加到数据库中
        DbOpFactory instance = DbOpFactory.getInstance();
        instance.init();
        for (Mortgage mortgage : mortgages) {
            String area = DataUtil.computeArea(mortgage.getHisRightNum());
            if (null == (mortgage.getSecurityNumber())) {
                System.out.println("无无权证号，跳过本条数据处理" + mortgage);
                continue;
            }
            mortgage.setArea(area);
            //物权证号
            String securityNumber = mortgage.getSecurityNumber();
            Map<String, Object> newCert = DataUtil.isNewCert(securityNumber);

            String target = null;
            //对常熟进行处理
            if (area.equals(DataUtil.CHANG_SHU)) {
                if ((boolean) newCert.get("isNew")) {
//                    System.out.println("新");
                    target = mortgage.getOwnerName() + "$苏" + "(" + newCert.get("year") + ")" + "常熟市不动产权第" + newCert.get("securiNum") + "号";
                } else {
//                    System.out.println("老");
                    if (securityNumber.contains(DataUtil.SHU)) {
//                        System.out.println("由虞山");
                        target = mortgage.getOwnerName() + "$虞山" + newCert.get("securiNum");
                    } else {
//                        System.out.println("无虞山");
                        target = mortgage.getOwnerName() + "$" + newCert.get("securiNum");
                    }
                }

            }
            //如果是高新区
            if(area.equals(DataUtil.GAO_XIN_QU)){
                //市区的年份、产权号规则和物权号码相同，可使用相同规则
                String hisRightNum = mortgage.getHisRightNum();
                Map<String, Object> newCert1 = DataUtil.isNewCert(hisRightNum);
                //获取产权号
                String securiNum = (String)newCert1.get("securiNum");
                if(securiNum.length()==8){
                    target = "02"+securiNum;
                }
                if(securiNum.length()==7){
                    target = "020"+securiNum;
                }
            }

            //如果是姑苏区、相城区、吴中区、工业园区、吴江区
            if(area.equals(DataUtil.GU_SU_QU)||
                    area.equals(DataUtil.XIANG_CHENG_QU)||
                    area.equals(DataUtil.WU_ZHONG_QU)||
                    area.equals(DataUtil.WU_JIANG_QU)||
                    area.equals(DataUtil.YUAN_QU)){
                //市区的年份、产权号规则和物权号码相同，可使用相同规则
                String hisRightNum = mortgage.getHisRightNum();
                Map<String, Object> newCert1 = DataUtil.isNewCert(hisRightNum);
                Boolean isNew = (Boolean) newCert1.get("isNew");
                if(isNew){
                    //如果是新证，有日期和产权号
                    String year = (String) newCert1.get("year");
                    String securiNum = (String)newCert1.get("securiNum");
                    target = year+securiNum;
                }else{
                    //如果是旧证，只有产权号
                    String securiNum = (String)newCert1.get("securiNum");
                    target = securiNum;
                }
            }
            mortgage.setSearch_target(target);


            System.out.println(mortgage);
            instance.insert(mortgage);

        }
        //释放资源
        instance.close();
    }
}

