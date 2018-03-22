
package barchart;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

enum ChartType{barChart, pieChart};

/**
 * 
 * @author Rolando Murillo
 * 3597117
 * Assignment 6
 * This program draws a histogram using Screenbuilder
 * 
 */

public class FXMLDocumentController implements Initializable {
    
    // Declare the variables we are going to be using throughtout the class
    final int TOTAL_CHARS = 26;  
    ChartType mChartType;
    int mChars[];
    XYChart.Series series;
    
    
    // Here we are declaring the variables that connect to the SceneBuilder GUI objects
    @FXML
    private Button closeButton;
    
    @FXML
    private Button inputDataButton;
    
    @FXML
    private ComboBox chartComboBox;
    
    @FXML
    private BarChart barChart;
    
    @FXML
    private PieChart pieChart;
    
    
   
    // This method is called when the close button is clicked
    // It will close the app
    @FXML
    private void didClickCloseButton(ActionEvent event){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    private String showInputDialog(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Input your text");

        ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        TextField inputText = new TextField();
        inputText.setPromptText("Input Text");
    
        gridPane.add(inputText, 0, 0);
    
        dialog.getDialogPane().setContent(gridPane);

        Platform.runLater(()->inputText.requestFocus());

        dialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == okButton) {
                return (inputText.getText());
            }
            return (null);
        });
        
        Optional<String> result = dialog.showAndWait();

        return (result.get());
    }
    
    private void paint(){
        
        // Please implement this function
        // This function according to the mChartType
        // refreshes either BarChart or PieChart
        // You need to add each item and its occurance (for instance 'a', 2)
        // to the selected chart for each character in the alpabet
                
        // We need an ObservableList to be able to give the PieChart it's data
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        
        
        // Loop through the mChars array
        for(int i = 0; i < mChars.length; i++) {
            // Convert each i to a letter for the xAxis
            String letter = String.valueOf((char)(i + 97));
            // Use the series variable to add the data for the BarChart
            series.getData().add(new XYChart.Data<>(letter, Integer.valueOf(mChars[i])));
            // Create a Data object representing each letter's count for the PieChart
            PieChart.Data d = new PieChart.Data(letter, Integer.valueOf(mChars[i]));
            // add the data to the PieChart
            pieChartData.add(d);
        }
        
        // Add the data for each
        
        barChart.getData().addAll(series);
        pieChart.setData(pieChartData);
        
    }
    
    @FXML
    private void didClickInputButton(ActionEvent event) {
        
        Arrays.fill(mChars, 0);
            
        String s = showInputDialog();
        
        if ((s!= null) && (s.trim().length() > 0)){
            
            s = s.toLowerCase();
            
            for (int i=0;i<s.length();i++){
                
                Character ch = s.charAt(i);
                
                if (Character.isAlphabetic(ch)){
                    
                    int index = ch - 'a';
                    mChars[index]++;
                }
            }
            
        }
        
        paint();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Please implement this function
        // 1. initialize your mChars array --
        // 2. make mPieChart hidden --
        // 3. set mChartType to barChart --
        // 4. add "BarChart" and "PieChart" to the comboBox -- 
        // 5. set selected item "BarChart" in the combobox --
        // 6. add ChangeListener to ComboBox, and implement changed event --
        // 7. In the changed event update the mChartType according to newValue and --
        // 8. update the visibility of the charts accordingly --
        // 9. call the paint 
        
        // initialize the mChars array
        mChars = new int[26];
        
        // make the mPieChart hidden
        pieChart.setVisible(false);
        
        // set mChartType to barChart
        mChartType = ChartType.barChart;
        
        // Initialize the series variable for the BarChart
        series = new XYChart.Series<>();
        series.setName("Histogram");
        
        // Initialize a ObservableList for the ChartComboBox
        ObservableList<ChartType> list = FXCollections.observableArrayList();
        
        // Add the ChartTypes
        list.add(ChartType.barChart);
        list.add(ChartType.pieChart);
        
        // Set the items in the chartComboBox to the list
        chartComboBox.setItems(list);
        chartComboBox.setValue(ChartType.barChart);
        
        // Add a listener to the chartComboBox to "listen" if the value has changed (i.e the user selecting the other chartType)
        chartComboBox.valueProperty().addListener(
                new ChangeListener<Object>(){
                    @Override
                    public void changed(ObservableValue<? extends Object> ot, Object oldType, Object newType) {
                        Object o = chartComboBox.getValue();
                        mChartType = (ChartType)o;
                        
                        // Set the correct visibility of the charts according to what was selected from the chartComboBox
                        if(mChartType.equals(ChartType.pieChart)) {
                            pieChart.setVisible(true);
                            barChart.setVisible(false);
                        } else {
                            pieChart.setVisible(false);
                            barChart.setVisible(true);
                        }
                        // Redraw/Paint
                        paint();
                    }
                }
        );
    }
    
}
