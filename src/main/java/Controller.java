import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/*#include <Wire.h>

        #include <Multiservo.h>

        #include <Servo.h>

*//* Simple Serial ECHO script : Written by ScottC 03/07/2012 *//*

*//* Use a variable called byteRead to temporarily store
   the data coming from the computer *//*
        byte byteRead;
        int num,b1,b2,b3,res;
        Servo servo;
        Multiservo servo6;
        Multiservo servo9;
        Multiservo servo10;
        Multiservo servo11;

        void setup() {
// Turn the Serial Protocol ON
        Serial.begin(9600);
        servo6.attach(6);
        servo10.attach(10);
        servo11.attach(11);
        servo9.attach(9);
        }

        void loop() {
        //delay(100);
        //Serial.print("aaa");
   *//*  check if data has been sent from the computer: *//*
        if (Serial.available()>0) {
        int a = Serial.read();
        if (a==97){
        while(Serial.available()<0);
        while (Serial.available() <= 0);
        b1 = (Serial.read()-'0');
        while (Serial.available() <= 0);
        b2 = (Serial.read()-'0');
        while (Serial.available() <= 0);
        b3 = (Serial.read()-'0');
        res = b1*100+b2*10+b3;
        Serial.println(res);
        //Serial.print("e");
        servo6.write(res);
        //Serial.print("s");
        while (Serial.available() <= 0);
        b1 = (Serial.read()-'0');
        while (Serial.available() <= 0);
        b2 = (Serial.read()-'0');
        while (Serial.available() <= 0);
        b3 = (Serial.read()-'0');
        res = b1*100+b2*10+b3;
        servo9.write(res);
        Serial.println(res);
        //Serial.print("e");
        //Serial.print("s");
        while (Serial.available() <= 0);
        b1 = (Serial.read()-'0');
        while (Serial.available() <= 0);
        b2 = (Serial.read()-'0');
        while (Serial.available() <= 0);
        b3 = (Serial.read()-'0');
        res = b1*100+b2*10+b3;
        servo10.write(res);
        Serial.println(res);

        } else return;
        //num = (Serial.read()-'0');
        //Serial.println("s");

        //Serial.print("e");
        //Serial.println(res);
        //switch (num) {
        //case 6:
        //if(res<100) res=100;
        //servo6.write(res);
        // break;
        //case 9:
        //servo9.write(res);
        //break;
        //case 0:
        //servo10.write(res);
        // break;
        //case 1:
        //servo11.write(res);
        //break;
        //}

        }
        }*/
public class Controller {
    Arduino arduino;

    AngelConverter converter;

    @FXML
    private Slider slider6;

    @FXML
    private Label lable6;

    @FXML
    private Slider slider11;

    @FXML
    private Slider slider10;

    @FXML
    private Slider slider9;

    @FXML
    private Label lablel11;

    @FXML
    private Label lablel10;

    @FXML
    private Label lablel9;

    @FXML
    private Slider testSlider;

    @FXML
    private Label testLabel;

    @FXML
    private TextField textField;

    @FXML
    private void sendMessage(){
        arduino.writeData(textField.getText().getBytes());
    }

    public void setArduino(Arduino arduino){
        this.arduino = arduino;
        //arduino.initialize();
    }



    public void init() {
        //arduino = new Arduino();

        lable6.textProperty().bindBidirectional(slider6.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int val = (int) Math.round((Double) object);
                String string;
                if (val<100) string = "0"+val;
                else string = String.valueOf(val);
                arduino.writeData("6".getBytes());
                arduino.writeData(string.getBytes());
                return String.valueOf(val);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        lablel11.textProperty().bindBidirectional(slider11.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int val = (int) Math.round((Double) object);
                String string;
                if (val<100) string = "0"+val;
                else string = String.valueOf(val);
                arduino.writeData("1".getBytes());
                arduino.writeData(string.getBytes());
                return String.valueOf(val);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        lablel10.textProperty().bindBidirectional(slider10.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int val = (int) Math.round((Double) object);
                String string;
                if (val<100) string = "0"+val;
                else if(val<10) string = "00"+val;
                else string = String.valueOf(val);
                arduino.writeData("0".getBytes());
                arduino.writeData(string.getBytes());
                return String.valueOf(val);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        lablel9.textProperty().bindBidirectional(slider9.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int val = (int) Math.round((Double) object);
                String string;
                if (val<100) string = "0"+val;
                else string = String.valueOf(val);
                arduino.writeData("9".getBytes());
                arduino.writeData(string.getBytes());
                return String.valueOf(val);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        testLabel.textProperty().bindBidirectional(testSlider.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int val = (int) Math.round((Double) object);
                String string;
                if (val<100) string = "0"+val;
                //else if(val<10) string = "00"+val;
                else string = String.valueOf(val);
                arduino.writeData("9".getBytes());
                arduino.writeData(string.getBytes());
                return String.valueOf(val);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
    }
}
