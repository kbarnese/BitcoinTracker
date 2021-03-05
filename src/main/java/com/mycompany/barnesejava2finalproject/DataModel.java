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
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.chart.XYChart;





/**
 *
 * @author k_bar
 */
//Model Version Created before Async implemenatation. 
public class DataModel {
   
    private SimpleFloatProperty high;
    private SimpleFloatProperty low;
    private SimpleFloatProperty close;
 
    public float getHigh() {
        return high.getValue();
    }

    public void setHigh(float high) {
        this.high = new SimpleFloatProperty(high);
    }

    public float getLow() {
        return low.getValue();
    }

    public void setLow(float low) {
        this.low = new SimpleFloatProperty(low);
    }

    public float getClose() {
        return close.getValue();
    }

    public void setClose(float close) {
        this.close = new SimpleFloatProperty(close);
    }
    
    public DataModel(){
        this.setHigh(0);
        this.setLow(0);
        this.setClose(0);
    }
    
    
public XYChart.Series<String,Float> readFromAPI(String selection){

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

            return parse(contents, selection);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    
private  XYChart.Series<String, Float> parse(String contents, String selection){
        Gson parser = new Gson();
        JsonObject parsed = parser.fromJson(contents, JsonObject.class).getAsJsonObject("Data");
        
        JsonArray results = parsed.get("Data").getAsJsonArray();

        XYChart.Series result = new XYChart.Series<String, Float>();
        
        float open = results.get(0).getAsJsonObject().getAsJsonPrimitive("open").getAsFloat();
        this.setHigh(open);
        this.setLow(open);
        
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
            
            result.getData().add(new XYChart.Data(time_formatted, open_value));
            if(high_value > this.getHigh())
                this.setHigh(high_value);
            if(low_value < this.getLow())
                this.setLow(low_value);
            this.setClose(close_value);
        }

        
        return result;
    }
    
}
