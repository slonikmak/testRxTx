import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Anton on 23.03.2017.
 */
public class MainController implements Initializable{
    Arduino arduino;

    ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    double lastMouseX;
    double lastMouseY;

    AngelConverter converter;
    GraphicsContext gc;
    boolean isCliced = false;
    int count = 0;

    double  y = 50;

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

    @FXML
    void openTestMode() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setArduino(arduino);
        controller.init();
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }



    public void initialize(URL location, ResourceBundle resources) {

        arduino = new Arduino();
        arduino.initialize();

        new Thread(new Consumer(queue, arduino)).start();

        gc = canvas.getGraphicsContext2D();

        converter = new AngelConverter(200, 150, 130);

        canvas.setOnScroll(e->{
            if (e.getDeltaY()>0){
                y=y+1;
            } else y=y-1;

            labelY.setText(String.valueOf(y));
            //converter.convert(lastMouseY, y, lastMouseX);
            writeToArduino(lastMouseX, y, lastMouseX);

        });


        labelA.textProperty().bindBidirectional(converter.angelApropertyProperty(), new MyStringConverter());
        labelB.textProperty().bindBidirectional(converter.angelBpropertyProperty(), new MyStringConverter());
        labelYA.textProperty().bindBidirectional(converter.angelYpropertyProperty(), new MyStringConverter());


        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            //double[] converted = converter.convert(mouseY, y, mouseX);

            writeToArduino(mouseY, y, mouseX);

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
        });


    }



    private void drawCircle(GraphicsContext gc, double x, double y){
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.fillOval(x,y,10,10);
    }

    private void drawLayer(GraphicsContext gc){
        gc.setFill(new Color(1,1,1, 0.1));
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
    }

    private void writeToArduino(double x, double y, double z){
        double[] converted = converter.convert(x, y, z);

        int valA = (int) Math.round(converted[0]);
        int valB = (int) Math.round(converted[1]);
        int valY = (int) Math.round(converted[2]);

        //System.out.println("A "+valA+" B "+valB+" Y "+valY);
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

        String lastCoords;

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
