import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class HttpProxy extends Thread {
    private final ServerSocket server;

    public HttpProxy(int port) throws IOException {
        server = new ServerSocket(port);
        System.out.println("代理端口：" + port);
    }

    public static void main(String[] args) throws IOException {
        int port = 11111;
        if (args != null && args.length > 0 && args[0].matches("\\d+")) {
            port = Integer.parseInt(args[0]);
        }
        new HttpProxy(port).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = server.accept();
                System.out.println(client);
                //使用线程处理收到的请求
                new HttpConnect(client).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




