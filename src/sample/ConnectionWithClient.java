package sample;

import java.util.List;

public class ConnectionWithClient {

    public String nazwa;
    public String uczelnia;
    public List<Obszar> obszary;
    public int id;

    public ConnectionWithClient(String nazwa, String uczelnia, List<Obszar> obszary, int id){
        this.nazwa = nazwa;
        this.uczelnia = uczelnia;
        this.obszary = obszary;
        this.id = id;
    }
}
