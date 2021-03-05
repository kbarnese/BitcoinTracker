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

public class PrimaryControllerAsync implements Initializable{
    @FXML Pane pane;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     y.setAutoRanging(false);
     chart.setLegendVisible(false);
     chart.getStylesheets().add("com/mycompany/barnesejava2finalproject/fonts.css");

     
     dayButton.fire();
    
    }
        




@FXML Button dayButton;
@FXML Button hourButton;
@FXML Button minuteButton;
@FXML LineChart<String, Float> chart;
@FXML CategoryAxis x;
@FXML NumberAxis y;
@FXML Label currentPrice;


public void eventButtonClick(ActionEvent event){
    
    CompletableFuture<String> cf = new CompletableFuture<>();
    
    
    
    //chart.getData().clear();
    cf.supplyAsync(() ->
    {    Button b = (Button)event.getSource();
         
         return b.getText();})
            .thenApply(param -> { 
                 
                 
                return DataModelAsync.readFromAPI(param);
               }).thenApply((param) -> setAxis(param))
            .thenAccept((param) -> drawChart(param));
    
}

private void drawChart( DataAndClose values){
    Platform.runLater(() ->{
    chart.getData().clear();    
    chart.getData().addAll(values.getXyvalues());
    currentPrice.setText(String.format("$%.0f",values.getClose()));
});
    
}

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


private int rounder(int i ){
    
    float val = (float)i;
    float divider = 1.0f;
    
    while (val/divider > 1){
        divider = divider * 10.0f;
    }
    
    return ((int)(val/divider * 10) * (int)divider/10);
    
    
}

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
