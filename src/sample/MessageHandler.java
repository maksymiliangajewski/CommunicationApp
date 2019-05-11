package sample;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    Server server;
    public MessageHandler(Server server) {
        this.server = server;
    }

    public void get(String message, SelectionKey key) {
        System.out.println("Handling GET request");
        if(message.split(":")[1].equals("UCZELNIE")){
            String response = "";
            for(int i = 0; i<server.uczelnie.size(); i++) {
                response += server.uczelnie.get(i);
                if(i<server.uczelnie.size() - 1){
                    response += ",";
                }
            }
            ByteBuffer bb = server.charset.encode(response);
            try {
                ((SocketChannel)key.channel()).write(bb);
            } catch (IOException e) {
                key.cancel();
            }
        }
        if(message.split(":")[1].equals("OBSZARY")){
            String response = "";
            for(int i = 0; i<server.obszary.size(); i++) {
                response += server.obszary.get(i).id + "|" + server.obszary.get(i).name;
                if(i<server.obszary.size() - 1){
                    response += ",";
                }
            }
            ByteBuffer bb = server.charset.encode(response);
            try {
                ((SocketChannel)key.channel()).write(bb);
            } catch (IOException e) {
                key.cancel();
            }
        }
        System.out.println("GET request handled");
    }

    public void connect(String message, SelectionKey key) {
        System.out.println("Handlig CONNECT request");
        String[] splittedMessage = message.split(":");
        List<Obszar> obszary = new ArrayList<>();
        for(String obszarId : splittedMessage[3].split(",")){
            for(Obszar obszar : server.obszary) {
                if(obszar.id == Integer.parseInt(obszarId)) {
                    obszary.add(obszar);
                }
            }
        }
        key.attach(new ConnectionWithClient(splittedMessage[1],splittedMessage[2],obszary,server.currentID));
        server.currentID += 1;
        System.out.println("CONNECT request handled");
    }

    public void help(String message, SelectionKey key) {
        System.out.println("Handling HELP request");
        if(key.attachment()!=null){
            String[] splittedMessage = message.split(":");
            String response = "";
            response += "HELP" + ":" + ((ConnectionWithClient)key.attachment()).nazwa + ":" + ((ConnectionWithClient)key.attachment()).id + ":" + splittedMessage[3];

            List<Obszar> obszary = new ArrayList<>();
            if(!splittedMessage[2].equals("dowolnie")) {
                for (String obszarId : splittedMessage[2].split(",")) {
                    for (Obszar obszar : server.obszary) {
                        if (obszar.id == Integer.parseInt(obszarId)) {
                            obszary.add(obszar);
                        }
                    }
                }
            }
            for(SelectionKey clientKey : server.selector.keys()){
                if(!key.equals(clientKey) && clientKey.attachment()!=null && !clientKey.channel().equals(server.serverSocketChannel)){
                    boolean send = true;
                    for(Obszar obszarKlienta : obszary){
                        if(!((ConnectionWithClient)key.attachment()).obszary.contains(obszarKlienta)){
                            send = false;
                        }
                    }
                    if(!((ConnectionWithClient)key.attachment()).uczelnia.equals("dowolnie")){
                        if(!((ConnectionWithClient)key.attachment()).uczelnia.equals(splittedMessage[1])){
                            send = false;
                        }
                    }
                    if(send) {
                        ByteBuffer bb = server.charset.encode(response);
                        try {
                            ((SocketChannel)clientKey.channel()).write(bb);
                        } catch (IOException e) {
                            key.cancel();
                        }
                    }
                }
            }
        }
        System.out.println("HELP request handled");
    }

    public void response(String message, SelectionKey key) {
        System.out.println("handling RESRPONSE request");
        String[] splittedMessage  = message.split(":");
        String response = splittedMessage[0] + ":" + splittedMessage[1] + ":" + splittedMessage[3];
        ByteBuffer bb = server.charset.encode(response);
        try {
            for(SelectionKey rightKey : server.selector.keys()) {
                if(rightKey.attachment()!=null && !rightKey.channel().equals(server.serverSocketChannel) && ((ConnectionWithClient)rightKey.attachment()).id == Integer.parseInt(splittedMessage[2])) {
                    ((SocketChannel) rightKey.channel()).write(bb);
                }
            }
        } catch (IOException e) {
            key.cancel();
        }
        System.out.println("RESPONSE request handled");
    }
}
