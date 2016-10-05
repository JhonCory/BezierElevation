import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.geom.Line2D;

public class MyPolygon extends Polygon {

    public static final long serialVersionUID = 24362462L;
    public Polygon Elevated;
    public boolean elevated = false;
    private double tempX[] = new double[4];
    private double tempY[] = new double[4];
    private int degCounter; // Stores the number of times the polyline's degree has been elevated

    public MyPolygon() {
	degCounter = 0;
    }

    public MyPolygon(int x[],int y[],int n) {
    }

    /////////////////////////////////////////////////////////////

    // This is a convenience method for ensuring that tempX and tempY
    // grow in an extensible manner.  If newSize is greater than the 
    // current capacity then tempX and tempY are reallocated to double the 
    // until they reach the required capacity.  This reallocation includes 
    // copying all data to the new location.
    private void resizeTemp(int newSize)
    {
        assert(tempX.length == tempY.length);
        while (newSize > tempX.length)
        {
            // Double the capacity of the array
            // Make more space
            double [] copy_x=new double[tempX.length*2];
            double [] copy_y=new double[tempX.length*2];

            // Copy data across
            System.arraycopy(tempX, 0, copy_x, 0, tempX.length);
            System.arraycopy(tempY, 0, copy_y, 0, tempX.length);

            // Swing the references back to the original object
            tempX=copy_x;
            tempY=copy_y;
           
            // System.out.println("Capacity increased to "+tempX.length);
        }
        assert(tempX.length == tempY.length);
    }

    public void elevateOnce() {
      // Input comes in the form of a Java Polygon (points are stored as integers)
      // Output is also in the form of a Java Polygon called Elevated
      // See http://download.oracle.com/javase/1.5.0/docs/api/java/awt/Polygon.html for Polygon details
      // As degree elevation progresses, the working data is kept in
      // double-precision floating point

      int np=npoints;


      // Begin new elevated polygon
      Elevated = new Polygon();
      // Make vector(s) of points which are of size npoints+1 (to allow room for elevation).
      resizeTemp(np+1);
      for (int i=0; i<np; i++) {
        tempX[i] =  xpoints[i];
        tempY[i] =  ypoints[i];
      }

      // TASK 1: Calculate values of np+1 new points (omitting the first one, which doesn't change)
      for (int i=np; i>0; i--) {
        double n = (double) np;
        double j = (double) i;

        double newX[] = new double[np+1];
        double newY[] = new double[np+1];

        newX[i] = (j/n)*((double) tempX[i-1]) + ((n-j)/n)*((double) tempX[i]);
        newY[i] = (j/n)*((double) tempY[i-1]) + ((n-j)/n)*((double) tempY[i]);

        // cast double values to ints, rounding them 
        tempX[i] = (int) newX[i]; 
        tempY[i] = (int) newY[i];
      }

      switch (degCounter) {
        case 0:  System.out.println("1st degree elevation...");
                 break;
        case 1:  System.out.println("2nd degree elevation...");
                 break;
        case 2:  System.out.println("3rd degree elevation...");
                 break;
        default: System.out.println((degCounter+1)+"st degree elevation...");
                 break;
      }

      // Copy all npoints+1 points back into the Elevated polygon
      Elevated.reset();
      for(int i=0;i<=np; i++) {
        Elevated.addPoint((int)(tempX[i]+0.5),
                          (int)(tempY[i]+0.5));
      }

      // TASK 2: Allow for repeated degree elevation by replacing the Polygon's old points with new ones
      reset();
      for (int i=0; i<np+1; i++) {
        addPoint(Elevated.xpoints[i], Elevated.ypoints[i]);
      }

      // Increment the degree elevation counter
      degCounter += 1;
    }
}
