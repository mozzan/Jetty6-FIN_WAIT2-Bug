package main.java.com.acme.jetty.problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class TestClient {

    private final URI uri;

    private AtomicInteger num = new AtomicInteger(0);
    private final SchemeRegistry schemeRegistry;

    private TestClient(URI uri) throws IOException, GeneralSecurityException {
        this.uri = uri;

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { new ClientX509TrustManager() }, null);

        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        Scheme http = new Scheme("http", 80, PlainSocketFactory.getSocketFactory());
        Scheme https = new Scheme("https", 888, sslSocketFactory);

        schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(http);
        schemeRegistry.register(https);
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException, URISyntaxException {
        for (String uri : args) {
            new TestClient(new URI(uri)).run();
        }
    }

    private void run() {
        final int MAX_PING_DATA_SIZE = 0x10000;
        final PrintStream OUT = System.out;
        final int PING_COUNT = 1;
        final int THREAD_COUNT = 10;
        final long SLEEP_SECONDS = 60;
        final Random random = new Random();


        //while (true) {
            OUT.println("ParallelPing starting...");
            //ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);


            long time = System.currentTimeMillis();
            for (int i = 0; i < PING_COUNT; i++) {
            	final int x = i;
            	/*threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        int size = random.nextInt(MAX_PING_DATA_SIZE);
                        StringBuilder data = new StringBuilder(size);
                        for (int index = 0; index < size; index++) {
                            data.append('x');
                        }

                        BasicClientConnectionManager connectionManager = new BasicClientConnectionManager(
                                schemeRegistry);
                        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager);

                        try {
                            ping(httpClient, uri, ContentType.TEXT_PLAIN, data.toString());
                        } catch (IOException e) {
                            OUT.println("Ping failed: " + e);
                        }
                        OUT.println("complete : " + num);
                        num.addAndGet(1);
                        //connectionManager.shutdown();
                    }
                });*/
            	Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int size = random.nextInt(MAX_PING_DATA_SIZE);
                        StringBuilder data = new StringBuilder(size);
                        for (int index = 0; index < size; index++) {
                            data.append('x');
                        }

                        BasicClientConnectionManager connectionManager = new BasicClientConnectionManager(
                                schemeRegistry);
                        DefaultHttpClient httpClient = new DefaultHttpClient(connectionManager);

                        /*try {
                        	OUT.println("Ping 1");
                            ping(httpClient, uri, ContentType.TEXT_PLAIN, data.toString());
                        } catch (IOException e) {
                            OUT.println("Ping failed: " + e);
                        }*/
                        OUT.println("Get : " + x);
                        //ping(httpClient, uri, ContentType.TEXT_PLAIN, data.toString());
                    	doGet("http://192.168.195.51:8080");
                        //connectionManager.shutdown();
                    }
                });
            	thread.start();
            }
            //threadPool.shutdown();
           // while (true) {
                //try {
                    //if (threadPool.awaitTermination(1, TimeUnit.SECONDS)) {
                    //    break;
                    //}
                //} catch (InterruptedException e) {

                //}
            //}
            long elapsed = System.currentTimeMillis() - time;
            OUT.println("ParallelPing x " + PING_COUNT + " @ " + THREAD_COUNT + " threads = " + elapsed
                    + " ms elapsed");
            try {
                OUT.println("ParallelPing sleeping...");
                Thread.sleep(TimeUnit.SECONDS.toMillis(SLEEP_SECONDS));
            } catch (InterruptedException e) {

            }
        //}
    }

    private static void sendHttpUriRequest(HttpClient httpClient, HttpUriRequest request) throws IOException {
        HttpContext context = new BasicHttpContext();
        HttpResponse response = httpClient.execute(request, context);
        /*HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            EntityUtils.consumeQuietly(responseEntity);
        }*/
    }

    public static void ping(HttpClient httpClient, URI uri, ContentType contentType, String content)
            throws IOException {
        HttpGet requestHttpGet = new HttpGet(uri);
        //requestHttpPost.setEntity(new StringEntity(content, contentType));

        sendHttpUriRequest(httpClient, requestHttpGet);
    }

    private static String doGet(String urlAsString) {

		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlAsString);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setRequestProperty("connection", "close");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				/*BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while (null != ((line = br.readLine()))) {
					sb.append(line);
				}*/
				//br.close();
				//return sb.toString();
				try {
		            Thread.sleep(65 * 1000);
		        } catch (InterruptedException e) {

		        }

				return null;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("Exception caught" + e);
			return null;
		} finally {
			//connection.disconnect();
		}

	}

    private static final class ClientX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            /*
             * This method is called by a server to determine whether or not to trust a client certificate. As
             * this method is implemented as part of the client, and it will not be determining whether or not
             * it trusts itself, there is no need to provide an implementation.
             */
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509Certificate certificate : chain) {
                certificate.checkValidity();
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

}
