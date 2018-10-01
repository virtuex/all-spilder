
import virtue.dto.DbOpFactory;
import virtue.pojo.Mortgage;
import virtue.util.DataUtil;
import virtue.util.PoiUtils;

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
                mortgage.setSearch_target(target);

            }
            System.out.println(mortgage);
            instance.insert(mortgage);
        }
    }
}

