package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class MySQLDemo {

  // MySQL 8.0 以下版本 - JDBC 驱动名及数据库 URL
  static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";

  // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
  //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&serverTimezone=UTC";


  // 数据库的用户名与密码，需要根据自己的设置
  static final String USER = "wqkant";
  static final String PASS = "wqkant";
  static Random random = new Random();

  private static String getData() {
    final boolean useLetters = true;
    final boolean useNumbers = false;
    String name = RandomStringUtils.random(10, useLetters, useNumbers);
    int age = random.nextInt();
    String birth = RandomStringUtils.random(10, useLetters, useNumbers);
    String base = RandomStringUtils.random(3, useLetters, useNumbers);
    return "('" + name + "', " + age + ", '" + birth + "', '" + base + "')";
  }

  public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;
    try {
      // 注册 JDBC 驱动
      Class.forName(JDBC_DRIVER);

      // 打开链接
      System.out.println("连接数据库...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      // 执行查询
      System.out.println(" 实例化Statement对象...");
      stmt = conn.createStatement();
      String sql;
      for (int i = 0; i < 10000; i++) {
        StringBuilder sb = new StringBuilder();
        sb.append(getData());
        for (int j = 0; j < 10000; j++) {
          sb.append(",");
          sb.append(getData());
        }
        sql = "insert into test.peoples(name, age, birth, base) values ";
        sql = sql + sb.toString();
        System.out.println(sql);
        stmt.executeUpdate(sql);
      }
      stmt.close();
      conn.close();
    } catch (SQLException se) {
      // 处理 JDBC 错误
      se.printStackTrace();
    } catch (Exception e) {
      // 处理 Class.forName 错误
      e.printStackTrace();
    } finally {
      // 关闭资源
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException se2) {
      }// 什么都不做
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    System.out.println("Goodbye!");
  }
}

class Main {

  public static String helper(String s1) {
    // s2 始终存上一个字符
    String s2 = "" + s1.charAt(0);
    int count = 1;
    for (int i = 1; i < s1.length(); i++) {
      int len = s2.length();
      if (s1.charAt(i) == s2.charAt(len - 1)) {
        count++;
      } else if (s1.charAt(i) != s2.charAt(len - 1)) {
        if (count > 1) {
          s2 = s2 + count + s1.charAt(i);
        } else {
          s2 = s2 + s1.charAt(i);
        }
        count = 1;
      }
    }
    if (count > 1) {
      s2 = s2 + count;
    }
    return s2;
  }

  public static void main(String[] args) {
    System.out.println(helper("aaaaaaaaaaabnbbbbbbbbbbbb"));
  }
}