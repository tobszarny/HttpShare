package pl.biltech.httpshare;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketTests extends Thread {

    public static final int PORT = 8081;

    public static void main(String[] args) throws IOException {
        InetAddress[] allByName = InetAddress.getAllByName("Hall9001");

        Arrays.stream(allByName).forEach(a -> System.out.println(a));

        InetAddress toBind = allByName[2];

        System.out.println("binding " + toBind);

        ServerSocket ss = new ServerSocket(PORT, 1, toBind);

        Socket socket = ss.accept();

        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        sendMessage(out, null);
        readResponse(in);

        out.close();
        in.close();
    }

    private static void sendMessage(BufferedWriter out, File request) throws IOException {
        System.out.println(" * Request");

//        for (String line : getContents(request)) {
//            System.out.println(line);
        for (int i = 0; i < 5; i++) {
            out.write("* Request" + "\r\n");
        }
//        }

        out.write("\r\n");
        out.flush();
    }

    private static void readResponse(BufferedReader in) throws IOException {
        System.out.println("\n * Response");

        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
    }

    private static List<String> getContents(File file) throws IOException {
        List<String> contents = new ArrayList<String>();

        BufferedReader input = new BufferedReader(new FileReader(file));
        String line;
        while ((line = input.readLine()) != null) {
            contents.add(line);
        }
        input.close();

        return contents;
    }
}