package com.mycompany.barnesejava2finalproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

//Controller used in program, supports async.
public class PrimaryControllerAsync implements Initializable{
   
@FXML Button dayButton;
@FXML Button hourButton;
@FXML Button minuteButton;
@FXML LineChart<String, Float> chart;
@FXML CategoryAxis x;
@FXML NumberAxis y;
@FXML Label currentPrice;
@Override
    //some chart initilization, fires the day button
    public void initialize(URL url, ResourceBundle rb) {
     y.setAutoRanging(false);
     chart.setLegendVisible(false);
     chart.getStylesheets().add("com/mycompany/barnesejava2finalproject/fonts.css");

     
     dayButton.fire();
    
    }
//method on event button clicks.
//button text is the key to determine type of api data to pull
//async implementation, text is passed to Data model to get data
//the data is then sent to setAxis
public void eventButtonClick(ActionEvent event){
    
    CompletableFuture<String> cf = new CompletableFuture<>();
    
    
    
    //chart.getData().clear();
    cf.supplyAsync(() ->
    {    Button b = (Button)event.getSource();
         
         return b.getText();})
            .thenApply(param -> { 
                return DataModelAsync.readFromAPI(param);})
            .thenApply((param) -> setAxis(param))
            .thenAccept((param) -> drawChart(param));
    
}


//set Axis sets the y-axis of the plot. Uses the highest and lowest values as the bounds
//uses tick marks for about 5 ticks between those bounds. 
//returns a DataAndClose Class that is a containter for the Data
// as a XYChart.Series and the close value of the last recorded digit to be used
// as the currentPrice for the drawChart method
private  DataAndClose setAxis(ArrayList<DataModelAsync> values){
    
    //XYChart.Series<String, Float> xyvalues = new XYChart.Series<String, Float>();
    
    DataAndClose val = new DataAndClose(values);

    
    
    int max = (int)DataModelAsync.getHigh(values);
    int min = (int)DataModelAsync.getLow(values);
    
    int spaceing = (max - min)/5;
    spaceing = this.rounder(spaceing);
    
    max = ((int)max/spaceing+1)*spaceing;
    min = ((int)min/spaceing)*spaceing;
    if (min < 0)
        min = 0;
    
    y.setTickUnit(spaceing);
    y.setUpperBound(max);
    y.setLowerBound(min);
    return val;
    
}

//Draw chart uses .runlater to draw the chart
//sets the value of the currentPrice on the application
private void drawChart( DataAndClose values){
    Platform.runLater(() ->{
    chart.getData().clear();    
    chart.getData().addAll(values.getXyvalues());
    currentPrice.setText(String.format("$%.0f",values.getClose()));
});
    
}

//helper method for setAxis, rounds an integer to a whole numnber
// with only one digit. ie. 57345 -> 50000
private int rounder(int i ){
    
    float val = (float)i;
    float divider = 1.0f;
    
    while (val/divider > 1){
        divider = divider * 10.0f;
    }
    
    return ((int)(val/divider * 10) * (int)divider/10);
    
    
}

//a class to hold a float for last closing value
//and a XYChart.Series<String, Float>
class DataAndClose{
    public XYChart.Series<String, Float> xyvalues;
    private float close;
        public XYChart.Series<String, Float> getXyvalues() {
            return xyvalues;
        }

        public float getClose() {
            return close;
        }
    
    
    public DataAndClose(ArrayList<DataModelAsync> values){
        xyvalues = new XYChart.Series<String, Float>();
        for(DataModelAsync dma : values)
            this.xyvalues.getData().add(new XYChart.Data<String,Float>(dma.getTime(), dma.getOpen()));
        
        this.close = values.get(values.size()-1).getClose();
    }
    
    
    
}   
}
