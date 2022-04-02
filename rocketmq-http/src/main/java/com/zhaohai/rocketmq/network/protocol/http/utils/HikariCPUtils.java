package com.zhaohai.rocketmq.network.protocol.http.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhaohai.rocketmq.network.protocol.http.entity.MessageData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Slf4j
public class HikariCPUtils {

    private static Properties props;

    private static HikariDataSource hikariDataSource;

    static {
        loadProps();
        initHikari();
    }

    synchronized static private void loadProps(){
        props = new Properties();
        InputStream in = null;
        try {
            in = HikariCPUtils.class.getClassLoader().getResourceAsStream("application.properties");
            props.load(in);
        } catch (Exception e) {
            log.error("HikariCPUtils loadProps error => ", e);
            throw new RuntimeException(e);
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("HikariCPUtils loadProps close error => ", e);
            }
        }
    }
    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }
    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    private static void initHikari() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("spring.datasource.url"));
        config.setUsername(props.getProperty("spring.datasource.username"));
        config.setPassword(props.getProperty("spring.datasource.password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        hikariDataSource = new HikariDataSource(config);
    }

    /**
     * 取得数据库连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection(){
        Connection conn=null;
        try {
            conn= hikariDataSource.getConnection();
        } catch (Exception e) {
            log.error("取得数据库连接时发生异常!"+ e);
            throw new RuntimeException(e);
        }
        return conn;
    }


    /**
     * 查询没有任何条件
     * @param sql
     * @return
     */
    public static List<Map<String, Object>> excuteQuery(String sql) {
        return excuteQuery(sql, null);
    }

    /**
     * 查询时传一个数据参数
     * @param sql
     * @param obj
     * @return
     */
    public static List<Map<String, Object>> excuteQuery(String sql,Object obj) {
        Object[] objs=new Object[1];
        objs[0]=obj;
        return excuteQuery(sql, objs);
    }

    /**
     * 查询时传递多个参数
     * @param sql
     * @param objs
     * @return
     */
    public static List<Map<String, Object>> excuteQuery(String sql,Object[] objs) {
        Connection connection=null;
        PreparedStatement ps = null;
        ResultSet rs=null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        try {
            connection=getConnection();
            ps=connection.prepareStatement(sql);
            if(objs!=null) {
                for(int i=0;i<objs.length;i++) {
                    ps.setObject(i+1, objs[i]);
                }
            }
            rs=ps.executeQuery();

            if(rs !=null){
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                while (rs.next()) {
                    Map<String,Object> rowData = new HashMap<String,Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.put(md.getColumnName(i), rs.getObject(i));
                    }
                    list.add(rowData);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("sql查询出现错误...");
        }finally {
            try {
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * DML操作，增删改操作,传递一个参数
     * @param sql
     * @return
     */
    public static int excuteUpdate(String sql,MessageData messageData) {
        List<MessageData> messageDataList = new ArrayList<>();
        messageDataList.add(messageData);
        return excuteUpdate(sql, messageDataList);
    }

    /**
     * DML操作，增删改操作,传递多个参数
     * @param sql
     * @return
     */
    public static int excuteUpdate(String sql, List<MessageData> messageDataList) {
        int rtn = 0;
        Connection connection=null;
        PreparedStatement ps = null;

        try {
            connection=getConnection();
            for(MessageData messageData : messageDataList) {
                ps=connection.prepareStatement(sql);
                connection.setAutoCommit(false);
                ps.setDate(1, new Date(messageData.getCreateDate().getTime()));
                ps.setDate(2, new Date(messageData.getUpdateDate().getTime()));
                ps.setDate(3, new Date(messageData.getConsumeSiteDate().getTime()));
                ps.setInt(4, messageData.getIsDeleted());
                ps.setString(5, messageData.getTopic());
                ps.setString(6, messageData.getGroupId());
                ps.setString(7, messageData.getKey());
                ps.setString(8, messageData.getTag());
                ps.setString(9, messageData.getMessagId());
                ps.setString(10, messageData.getMessage());
                rtn = ps.executeUpdate();
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
                if(ps!=null){
                    ps.close();
                }
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rtn;
    }

    /**
     * @param sql
     * @param paramsList
     * @return 每条SQL语句影响的行数
     */
    public static int[] executeBatch(String sql, List<Object[]> paramsList) {
        int[] rtn = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();

            // 第一步：使用Connection对象，取消自动提交
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);

            // 第二步：使用PreparedStatement.addBatch()方法加入批量的SQL参数
            if (paramsList != null && paramsList.size() > 0) {
                for (Object[] params : paramsList) {
                    for (int i = 0; i < params.length; i++) {
                        pstmt.setObject(i + 1, params[i]);
                    }
                    pstmt.addBatch();
                }
            }

            // 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
            rtn = pstmt.executeBatch();

            // 最后一步：使用Connection对象，提交批量的SQL语句
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rtn;
    }

    public static void closeHikari() throws SQLException {
        hikariDataSource.getConnection().close();
    }

}
