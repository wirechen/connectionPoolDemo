import org.junit.Test;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class TestConnectionPool {

    @Test
    public void connectionPoolTest() {

        try {
            //############################ 使用连接池 ############################

            ConnectionPool connPool = ConnectionPoolUtils.GetPoolInstance();//单例模式创建连接池对象
            // SQL测试语句
            String sql = "Select * from user"; //*********确保该数据库有该表
            // 设定程序运行起始时间
            long start = System.currentTimeMillis();
            // 循环测试100次数据库连接
            for (int i = 0; i < 100; i++) {
                Connection conn = connPool.getConnection(); // 从连接库中获取一个可用的连接
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String name = rs.getString("username"); //*********确保该表有该字段
                    //  System.out.println("查询结果" + name);
                }
                rs.close();
                stmt.close();
                connPool.returnConnection(conn);// 连接使用完后释放连接到连接池
            }
            System.out.println("经过100次的循环调用，使用连接池花费的时间:" + (System.currentTimeMillis() - start) + "ms");
            // connPool.refreshConnections();//刷新数据库连接池中所有连接，即不管连接是否正在运行，都把所有连接都释放并放回到连接池。注意：这个耗时比较大。
            connPool.closeConnectionPool();// 关闭数据库连接池。注意：这个耗时比较大。

            //############################ 不使用连接池 ############################


            //读取配置文件
            Properties properties = new Properties();
            try {
                properties.load(ConnectionPoolUtils.class.getClassLoader().getResourceAsStream("application.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 设定程序运行起始时间
            start = System.currentTimeMillis();
            // 导入驱动
            Class.forName(properties.getProperty("datasource.jdbcDriver"));
            for (int i = 0; i < 100; i++) {
                // 创建连接
                Connection conn = DriverManager.getConnection(properties.getProperty("datasource.dbUrl"),
                                                                properties.getProperty("datasource.dbUsername"),
                                                                properties.getProperty("datasource.dbPassword"));

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                }
                rs.close();
                stmt.close();
                conn.close();// 关闭连接
            }
            System.out.println("经过100次的循环调用，不使用连接池花费的时间:"
                    + (System.currentTimeMillis() - start) + "ms");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
