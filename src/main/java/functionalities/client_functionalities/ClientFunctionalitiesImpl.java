package functionalities.client_functionalities;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientFunctionalitiesImpl implements ClientFunctionalities {
    private Socket socket;
    private BufferedWriter bufferedWriter = null;
    private BufferedReader bufferedReader = null;
    private Scanner scanner = new Scanner(System.in);

    private volatile boolean isConnected;

    public void disconnect(){
        isConnected = false;
    }

    public void connectToSocket(String ipAddress, int port){
        System.out.println("- - - - Chat set up. - - - -\nWhat is your preferred username?");
        String name = scanner.nextLine();

        System.out.println("Establishing connection with server . . .");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException m){
            m.printStackTrace();
        };
        int amountOfTry = 0;
        while(amountOfTry < 3 && socket == null){
            try {
                amountOfTry++;
                socket = new Socket(ipAddress, port);
                isConnected = true;
            } catch (IOException e) {
                System.out.println("Trying again in 3 seconds. . .");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException m){
                    m.printStackTrace();
                };
                if (amountOfTry == 2) System.err.println("Unable to establish connection with server...");
        }
        }

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connected Successfully!");
        try {
            bufferedWriter.write(name);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(){
        String messageToSend;

        try {
            while(isConnected){
                messageToSend = scanner.nextLine();

                if (messageToSend.equalsIgnoreCase("exit")) {
                    bufferedWriter.write(messageToSend);
                    closeEverything(bufferedWriter, bufferedReader, socket);
                    isConnected = false;
                } else {

                    bufferedWriter.write(messageToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e){
            closeEverything(bufferedWriter, bufferedReader, socket);
            e.printStackTrace();
        }
    }

    public void listenForMessage(){
        new Thread(()-> {
            String messageFromChat;
                while(isConnected) {
                    try {
                        messageFromChat = bufferedReader.readLine();
                        if (messageFromChat == null) {
                            isConnected = false;
                            break;
                        } else System.out.println(messageFromChat);
                    } catch (IOException e) {
                        closeEverything(bufferedWriter, bufferedReader, socket);
                    }
                }
            }).start();
    }
}
