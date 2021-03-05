/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.barnesejava2finalproject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.chart.XYChart;





/**
 *
 * @author k_bar
 */
public class DataModelAsync {
   
    private  SimpleFloatProperty high;
    private  SimpleFloatProperty low;
    private  SimpleFloatProperty close;
    private  SimpleStringProperty time;
    private SimpleFloatProperty open;
    
    
    
        public String getTime() {
            return time.getValue();
        }

 


    public  void setHigh(float high) {
        this.high = new SimpleFloatProperty(high);
    }


    public  void setLow(float low) {
        this.low = new SimpleFloatProperty(low);
    }

    public  float getClose() {
        return close.getValue();
    }
    
    public float getOpen(){
        return open.getValue();
    }

    public  void setClose(float close) {
        this.close = new SimpleFloatProperty(close);
    }
    

    
    public DataModelAsync(float high, float low, float open, float close, String time){
        this.high = new SimpleFloatProperty(high);
        this.low = new SimpleFloatProperty(low);
        this.open = new SimpleFloatProperty(open);
        this.close = new SimpleFloatProperty(close);
        this.time = new SimpleStringProperty(time);
        
    }
    
    
public static ArrayList<DataModelAsync>  readFromAPI(String selection){

        String contents = "";
        String urlString ="";
        
        
        switch (selection) {
            case "Day":
                urlString = "https://min-api.cryptocompare.com/data/v2/histoday?fsym=BTC&tsym=USD&limit=10";
                break;
            case "Hour":
                urlString = "https://min-api.cryptocompare.com/data/v2/histohour?fsym=BTC&tsym=USD&limit=10";
                break;
            case "Minute":
                urlString = "https://min-api.cryptocompare.com/data/v2/histominute?fsym=BTC&tsym=USD&limit=10";
                break;
            default:
                break;
        }
            
            
        
        try {
            URL address = new URL(urlString);
            InputStreamReader reader = new InputStreamReader(address.openStream());
            BufferedReader buffer = new BufferedReader(reader);

            String line = "";
            while((line = buffer.readLine())!= null){
                contents += line;
            }

           return DataModelAsync.parse(contents, selection);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
private  static ArrayList<DataModelAsync> parse(String contents, String selection){
        Gson parser = new Gson();
        JsonObject parsed = parser.fromJson(contents, JsonObject.class).getAsJsonObject("Data");
        
        JsonArray results = parsed.get("Data").getAsJsonArray();

        ArrayList<DataModelAsync> values = new ArrayList<DataModelAsync>();
        
        

        
        for(JsonElement item:results){
            
            
            JsonObject btcInstance_item = item.getAsJsonObject();
            
            
            long time_value = btcInstance_item.getAsJsonPrimitive("time").getAsLong();
            float open_value = btcInstance_item.getAsJsonPrimitive("open").getAsFloat();
            float close_value = btcInstance_item.getAsJsonPrimitive("close").getAsFloat();
            float high_value = btcInstance_item.getAsJsonPrimitive("high").getAsFloat();
            float low_value = btcInstance_item.getAsJsonPrimitive("low").getAsFloat();
            

            ZonedDateTime zdt = Instant.ofEpochSecond(time_value).atZone(ZoneId.systemDefault());
            String time_formatted;
            if(selection.equals("Day"))
                time_formatted = zdt.format(DateTimeFormatter.ofPattern("MM/dd"));
            else
                time_formatted = zdt.format(DateTimeFormatter.ofPattern("HH:mm"));
            
            values.add(new DataModelAsync(high_value, low_value, open_value, close_value, time_formatted ));
            
        }
        
        return values;


    }

public static float getHigh(ArrayList<DataModelAsync> values){
    float tempHigh = 0;
    for(DataModelAsync dma : values){
        if (dma.high.getValue() > tempHigh) 
            tempHigh = dma.high.getValue();
    }
    
    return tempHigh;
    
}

public static float getLow(ArrayList<DataModelAsync> values){
    float templow = 0;
    for(DataModelAsync dma : values){
        if (dma.low.getValue() < templow || templow == 0) 
            templow = dma.low.getValue();
    }
    
    return templow;
    
}
    
}
