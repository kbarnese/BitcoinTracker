package com.mycompany.barnesejava2finalproject;

import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.layout.StackPane;

//controller Class without Async
public class PrimaryController implements Initializable{
    @FXML Pane pane;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     y.setAutoRanging(false);
     chart.setLegendVisible(false);
     
   
     
     chart.getStylesheets().add("com/mycompany/barnesejava2finalproject/fonts.css");

     
     dayButton.fire();
    
    }
        
    DataModel dm = new DataModel();

XYChart.Series<String, Float> data;

@FXML Button dayButton;
@FXML Button hourButton;
@FXML Button minuteButton;
@FXML LineChart<String, Float> chart;
@FXML CategoryAxis x;
@FXML NumberAxis y;
@FXML Label currentPrice;

public void eventButtonClick(ActionEvent event){
    Button b = (Button)event.getSource();
   
    data = dm.readFromAPI(b.getText());
    
    
    chart.getData().clear();
    
    
    
    this.SetAxis();
    
    this.currentPrice.setText(String.format("$%,.0f", dm.getClose()));
    

    chart.getData().add(data);

    event.consume();
}

private void SetAxis(){
    int max = (int)dm.getHigh();
    int min = (int)dm.getLow();
    
    int spaceing = (max - min)/5;
    spaceing = this.rounder(spaceing);
    
    max = ((int)max/spaceing+1)*spaceing;
    min = ((int)min/spaceing)*spaceing;
    if (min < 0)
        min = 0;
    y.setTickUnit(spaceing);
    y.setUpperBound(max);
    y.setLowerBound(min);
    
    
}


private int rounder(int i ){
    
    float val = (float)i;
    float divider = 1.0f;
    
    while (val/divider > 1){
        divider = divider * 10.0f;
    }
    
    return ((int)(val/divider * 10) * (int)divider/10);
    
    
}

    
    
}
