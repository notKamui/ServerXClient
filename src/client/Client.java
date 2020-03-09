package client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public Client(String inetaddr, int port) {
        try {
            socket = new Socket(inetaddr, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initWriter() {
        try {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initReader() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String req) {
        writer.printf(req + '\0');
    }

    public String recv() {
        try {
            return reader.readLine().replaceAll("\0", "");
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void close() {
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }
}
