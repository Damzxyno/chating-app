package model;

import functionalities.client_functionalities.ClientFunctionalitiesImpl;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Client {
    public static void main(String[] args) {
        ClientFunctionalitiesImpl clientFunctionalities = new ClientFunctionalitiesImpl();
        clientFunctionalities.connectToSocket("localhost", 1234); //24567
        clientFunctionalities.listenForMessage();
        clientFunctionalities.sendMessage();
    }
}
