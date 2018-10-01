package virtue.pojo;


import cn.afterturn.easypoi.excel.annotation.Excel;

public class Mortgage {
    @Excel(name = "hisRightNum")
    private String hisRightNum;
    @Excel(name="ownerName")
    private String ownerName;
    @Excel(name = "securityNumber")
    private String securityNumber;
    private String area;
    private String search_target;

    public Mortgage() {
    }

    public Mortgage(String hisRightNum, String ownerName, String securityNumber, String area, String search_target) {
        this.hisRightNum = hisRightNum;
        this.ownerName = ownerName;
        this.securityNumber = securityNumber;
        this.area = area;
        this.search_target = search_target;
    }

    public String getHisRightNum() {
        return hisRightNum;
    }

    public void setHisRightNum(String hisRightNum) {
        this.hisRightNum = hisRightNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSearch_target() {
        return search_target;
    }

    public void setSearch_target(String search_target) {
        this.search_target = search_target;
    }

    @Override
    public String toString() {
        return "Mortgage{" +
                "hisRightNum='" + hisRightNum + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", securityNumber='" + securityNumber + '\'' +
                ", area='" + area + '\'' +
                ", search_target='" + search_target + '\'' +
                '}';
    }
}
