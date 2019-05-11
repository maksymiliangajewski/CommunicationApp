package sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.sql.*;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public Charset charset;
    public ArrayList<Obszar> obszary;
    public ArrayList<String> uczelnie;
    ServerSocketChannel serverSocketChannel;
    Selector selector;
    boolean stop;
    int currentID;

    public static void main(String[] args){
        new Server();
    }

    public Server() {
        System.out.println("Server set up start");
        currentID = 0;
        charset = Charset.forName("UTF8");
        uczelnie = new ArrayList<String>();
        uczelnie.add("PJATK");
        uczelnie.add("UW");
        uczelnie.add("Politechnika");
        obszary = new ArrayList<Obszar>();
        obszary.add(new Obszar(1, "Matematyka"));
        obszary.add(new Obszar(2, "Informatyka"));
        obszary.add(new Obszar(3, "Fizyka"));
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 9001));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.err.println("Server could not be set up");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server set up finished");
        try {
            System.out.println("Server start");
            start();
        }catch(IOException e){
            System.err.println("Unpredicted error. Server shutdown.");
            e.printStackTrace();
            System.exit(1);
        }
    }
    private void start() throws IOException {
        stop = false;
        new Thread(()->{
            java.util.Scanner in = new java.util.Scanner(System.in);
             while(!stop){
                 if(in.nextLine().equals("stop")){
                     stop = true;
                     try {
                         SocketChannel.open().connect(new InetSocketAddress("localhost",9001));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }
        }).start();
        System.out.println("Set up MessageHandler");
        MessageHandler handler = new MessageHandler(this);
        System.out.println("Start selector loop");
        while(!stop){
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    System.out.println("Accepting new client");
                    SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("Client accepted");
                }
                if(key.isReadable()){
                    System.out.println("Reciving message");
                    ByteBuffer bb =  ByteBuffer.allocate(5000);
                    ((SocketChannel)key.channel()).read(bb);
                    bb.flip();
                    String message = charset.decode(bb).toString();
                    System.out.println("Message recieved: " + message);
                    System.out.println("Handling message" );
                    switch(message.split(":")[0]) {
                        case "GET":
                            handler.get(message, key);
                            break;
                        case "HELP":
                            handler.help(message, key);
                            break;
                        case "RESPONSE":
                            handler.response(message, key);
                            break;
                        case "CONNECT":
                            handler.connect(message,key);
                            break;
                    }
                }
            }
            iterator.remove();
        }
        selector.close();
    }
}