import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import virtue.constant.ConValues;
import virtue.dto.DbOpFactory;
import virtue.pojo.AuctionItem;
import virtue.pojo.Mortgage;
import virtue.pojo.SearchAttribute;
import virtue.pojo.SearchTarget;
import virtue.util.DataUtil;
import virtue.util.JsonUtil;
import virtue.util.PoiUtils;
import virtue.util.URLUtil;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 查询Excel中的条件数据
 */
public class Main3 {
    public static void main(String[] args) throws Exception {
        DbOpFactory instance = DbOpFactory.getInstance();
        instance.init();
        //得到Excel中的信息
        List<SearchAttribute> allAttributes = PoiUtils.importExcel("file/paimai.xlsx", 0, 1, SearchAttribute.class);
        for(SearchAttribute attribute:allAttributes){
            String fullSearchAddress = URLUtil.getFullSearchAddress(attribute.getAddress());
            Document document1 = Jsoup.connect(fullSearchAddress).timeout(5000).get();
            String basePageUrl = "";
            int totalPage = 1;
            //取出页面底部的页码
            Elements pages = document1.select("div[class=pagination J_Pagination]");
            if(pages.size()==0){
                //该法院暂无拍卖信息
            }else {
                //主要用于获得每页的连接和总页数
                for (Element var3 : pages) {
                    basePageUrl= "http:"+var3.select("button[class=J_PageSkipSubmit]").attr("data-url").trim();
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
                    System.out.println(detailTitle+"  "+detailPrice+"   "+detailUrl);
                    SearchTarget searchTarget = new SearchTarget();
                    searchTarget.setDetailTitle(detailTitle);
                    searchTarget.setDetailPrice(detailPrice);
                    searchTarget.setDetailUrl(detailUrl);
                    instance.insertAuctionTarget(searchTarget);
                }
            }
        }
        instance.close();
    }
}
