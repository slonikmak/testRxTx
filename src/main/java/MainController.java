import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Anton on 23.03.2017.
 */
public class MainController implements Initializable{
    Arduino arduino;

    ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();


    AngelConverter converter;
    GraphicsContext gc;
    boolean isCliced = false;
    int count = 0;

    double  y = 100;

    @FXML
    Canvas canvas;

    @FXML
    private Label labelX;

    @FXML
    private Label labelYA;

    @FXML
    private Label labelZ;

    @FXML
    private Label labelA;

    @FXML
    private Label labelB;

    @FXML
    private Label labelY;



    public void initialize(URL location, ResourceBundle resources) {



        arduino = new Arduino();
        arduino.initialize();

        new Thread(new Consumer(queue, arduino)).start();

        gc = canvas.getGraphicsContext2D();

        converter = new AngelConverter(200, 150, 130);


        labelA.textProperty().bindBidirectional(converter.angelApropertyProperty(), new MyStringConverter());
        labelB.textProperty().bindBidirectional(converter.angelBpropertyProperty(), new MyStringConverter());
        labelYA.textProperty().bindBidirectional(converter.angelYpropertyProperty(), new MyStringConverter());


        converter.angelAproperty.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {


                int valA = (int) Math.round((Double) newValue);
                int valB = (int) Math.round(converter.angelBpropertyProperty().get());
                int valY = (int) Math.round(converter.angelYpropertyProperty().get());

                System.out.println("A "+valA+" B "+valB+" Y "+valY);
                String stringA, stringB, stringY;
                if (valA<100) stringA = "0"+valA;
                else stringA = String.valueOf(valA);
                if (valB<100) stringB = "0"+valB;
                else stringB = String.valueOf(valB);
                if (valY<100) stringY = "0"+valY;
                else stringY = String.valueOf(valY);

                StringBuilder builder = new StringBuilder();
                builder.append("a");
                //builder.append("6");
                builder.append(stringA);
                //builder.append("9");
                builder.append(stringB);
                //builder.append("0");
                builder.append(stringY);



                //System.out.println(builder);
                //arduino.writeData("6".getBytes());
                queue.add(builder.toString());
                //arduino.writeData(builder.toString().getBytes());
            }
        });
        /*converter.angelBproperty.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int val = (int) Math.round((Double) newValue);
                String string;
                //if (val<100&&val>10) string = "0"+val;
                if (val<100) string = "0"+val;
                else string = String.valueOf(val);
                System.out.println(string);
                arduino.writeData("9".getBytes());
                arduino.writeData(string.getBytes());
            }
        });
        converter.angelYproperty.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int val = (int) Math.round((Double) newValue);
                String string;
                if (val<100) string = "0"+val;
                else string = String.valueOf(val);
                arduino.writeData("0".getBytes());
                arduino.writeData(string.getBytes());
            }
        });*/



        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                double mouseX = event.getX();
                double mouseY = event.getY();

                double[] converted = converter.convert(mouseY, y, mouseX);

                double[] convertedDim = converter.convertDimentions(mouseY, mouseX);

                labelX.setText(String.valueOf(Math.round(convertedDim[0])));
                labelY.setText(String.valueOf(y));
                labelZ.setText(String.valueOf(Math.round(convertedDim[1])));

                drawCircle(gc, mouseX, mouseY);
                count++;
                if (count==50) {
                    drawLayer(gc);
                    count = 0;
                }
            }
        });


    }



    void drawCircle(GraphicsContext gc, double x, double y){
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.fillOval(x,y,10,10);
    }

    private void drawLayer(GraphicsContext gc){
        gc.setFill(new Color(1,1,1, 0.1));
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    }

    static class MyStringConverter extends StringConverter<Number>{

        public String toString(java.lang.Number object) {
            int val = (int) Math.round((Double) object);
            return String.valueOf(val);
        }

        public java.lang.Number fromString(String string) {
            return null;
        }
    }

    static class Consumer implements Runnable{
        ConcurrentLinkedQueue<String> queue;
        Arduino arduino;

        Consumer(ConcurrentLinkedQueue<String> queue, Arduino arduino){
            this.queue = queue;
            this.arduino = arduino;
        }

        public void run() {
            while (true){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (queue.size()==0) continue;
                arduino.writeData(queue.poll().getBytes());
            }
        }
    }


}
