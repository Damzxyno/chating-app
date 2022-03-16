package model;

import functionalities.server_functionalities.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.*;

public class Server {
    private ServerSocket serverSocket;

    public Server (int port){
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void startServer(){
        out.println("Server started... \nOpen to clients!");
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                out.println("New Client Connected!");
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
                err.println("Server Error: " + e.getMessage());
        }
    }

    public void closeServer(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            out.println("Server down . . .");
        }));

        Server server = new Server(1234);
        server.startServer();
    }
}
