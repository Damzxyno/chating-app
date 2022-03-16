package functionalities.client_functionalities;

import functionalities.common.CommonOperations;

public interface ClientFunctionalities extends CommonOperations {
    void connectToSocket(String ipAddress, int port);
    void listenForMessage();
    void sendMessage();
}

