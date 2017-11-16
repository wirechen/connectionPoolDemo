import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 连接池工具类，返回唯一的一个数据库连接池对象,单例模式 ConnectionPoolUtils.java
 *
 */
public class ConnectionPoolUtils {

    private ConnectionPoolUtils(){};//私有静态方法

    private static ConnectionPool poolInstance = null;



    public static ConnectionPool GetPoolInstance(){

        //读取配置文件
        Properties properties = new Properties();
        try {
            properties.load(ConnectionPoolUtils.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(poolInstance == null) {
            poolInstance = new ConnectionPool(properties.getProperty("datasource.jdbcDriver"),
                                                properties.getProperty("datasource.dbUrl"),
                                                properties.getProperty("datasource.dbUsername"),
                                                properties.getProperty("datasource.dbPassword"));
            try {
                poolInstance.createPool();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return poolInstance;
    }
}