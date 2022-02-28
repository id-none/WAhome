import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新连接处理线程
 */

public class HttpConnect extends Thread {

    private final Socket client;
    private final long createTime = System.currentTimeMillis();
    byte[] clientInputBuffer = new byte[1024 * 1024 * 4];
    private Socket server = null;
    private String host = null;
    private int port = 80;
    private int clientReadLength = 0;
    private DataInputStream clientInputStream = null; //客户端输入流
    private DataInputStream serverInputStream = null; //服务端输入流
    private DataOutputStream clientOutputStream = null; //客户端输出流
    private DataOutputStream serverOutputStream = null;  //服务端输出流
    private String clientInputString = null;
    static private final ArrayList<String> hosts = new ArrayList<>();

    public HttpConnect(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //hosts.add("127.0.0.1");
            //hosts = HttpProxy.list;
            hosts.add("baidu.com");
            clientInputStream = new DataInputStream(client.getInputStream());
            clientOutputStream = new DataOutputStream(client.getOutputStream());
            if (clientInputStream != null) {
                clientReadLength = clientInputStream.read(clientInputBuffer, 0, clientInputBuffer.length); // 从客户端读数据
                if (clientReadLength > 0) { // 读到数据
                    clientInputString = new String(clientInputBuffer, 0, clientReadLength);
                    // 去掉/n
                    if (clientInputString.contains("\n")) {
                        clientInputString = clientInputString.substring(0, clientInputString.indexOf("\n"));
                    }
                    if (clientInputString.contains("CONNECT ")) {
                        parseServerHost("CONNECT ([^ ]+) HTTP/");
                    } else if (clientInputString.contains("http://") && clientInputString.contains("HTTP/")) {
                        // 从所读数据中取域名和端口号
                        parseServerHost("http://([^/]+)/");
                    }
                    if (host != null && IsOk(host)) {
                        //System.out.println(hosts.contains("baidu.com"));
                        System.out.println(host);
                        server = new Socket(host, port);
                        // 根据读到的域名和端口号建立套接字
                        serverInputStream = new DataInputStream(server.getInputStream());
                        serverOutputStream = new DataOutputStream(server.getOutputStream());
                        if (serverInputStream != null && server != null) {
                            if (clientInputString.contains("CONNECT ")) {
                                doConnect();
                                return;
                            }
                            doRequest();
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IOUtils.close(serverInputStream, serverOutputStream, server, clientInputStream, clientOutputStream, client);
    }

    /**
     * 解析主机地址
     *
     * @param regExp
     */
    private void parseServerHost(String regExp) {
        System.out.println("解析端口号");
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(clientInputString + "/");
        if (matcher.find()) {
            host = matcher.group(1);
            // 判断端口号
            if (host.contains(":")) {
                port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
                host = host.substring(0, host.indexOf(":"));
            }
        }
    }

    /**
     * 处理请求
     *
     * @throws IOException
     */
    private void doRequest() throws IOException, InterruptedException {
        serverOutputStream.write(clientInputBuffer, 0, clientReadLength);
        serverOutputStream.flush();
        final CountDownLatch latch;
        if (clientInputString.contains("POST ")) {
            System.out.println("doPost");
            latch = new CountDownLatch(2);
            new HttpExit(clientInputStream, serverOutputStream, latch).start();
        } else {
            System.out.println("doGet");
            latch = new CountDownLatch(2);
        }
        new HttpExit(serverInputStream, clientOutputStream, latch).start();
        latch.await(120, TimeUnit.SECONDS);
        IOUtils.close(serverInputStream, serverOutputStream, server, clientInputStream, clientOutputStream, client);
        System.out.println("请求地址：" + clientInputString + "，耗时：" + (System.currentTimeMillis() - createTime) + "ms");
    }

    /**
     * 处理连接请求
     *
     * @return
     */
    private void doConnect() throws IOException, InterruptedException {
        System.out.println("doConnect");
        String ack = "HTTP/1.0 200 Connection established\r\n";
        ack = ack + "Proxy-agent: proxy\r\n\r\n";
        clientOutputStream.write(ack.getBytes());
        clientOutputStream.flush();
        final CountDownLatch latch = new CountDownLatch(2);
        new HttpExit(serverInputStream, clientOutputStream, latch).start();
        new HttpExit(clientInputStream, serverOutputStream, latch).start();
        latch.await(120, TimeUnit.SECONDS);
        IOUtils.close(serverInputStream, serverOutputStream, server, clientInputStream, clientOutputStream, client);
    }

    private boolean IsOk(String h) {
        for (String i : hosts) {
            if (h.contains(i)) {
                return false;
            }
        }
        return true;
    }
}

