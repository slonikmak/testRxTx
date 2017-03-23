import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 Servo myservo;
 // the setup function runs once when you press reset or power the board
 void setup() {
 // initialize digital pin LED_BUILTIN as an output.
 pinMode(LED_BUILTIN, OUTPUT);
 myservo.attach(2);
 Serial.begin(9600);
 }

 // the loop function runs over and over again forever
 void loop() {
 String s;


 //myservo.write(90);
 //Serial.println("Succesfully received.");
 //digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
 //myservo.write(100);
 //digitalWrite(LED_BUILTIN, LOW);    // turn the LED off by making the voltage LOW
 //delay(1000);                       // wait for a second
 }
 void serialEvent(){
 //Serial.println("ok");
 String s;
 s = Serial.readString();
 int i = s.toInt();
 Serial.println(i);
 myservo.write(i);
 }

 */
public class Controller implements Initializable{
    Arduino arduino;

    @FXML
    private Slider slider;

    @FXML
    private Label lable;

    public void initialize(URL location, ResourceBundle resources) {
        arduino = new Arduino();
        arduino.initialize();

        lable.textProperty().bindBidirectional(slider.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int val = (int) Math.round((Double) object);
                arduino.writeData(String.valueOf(val));
                return String.valueOf(val);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
    }
}
