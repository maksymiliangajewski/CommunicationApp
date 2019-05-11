package sample;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {

    String nazwa;
    String uczelnia;
    ArrayList<Obszar> obszary;
    SocketChannel sc = null;
    Charset charset = Charset.forName("UTF-8");

    public Client(){
        try {
            this.sc = SocketChannel.open(new InetSocketAddress("localhost", 9001));
            System.out.println("Client connecting to server on port 9001");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(String nazwa, String uczelnia, ArrayList<Obszar> obszary, MainWindowController mainWindowController){
        this.nazwa = nazwa;
        this.uczelnia = uczelnia;
        this.obszary = obszary;

        String obszaryId = "";
        for(int i = 0; i < this.obszary.size(); ++i)
            obszaryId += this.obszary.get(i).id + ",";


        Thread listenToMessages = new Thread(){
            public void run(){
                while(true) {
                    ByteBuffer bb = ByteBuffer.allocate(5000);
                    try {
                        sc.read(bb);
                        bb.flip();
                        String res = charset.decode(bb).toString();
                        System.out.println("read:Otrzymalem wiadomosc " + res);
                        String[] splittedRes = res.split(":");

                        if(splittedRes[0].equals("HELP")) {
                         //   displayHelp(splittedRes);
                            mainWindowController.giveHelpRequest(splittedRes[3], splittedRes[1], Integer.parseInt(splittedRes[2]));
                        }
                        if(splittedRes[0].equals("RESPONSE")) {
                            //displayResponse(splittedRes);
                            mainWindowController.giveResponse(splittedRes[2], splittedRes[1]);
                        }
                        bb.clear();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        };
        listenToMessages.start();
        String wiadomosc ="CONNECT:" +  nazwa + ":" + uczelnia + ":" + obszaryId;
        System.out.println("connect:Polaczenie do servera " + wiadomosc);
        sendMessage(wiadomosc);
    }

    public ArrayList<String> getUczelnie(){
        //to wywoluje GUI
        String wiadomosc = "GET:UCZELNIE";
        sendMessage(wiadomosc);
        String receivedMsg = "";
        try {
            ByteBuffer bb = ByteBuffer.allocate(5000);
            this.sc.read(bb);
            bb.flip();
            receivedMsg = charset.decode(bb).toString();
            System.out.println("getUczelnie:Otrzymalem liste uczelni " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] splitted = receivedMsg.split(",");
        ArrayList<String> res = new ArrayList<>(Arrays.asList(splitted));

        return res;
    }

    public ArrayList<Obszar> getObszary(){
        //to wywoluje GUI
        ArrayList<Obszar> res = new ArrayList<Obszar>();
        String wiadomosc = "GET:OBSZARY";
        sendMessage(wiadomosc);
        String receivedMsg = "";
        try {
            ByteBuffer bb = ByteBuffer.allocate(5000);
            this.sc.read(bb);
            bb.flip();
            receivedMsg = charset.decode(bb).toString();
            System.out.println("getObszary:Otrzymalem liste obszarow " + receivedMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] splitted1 = receivedMsg.split(",");

        for(int i = 0; i < splitted1.length; ++i) {
            System.out.println(splitted1[i]);
            String toSplit = splitted1[i];
            String[] splitted2 = toSplit.split("\\|");
            int id = Integer.parseInt(splitted2[0]);
            Obszar o = new Obszar(id, splitted2[1]);
            res.add(o);
        }
        return res;
    }

    public void getHelp(Obszar o, String opis, String temat, String uczelnia) {
        String wiadomosc = "HELP:";
        if(uczelnia.equals(""))
            uczelnia="dowolnie";
        if(opis.equals("") || opis.equals(null))
            opis = " ";
        wiadomosc += uczelnia + ":" + o.id + ":" + opis;
        System.out.println("getHelp:Wysylam wiadomosc do servera " + wiadomosc);
        sendMessage(wiadomosc);
    }

    public void respond(String nazwa, int id, String wiadomosc) {
        String msg = "RESPONSE:";
        if(wiadomosc.equals("") || wiadomosc.equals(null))
            wiadomosc = " ";
        String idString = "" + id;
        msg += nazwa + ":" + idString + ":" + wiadomosc;
        System.out.println("respond:Wysylam wiadomosc do servera " + msg);
        sendMessage(msg);
    }

    public static String[] displayHelp(String[] message) {
        return message;
    }

    public String[] displayResponse(String[] message) {
        return message;
    }

    public void sendMessage (String message) {
        ByteBuffer bf = this.charset.encode(message);
        try {
            this.sc.write(bf);
        } catch (IOException e1) {
            System.out.println(e1);
        }
        bf.clear();
    }
}
