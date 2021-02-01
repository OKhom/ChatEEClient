package ua.kiev.prog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public static String request (String endpoint) throws IOException {
        java.net.URL url = new URL(endpoint);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        String strBuf;
        InputStream is = http.getInputStream();
        try {
            byte[] buf = responseBodyToArray(is);
            strBuf = new String(buf, StandardCharsets.UTF_8);
        } finally {
            is.close();
        }
        return strBuf;
    }

    private static byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;
        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);
        return bos.toByteArray();
    }
}
