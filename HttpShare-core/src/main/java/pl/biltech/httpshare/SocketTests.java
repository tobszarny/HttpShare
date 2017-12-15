package pl.biltech.httpshare;

import pl.biltech.httpshare.util.NetworkUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketTests extends Thread {

    public static final int PORT = 8081;

    public static void main(String[] args) throws IOException {

        String localHostName = NetworkUtil.getLocalHostName();

        InetAddress[] allByName = InetAddress.getAllByName(localHostName);

        NetworkUtil.printInetAdressesByHostName(allByName);

        for (int i = 0; i < allByName.length; i++) {
            System.out.println(String.format("[%d] %s", i + 1, allByName[i]));
        }

        int readChoice = readChoice(1, allByName.length + 1);

        InetAddress toBind = allByName[readChoice - 1];

        System.out.println("binding to " + toBind);
        System.out.println("http://" + localHostName + ":" + PORT);

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

    private static int readChoice(int from, int to) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;
        boolean incorrect = true;
        do {
            System.out.print("Enter choice: ");
            String s = br.readLine();
            choice = Integer.parseInt(s);
            System.out.println(choice);
            incorrect = !(choice >= from && choice <= to);
            if (incorrect) {
                System.out.println(String.format("Choice has to be between %d and %d, try again", from, to));
            }
        } while (incorrect);

        return choice;
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