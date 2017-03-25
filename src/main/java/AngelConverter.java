import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by User on 24.03.2017.
 */
public class AngelConverter {
    double l1;
    double l2;

    double deltaX;

    DoubleProperty angelAproperty = new SimpleDoubleProperty(90);
    DoubleProperty angelBproperty = new SimpleDoubleProperty(90);
    DoubleProperty angelYproperty = new SimpleDoubleProperty(90);

    public AngelConverter(double l1, double l2, double deltaX){
        this.l1=l1;
        this.l2=l2;
        this.deltaX = deltaX;
    }


    public double[] convert(double x, double y, double z){
        if(x<0) x = 0;
        if(x>600) x =600;
        if(z<0) z=0;
        if(z>1600) z = 1600;

        double[] converted = convertDimentions(x, z);

        x = converted[0];
        z = converted[1];



        double[] result = new double[3];

        double angelY = Math.atan(z/x);

        x = Math.sqrt(Math.pow(z,2)+Math.pow(x,2));

        double angelB = Math.acos((Math.pow(l1,2)+Math.pow(l2,2)-Math.pow(x,2)-Math.pow(y,2))/(2*l1*l2));
        double angelA1 = Math.acos((Math.pow(l1,2)+(Math.pow(x,2)+Math.pow(y,2))-Math.pow(l2,2))
                /(2*l1*Math.sqrt(Math.pow(x,2)+Math.pow(y,2))));
        double angelA2 = Math.acos(x/(Math.sqrt(Math.pow(x,2)+Math.pow(y,2))));

        double angelA = angelA1+angelA2;

        angelA = 180-Math.toDegrees(angelA);
        angelB = Math.toDegrees(angelB);
        angelY = -Math.toDegrees(angelY)+100;

        angelAproperty.setValue(angelA);
        angelBproperty.setValue(angelB);
        angelYproperty.setValue(angelY);



        result[0] = angelA;
        result[1] = angelB;
        result[2] = angelY;

        return result;
    }

    public double[] convertDimentions(double x, double z){
        double[] result = new double[2];

        x = (600-x)*0.3+deltaX;
        z = -(800-z)*0.3;

        result[0] = x;
        result[1] = z;

        return result;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = this.deltaX+deltaX;
    }

    public double getAngelAproperty() {
        return angelAproperty.get();
    }

    public DoubleProperty angelApropertyProperty() {
        return angelAproperty;
    }

    public void setAngelAproperty(double angelAproperty) {
        this.angelAproperty.set(angelAproperty);
    }

    public double getAngelBproperty() {
        return angelBproperty.get();
    }

    public DoubleProperty angelBpropertyProperty() {
        return angelBproperty;
    }

    public void setAngelBproperty(double angelBproperty) {
        this.angelBproperty.set(angelBproperty);
    }

    public double getAngelYproperty() {
        return angelYproperty.get();
    }

    public DoubleProperty angelYpropertyProperty() {
        return angelYproperty;
    }

    public void setAngelYproperty(double angelYproperty) {
        this.angelYproperty.set(angelYproperty);
    }
}
