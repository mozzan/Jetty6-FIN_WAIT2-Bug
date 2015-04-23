package main.java.com.acme.jetty.problem;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class TestClient {
    private TestClient() throws IOException, GeneralSecurityException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { new ClientX509TrustManager() }, null);

        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,
                SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException, URISyntaxException {
    	if(args.length != 2) {
    		return;
    	}
    	new TestClient().run(args[0], Integer.parseInt(args[1]));
    }

    private void run(final String url, final int pinCount) {
        final PrintStream OUT = System.out;
        final int THREAD_COUNT = 10;

        OUT.println("ParallelPing starting...");
        long time = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < pinCount; i++) {
        	final int num = i;

        	threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    OUT.println("#" + num + " Get : " + url);
                	doGet(url);
                }
            });

        }
        threadPool.shutdown();
        long elapsed = System.currentTimeMillis() - time;
        OUT.println("ParallelPing complete... " + elapsed + "ms");
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
