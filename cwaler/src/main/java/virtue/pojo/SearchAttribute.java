package virtue.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class SearchAttribute {
    @Excel(name = "序号")
    private String id;
    @Excel(name = "地址")
    private String address;
    @Excel(name = "所有人")
    private String owner;
    @Excel(name = "产证号1")
    private String certNum1;
    @Excel(name = "产证号2")
    private String certNum2;
    @Excel(name = "产证号3")
    private String certNum3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCertNum1() {
        return certNum1;
    }

    public void setCertNum1(String certNum1) {
        this.certNum1 = certNum1;
    }

    public String getCertNum2() {
        return certNum2;
    }

    public void setCertNum2(String certNum2) {
        this.certNum2 = certNum2;
    }

    public String getCertNum3() {
        return certNum3;
    }

    public void setCertNum3(String certNum3) {
        this.certNum3 = certNum3;
    }
}
