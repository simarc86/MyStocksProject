package com.simarc86.mystocks;

/**
 * Created by marctamaritromero on 25/09/14.
 */
public class Stock {

    //nombre, precio, variación neta, variación, volumen, hora

    private String name = "";
    private String price = "0.0";
    private String var_net = "0.0";
    private String var = "0.0";
    private String volume = "0.0";
    private String date = "";

    public Stock(){
    }

    public Stock(String n){
        name = n;
    }

    public Stock(String n, String pr){
        name = n;
        price = pr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVar_net() {
        return var_net;
    }

    public void setVar_net(String var_net) {
        this.var_net = var_net;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
