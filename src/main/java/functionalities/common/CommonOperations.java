package functionalities.common;

import java.io.*;
import java.net.Socket;

public interface CommonOperations {
    default void closeEverything(BufferedWriter bufferedWriter, BufferedReader bufferedReader, Socket socket){
        try{
            if(bufferedWriter != null) bufferedWriter.close();
            if(bufferedReader != null) bufferedReader.close();
            if(socket.isConnected()) socket.close();
        } catch (IOException e){
            System.err.println("Closing Error: " + e.getMessage());
        }
    }
}
