package functionalities.server_functionalities;

import functionalities.common.CommonOperations;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable, CommonOperations {
    private static final List<ClientHandler> clientHandlerArrayList = new ArrayList<>();
    private final Socket socket;
    private String clientUserName;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientHandlerArrayList.add(this);
            this.clientUserName = bufferedReader.readLine();
            String message = "Server: " + clientUserName + " joined the chat!";
            System.out.println(message);
            broadcastGroupMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            removeClientHandler();
        }
    }


    private void removeClientHandler(){
        clientHandlerArrayList.remove(this);
        System.out.println("Server: " + clientUserName + " left the chat!");
    }

    private void broadcastGroupMessage (String message){
        for (ClientHandler clientHandler : clientHandlerArrayList) {
            if (!clientHandler.clientUserName.equals(clientUserName))
                broadcastIndividualMessage(clientHandler, message);
        }
    }

    private void broadcastIndividualMessage (ClientHandler clientHandler, String message) {
        try {
            clientHandler.bufferedWriter.write(message);
            clientHandler.bufferedWriter.newLine();
            clientHandler.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(bufferedWriter, bufferedReader, socket);
            removeClientHandler();
        }
    }



    @Override
    public void run() {
        String messageFromClient;

        while(true){
            try {
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient.equalsIgnoreCase("exit")) {
                    removeClientHandler();
                    broadcastGroupMessage(clientUserName + " left the chat!");
                    break;
                }
                else broadcastGroupMessage(clientUserName + " :" + messageFromClient);
            }  catch (NullPointerException e){
                removeClientHandler();
                System.out.println(clientUserName + " left abruptly!");
                broadcastGroupMessage(clientUserName + " left the chat");
                break;
            }catch (IOException e){
                closeEverything(bufferedWriter, bufferedReader, socket);
                removeClientHandler();
            }
        }
    }
}
