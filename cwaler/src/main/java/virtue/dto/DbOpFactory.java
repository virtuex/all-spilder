package virtue.dto;


import virtue.pojo.AuctionItem;
import virtue.pojo.Mortgage;
import virtue.pojo.SearchTarget;

import java.sql.*;

public class DbOpFactory {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/spider-data";
    private static DbOpFactory instance;
    public static DbOpFactory getInstance(){
        if(instance==null){
            return new DbOpFactory();
        }else{
            return instance;
        }
    }

    private DbOpFactory(){
        init();
    }
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "xda265856";
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    public void init() {
        // 注册 JDBC 驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理数据，并进行插入
     * @param mortgage
     */
    public void insert(Mortgage mortgage){
        try{
            // 执行查询
            //System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = String.format("INSERT INTO TB_MORTGAGE_INFO(MOR_HISRIGHT_NUMBER,MOR_OWNER_NAME,MOR_SECURITY_NUMBER,MOR_AREA,MOR_SERACH_TARGET) VALUES('%s','%s','%s','%s','%s')"
                    ,mortgage.getHisRightNum(),mortgage.getOwnerName(),mortgage.getSecurityNumber(),mortgage.getArea(),mortgage.getSearch_target());
            stmt.executeUpdate(sql);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }

    /**
     * 持久化拍卖信息
     * @param auctionItem
     */
    public void insertAuction(AuctionItem auctionItem){
        try{
            // 执行查询
           // System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = String.format("INSERT INTO TB_AUCTION_ITEM(AUCTION_PROVINCE,AUCTION_CITY,AUCTION_COUNTRY_NAME,AUCTION_DETAIL_TITLE,AUCTION_PRICE,AUCTION_DETAIL_URL) VALUES('%s','%s','%s','%s','%s','%s')"
                    ,auctionItem.getProvince(),auctionItem.getCity(),auctionItem.getCountryName(),auctionItem.getDetailTitle(),auctionItem.getDetailPrice(),auctionItem.getDetailUrl());
            stmt.executeUpdate(sql);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }


    /**
     * 持久化条件查询的拍卖信息
     * TB_AUCTION_TARGET
     */
    public void insertAuctionTarget(SearchTarget searchTarget){
        try{
            // 执行查询
            //System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();
            String sql;
            sql = String.format("INSERT INTO TB_AUCTION_TARGET(AUCTION_DETAIL_TITLE,AUCTION_DETAIL_PRICE,AUCTION_DETAIL_URL) VALUES('%s','%s','%s')"
                    ,searchTarget.getDetailTitle(),searchTarget.getDetailPrice(),searchTarget.getDetailUrl());
            stmt.executeUpdate(sql);
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
    }


    public void close(){
        // 完成后关闭
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
