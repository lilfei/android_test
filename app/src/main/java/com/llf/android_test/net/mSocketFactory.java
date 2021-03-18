package com.llf.android_test.net;

import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class mSocketFactory {

    public static SSLSocketFactory getSocketFactory(Context context) {
        String pwd = "llf_client";
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(context.getAssets().open("client.p12"), pwd.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, pwd.toCharArray());

            InputStream inputStream = context.getAssets().open("client.bks");
            KeyStore trustStore = keyStore.getInstance("BKS");
            trustStore.load(inputStream, pwd.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            SSLContext sc = null;
            if (Build.VERSION.SDK_INT < 16) {
                sc = SSLContext.getInstance("TLS");
            } else {
                sc = SSLContext.getInstance("TLSv1.2");
            }
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            return sc.getSocketFactory();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
