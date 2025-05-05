package Utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDb {
    private Connection conn;
    private String url="jdbc:mysql://localhost:3306/projet";
    private String user="root";
    private String pwd="";
private static MyDb instance;
public static MyDb getInstance(){
    if(instance==null){
        instance=new MyDb();
    }
    return instance;
}
public Connection getConn(){
    return conn;
}
    private MyDb() {
        try {
        this.conn = DriverManager.getConnection(url , user, pwd);
        System.out.println("Connected to MyDb");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
