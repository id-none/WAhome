import java.io.Closeable;
import java.io.IOException;


public class IOUtils {

    private IOUtils() {
    }

    public static void close(Closeable... closeables) {
        isNull(closeables);
    }

    public static void isNull(Closeable[] closeables) {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}