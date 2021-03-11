package com.zzx.okhttptest.net.ssl;

import android.content.Context;

import com.zzx.okhttptest.util.Logg;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLSocketTest1 {

    private final static String CLIENT_PRI_KEY = "client.bks";
    private final static String TRUSTSTORE_PUB_KEY = "client.bks";
    private final static String CLIENT_BKS_PASSWORD = "llf_client";
    private final static String TRUSTSTORE_BKS_PASSWORD = "llf_client";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLSv1.2";
    private final static String CERTIFICATE_FORMAT = "X509";

    public static SSLSocketFactory getSSLCertifcation(Context context) {
        SSLSocketFactory sslSocketFactory = null;

        KeyStore keyStore = null;// 客户端信任的服务器端证书
        try {
            // step 1 获取ca
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            final Certificate ca;
            InputStream caInput = new BufferedInputStream(context.getAssets().open("client.cer"));
            ca = cf.generateCertificate(caInput);
            Logg.d("ca=" + ((X509Certificate) ca).getSubjectDN());
            Logg.d("key=" + ((X509Certificate) ca).getPublicKey());
            Logg.d("key=" + Arrays.toString(((X509Certificate) ca).getSigAlgParams()));
            caInput.close();

            // step 2 获取keyStore
            keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // step 3 获取trustStore
            KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);//读取证书
            trustStore.load(null, null);
            trustStore.setCertificateEntry("ca", ca);

            // 初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_FORMAT);

            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            final X509TrustManager origTrustmanager = (X509TrustManager) trustManagers[0];

            TrustManager[] wrappedTrustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return origTrustmanager.getAcceptedIssuers();
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustmanager.checkClientTrusted(certs, authType);
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                            origTrustmanager.checkServerTrusted(certs, authType);
                        }
                    }
            };

            // 设置使用自定义证书
            sslContext.init(keyManagerFactory.getKeyManagers(), wrappedTrustManagers, null);

            // 设置信任所有证书（双向加密没有了意思）
//            sslContext.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            Logg.e("finally");
        }

        return sslSocketFactory;
    }

}
