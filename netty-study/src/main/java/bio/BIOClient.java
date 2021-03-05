package bio;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName BIOClient
 * @Description TODO
 * @Author wqkant
 * @Date 2021/3/5 0:02
 * @Version 1.0
 */
public class BIOClient {
  public static void main(String[] args) {
    Socket socket = new Socket();
    try {
      socket.connect(new InetSocketAddress("127.0.0.1", 9999), 100);
      InputStream inputStream =  socket.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      OutputStream outputStream = socket.getOutputStream();
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
      while (true) {
        bw.write("fuck me, ha end");
        bw.flush();
        System.out.println(br.readLine());
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
