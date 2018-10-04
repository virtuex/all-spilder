import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.corba.se.impl.orbutil.concurrent.CondVar;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import virtue.constant.ConValues;
import virtue.dto.DbOpFactory;
import virtue.pojo.AuctionItem;
import virtue.util.DataUtil;
import virtue.util.JsonUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * 司法拍卖信息执行类
 */
public class Main2 {
    public static void main(String[] args) throws IOException {
        DbOpFactory instance = DbOpFactory.getInstance();
        instance.init();
        //加载Document对象
        int total = 0;
        Document document = Jsoup.connect(ConValues.SF_URL_ENTRANCE).timeout(5000).get();
        Elements select = document.select("div[class=provinces clearfix]");
        //第一层，获取所有省的法院列表
        for(Element element:select){
            String province = "";//省
            String city = "";//市
            String baseUrl = "";//url路径
            String title = "";//拍卖标题
            String price = "";//起拍价
            String countryName="";//法院
            Elements elements = element.select("div[class=province");
            Matcher valueByReg = DataUtil.getValueByReg(ConValues.ZHONG_WEN_REG, elements.toString());
            if(valueByReg.find()){
                //省
                province = valueByReg.group(0);
            }
            //获取该省的所有市的列表
            for(Element var1 : select){
                Elements citys = var1.select("dl[class=city]");
                System.out.println("");
                //处理每个市的法院拍卖信息
                for(Element var2:citys){
                    Elements cityName = var2.select("dt[class=name clearfix]");
                    Matcher valueByReg2 = DataUtil.getValueByReg(ConValues.ZHONG_WEN_REG, cityName.toString());
                    if(valueByReg2.find()){
                        //市
                        city = valueByReg2.group(0);
                    }
                    //得到法院列表
                    Elements countys = var2.select("span[class=county i-b w-b ]");
                    for(Element country:countys){
                        Matcher valueByReg3 = DataUtil.getValueByReg(ConValues.ZHONG_WEN_REG, country.toString());
                        if(valueByReg3.find()){
                            //市
                            countryName = valueByReg3.group(0);
                        }
                        String countryUrl = "http:"+country.select("a").first().attr("href");
                        System.out.println(countryUrl);
                        Document document1 = Jsoup.connect(countryUrl).timeout(5000).get();
                        String basePageUrl = "";
                        int totalPage = 1;
                        //取出页面底部的页码
                        Elements pages = document1.select("div[class=pagination J_Pagination]");
                        if(pages.size()==0){
                            //该法院暂无拍卖信息
                        }else {
                            //主要用于获得每页的连接和总页数
                            for (Element var3 : pages) {
                                Elements var4 = var3.select("a");
                                for(Element var5:var4){
                                    if(var5.hasAttr("rel")){
                                        basePageUrl = "http:"+var5.attr("href").trim();
                                        break;
                                    }
                                }
                                String pageText = var3.select("span[class=page-skip]").select("em[class=page-total]").text();
                                if(pageText.length()>0){
                                    totalPage = Integer.parseInt(pageText);
                                }
                            }
                        }
                        if(basePageUrl.length()==0){
                            System.out.println("暂无拍卖信息，跳过");
                            continue;
                        }
                        //开始进行处理
                        for(int i=1;i<=totalPage;i++){
                            //对每一页进行处理
                            String pageUrl = basePageUrl.substring(0,basePageUrl.length()-1)+i;
                            Document document2 = Jsoup.connect(pageUrl).timeout(5000).get();
                            //获得该页所有商品信息，该页的所有商品信息是以json的格式存放在<script>标签中的
                            String oriData = document2.getElementById("sf-item-list-data").toString();
                            //接下来对数据进行处理
                            //1.找到“>”标签的位置
                            int start = oriData.indexOf(">");
                            //2.找到"</"标签的位置
                            int end = oriData.indexOf("</");
                            //截取出值
                            String data = oriData.substring(start+1, end);
                            JSONArray data1 = (JSONArray) JsonUtil.convertJsonStrToMap(data).get("data");
                            Iterator<Object> iterator = data1.iterator();
                            while(iterator.hasNext()){
                                JSONObject next = (JSONObject)iterator.next();
                                //需要记录的url
                                String detailUrl = "http:"+next.get("itemUrl");
                                //获取标题
                                Document document3 = Jsoup.connect(detailUrl).timeout(5000).get();
                                String detailTitle = document3.select("div[class=pm-main clearfix]").select("h1").text();
                                //获取变卖价
                                String detailPrice = document3.select("span[class=J_Price]").first().text();
                                System.out.println("抓取信息：");
                                System.out.println(province+"  "+city+"  "+countryName+"  "+detailTitle+"  "+detailPrice+"   "+detailUrl);
                                AuctionItem auctionItem = new AuctionItem();
                                auctionItem.setProvince(province);
                                auctionItem.setCity(city);
                                auctionItem.setCountryName(countryName);
                                auctionItem.setDetailTitle(detailTitle);
                                auctionItem.setDetailPrice(detailPrice);
                                auctionItem.setDetailUrl(detailUrl);
                                instance.insertAuction(auctionItem);
                                System.out.println("存入数据库成功");
                            }
                        }
                    }
                }
            }

        }
        instance.close();
    }
}
