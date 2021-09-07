package task2_nasa_photo_getter;

import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NasaPhotoGetter {
    private static final String REMOTE_ADDRESS_WO_KEY = "https://api.nasa.gov/planetary/apod?api_key=";
    private static final String MY_KEY = "Tr3U8q9msL9nXZ6LdNs18MzzfUSGesfGUgebb7qr";
    private static final String SEP = File.separator;

    public static void main() {
        final String REMOTE_ADDRESS = REMOTE_ADDRESS_WO_KEY + MY_KEY;
        try (CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setUserAgent("HTTP_homework_program")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        ) {
            HttpGet request = new HttpGet(REMOTE_ADDRESS);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
//                System.out.println(body);
                final String URL_TAG = "\"url\":\"";
                String urlString = body.substring(body.indexOf(URL_TAG) + URL_TAG.length());
                urlString = urlString.substring((0), urlString.indexOf('"'));
                System.out.println(urlString);
                URL url = new URL(urlString);
                String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
                System.out.println(fileName);
                String filePath = "src" + SEP + "main" + SEP + "resources" + SEP + fileName;

                request = new HttpGet(urlString);
                request.setHeader(HttpHeaders.ACCEPT, ContentType.IMAGE_JPEG.getMimeType());
                body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(body);

                try (
                        BufferedInputStream bis = new BufferedInputStream(url.openStream());
                        FileOutputStream fos = new FileOutputStream(filePath, false)
                ) {
                    byte[] buffer = new byte[256];
                    int count = 0;
                    while ((count = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, count);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
