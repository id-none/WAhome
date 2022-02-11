import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.CountDownLatch;

/**
 * 流通道处理线程
 */
public final class HttpExit extends Thread {
    private final CountDownLatch countDownLatch;
    private final DataInputStream in;
    private final DataOutputStream out;

    public HttpExit(DataInputStream in, DataOutputStream out, CountDownLatch countDownLatch) {
        this.in = in;
        this.out = out;
        this.countDownLatch = countDownLatch;

    }

    @Override
    public void run() {
        int len;
        byte[] buf = new byte[10240];
        try {
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, len);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(in, out);
            countDownLatch.countDown();
        }
    }
}