package renderer;

import primitives.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.isZero;

public class Camera implements Cloneable{
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double width;
    private double height;
    private double distance;
    private Point viewPlanePC;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private final String RESOURCE = "Renderer resource not set";
    private final String CAMERA_CLASS = "Camera";
    private final String IMAGE_WRITER = "Image writer";
    private final String CAMERA = "Camera";
    private final String RAY_TRACER = "Ray tracer";
    private final String DISTANCE = "camera cant be in distance 0";
    /**
     * turn on - off antialising super sampling
     */
    private boolean isAntialising= false;//indicates of to improve the picture with antialising or not. the default is not.
    /**
     * the number of rays
     */
    private int numOfRaysSuperSampeling = 100;//for sumersampleing

    private boolean adaptiveSuperSamplingFlag = false;
    private int numOfRays = 1;
    private int threads = 1;
    private static final int SPARE_THREADS = 2;
    private boolean print = false;


    public Camera setNumOfRays(int numOfRays) {
        this.numOfRays = Math.max(1, numOfRays);
        return this;
    }

    public Camera setNumOfRaysSuperSampeling(int numOfRaysSuperSampeling) {
        this.numOfRaysSuperSampeling = numOfRaysSuperSampeling;
        return this;
    }

    public Camera setAdaptiveSuperSamplingFlag(boolean adaptiveSuperSamplingFlag) {
        this.adaptiveSuperSamplingFlag = adaptiveSuperSamplingFlag;
        return this;
    }

    public Camera setThreads(int threads) {
        this.threads = threads;
        return this;
    }

    public Camera setPrint(boolean print) {
        this.print = print;
        return this;
    }

    public Camera setnumOfRaysSuperSampeling(int numOfRays) {
        this.numOfRaysSuperSampeling = numOfRays;
        return this;
    }

    public Camera setAntialising(boolean isAntialising) {
        this.isAntialising = isAntialising;
        return this;
    }

    private class Pixel {
        private long maxMunRows;
        private long maxMunCols;
        private long pixels;
        public volatile int row = 0;
        public volatile int col = -1;
        private long counter = 0;
        private int percent = 0;
        private long nextCounter = 0;

        public Pixel(int row, int col) {
            this.maxMunRows = row;
            this.maxMunCols = col;
            this.pixels = maxMunCols * maxMunRows;
            this.nextCounter = pixels / 100;
            if (print) {
                System.out.printf("\r %02d%%", percent);
            }
        }

        public Pixel() {}

        public boolean nextPixel(Pixel p) {
            int percent = nextp(p);
            if (percent > 0 && print) {
                synchronized (System.out) {
                    System.out.printf("\r %02d%%", percent);
                }
            }
            if (percent >= 0) return true;
            if (print) {
                synchronized (System.out) {
                    System.out.printf("\r %02d%%", 100);
                }
            }
            return false;
        }

        private synchronized int nextp(Pixel target) {
            col++;
            counter++;
            if (col < maxMunCols) {
                target.row = this.row;
                target.col = this.col;
                if (counter == nextCounter) {
                    percent++;
                    nextCounter = pixels * (percent + 1) / 100;
                    return percent;
                }
                return 0;
            }
            row++;
            if (row < maxMunRows) {
                col = 0;
                if (counter == nextCounter) {
                    percent++;
                    nextCounter = pixels * (percent + 1) / 100;
                    return percent;
                }
                return 0;
            }
            return -1;
        }
    }


    private Camera(){}


    /**
     * @return the p0
     */
    public Point getP0() {
        return p0;
    }

    /**
     * @return the vTo
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * @return the vUp
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * @return the v
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Static method to obtain a builder for creating a Camera instance.
     *
     * @return A new instance of the Camera.Builder class.
     */
    public static Builder getBuilder() { return new Builder(); }

    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pCenter = p0.add(vTo.scale(distance));

        double Ry = (double) height / nY;
        double Rx = (double) width / nX;

        double yI = -(i - (double) (nY - 1) / 2) * Ry;
        double xJ = (j - (double) (nX - 1) / 2) * Rx;

        Point pIJ = pCenter;
        if (xJ != 0) pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0) pIJ = pIJ.add(vUp.scale(yI));

        Vector vIJ = pIJ.subtract(p0);
        return new Ray(p0, vIJ);
    }

    /**
     * Builder class for constructing Camera instances with specified parameters.
     */
    public static class Builder {

        // Private instance of the Camera class being built
        private final Camera camera = new Camera();

        /**
         * Set the location of the camera.
         *
         * @param location The location (Point) of the camera.
         * @return The Builder instance for method chaining.
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Set the direction vectors for the camera.
         *
         * @param to The direction vector towards which the camera is pointing.
         * @param up The up vector for the camera orientation.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input vectors are not orthogonal.
         */
        public Builder setDirection(Vector to, Vector up) throws IllegalArgumentException {
            if (!isZero(to.dotProduct(up))) throw new IllegalArgumentException("The vectors aren't orthogonal");
            camera.vTo = to.normalize();
            camera.vUp = up.normalize();
            return this;
        }
        /**
         * Set the size of the view plane.
         *
         * @param width The width of the view plane.
         * @param height The height of the view plane.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input dimensions are not valid.
         */
        public Builder setVpSize(double width, double height) throws IllegalArgumentException {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid length or width");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Set the distance from the camera to the view plane.
         *
         * @param distance The distance from the camera to the view plane.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input distance is not valid.
         */
        public Builder setVpDistance(double distance) throws IllegalArgumentException {
            if (distance <= 0) {
                throw new IllegalArgumentException("Invalid distance");
            }
            camera.distance = distance;
            return this;
        }
        /**
         * Build the Camera instance with the specified parameters.
         *
         * @return The constructed Camera instance.
         * @throws MissingResourceException if any required parameter is missing.
         */
        public Camera build() throws MissingResourceException {
            // Check for missing arguments
            String missingArgMsg = "there's a missing argument";
            String className = "Camera";
            if (camera.p0 == null) throw new MissingResourceException(missingArgMsg, className, "p0 - the location of the camera");
            if (camera.vUp == null) throw new MissingResourceException(missingArgMsg, className, "vUp - one of the direction vectors of the camera");
            if (camera.vTo == null) throw new MissingResourceException(missingArgMsg, className, "vTo - one of the direction vectors of the camera");

            // Check if imageWriter and rayTracer are set
            if (camera.imageWriter == null || camera.rayTracer == null) {
                throw new MissingResourceException("ImageWriter or RayTracerBase is missing", className, missingArgMsg);
            }

            // Calculate the right vector and normalize it
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Check for zero values and orthogonality
            if (Util.alignZero(camera.width) == 0) throw new MissingResourceException(missingArgMsg, className, "planeWidth");
            if (Util.alignZero(camera.height) == 0) throw new MissingResourceException(missingArgMsg, className, "planeHeight");
            if (Util.alignZero(camera.distance) == 0) throw new MissingResourceException(missingArgMsg, className, "planeDistance");
            if (!isZero(camera.vRight.dotProduct(camera.vTo))) throw new IllegalArgumentException();

            // Check for valid dimensions and distance
            if (camera.width < 0 || camera.height < 0) {
                throw new IllegalArgumentException("Invalid length or width");
            }
            if (camera.distance < 0) {
                throw new IllegalArgumentException("Invalid distance");
            }

            // Calculate the view plane center point
            camera.viewPlanePC = camera.p0.add(camera.vTo.scale(camera.distance));

            // Attempt to clone the camera instance
            try {
                return (Camera) camera.clone(); // Cloneable – get a full copy
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        /**
         * Sets the image writer of the Camera.
         *
         * @param i the ImageWriter to set
         * @return the Builder object for chaining
         */
        public Builder setImageWriter(ImageWriter i) {
            if (i == null) {
                throw new IllegalArgumentException("ImageWriter cannot be null");
            }
            this.camera.imageWriter = i;
            return this;
        }

        /**
         * Sets the ray tracer of the Camera.
         *
         * @param r the RayTracerBase to set
         * @return the Builder object for chaining
         */
        public Builder setRayTracer(RayTracerBase r) {
            if (r == null) {
                throw new IllegalArgumentException("RayTracerBase cannot be null");
            }
            this.camera.rayTracer = r;
            return this;
        }
    }
    /**
     * Renders the image by iterating through each pixel in the image writer and
     * casting a ray for each pixel, then writing the resulting color to the image
     * writer. Throws a MissingResourceException if either the image writer or the
     * ray tracer base are not set.
     */
    public Camera renderImage() {
        try {
            if (adaptiveSuperSamplingFlag) {
                renderImageAdaptive();
            } else {
                renderImageBeam();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    private void renderImageAdaptive() throws InterruptedException {
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        final Pixel thePixel = new Pixel(nY, nX);
        Thread[] threads = new Thread[this.threads];
        for (int i = 0; i < this.threads; i++) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                while (thePixel.nextPixel(pixel)) {
                    try {
                        primitives.Color color = adaptiveSuperSampling(nX, nY, pixel.col, pixel.row, numOfRaysSuperSampeling);
                        imageWriter.writePixel(pixel.col, pixel.row, new Color(color.getColor()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (Thread thread : threads) thread.start();
        for (Thread thread : threads) thread.join();
        if (print) System.out.print("\r100%\n");
    }

    private primitives.Color adaptiveSuperSampling(int nX, int nY, int j, int i, int numOfRays) throws Exception {
        int numOfRaysInRowCol = (int) Math.floor(Math.sqrt(numOfRays));
        if (numOfRaysInRowCol == 1) {
            return rayTracer.traceRay(constructRay(nX, nY, j, i));
        }

        Point Pc = p0.add(vTo.scale(distance));
        double Ry = height / nY;
        double Rx = width / nX;
        double Yi = (i - (nY / 2d)) * Ry + Ry / 2d;
        double Xj = (j - (nX / 2d)) * Rx + Rx / 2d;
        Point Pij = Pc.add(vRight.scale(-Xj)).add(vUp.scale(-Yi));

        double PRy = Ry / numOfRaysInRowCol;
        double PRx = Rx / numOfRaysInRowCol;
        return adaptiveSuperSamplingRec(Pij, Rx, Ry, PRx, PRy, null);
    }

    private primitives.Color adaptiveSuperSamplingRec(Point centerP, double width, double height, double minWidth, double minHeight, List<Point> prePoints) throws Exception {
        if (width < minWidth * 2 || height < minHeight * 2) {
            return rayTracer.traceRay(new Ray(p0,centerP.subtract(p0)));
        }

        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                Point tempCorner = centerP.add(vRight.scale(i * width / 2)).add(vUp.scale(j * height / 2));
                cornersList.add(tempCorner);
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    Ray tempRay = new Ray(p0,tempCorner.subtract(p0));
                    nextCenterPList.add(centerP.add(vRight.scale(i * width / 4)).add(vUp.scale(j * height / 4)));
                    colorList.add(rayTracer.traceRay(tempRay));
                }
            }
        }

        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }

        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color)) {
                isAllEquals = false;
                break;
            }
        }

        if (isAllEquals && colorList.size() > 1) {
            return tempColor;
        }

        tempColor = primitives.Color.BLACK;
        for (Point center : nextCenterPList) {
            tempColor = tempColor.add(adaptiveSuperSamplingRec(center, width / 2, height / 2, minWidth, minHeight, cornersList));
        }
        return tempColor.reduce(nextCenterPList.size());
    }

    private boolean isInList(List<Point> pointsList, Point point) {
        for (Point tempPoint : pointsList) {
            if (point.isAlmostEquals(tempPoint)) return true;
        }
        return false;
    }

    public Camera setMultithreading(int threads) {
        if (threads < 0) throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");
        if (threads != 0) {
            this.threads = threads;
        } else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            this.threads = cores <= 2 ? 1 : cores;
        }
        return this;
    }

    /**
     * Casts a ray through the given pixel (i,j) on the view plane and returns the
     * color that results from tracing the ray.
     *
     * @param i the x-coordinate of the pixel on the view plane
     * @param j the y-coordinate of the pixel on the view plane
     * @return the color resulting from tracing the ray through the given pixel
     */
    private Color castRay(int j, int i, int nX, int nY) {
        Ray ray = constructRay(nX, nY, j, i);
        return this.rayTracer.traceRay(ray);
    }

    /**
     * Create grid of lines to draw the view plane
     *
     * @param interval - the interval between the lines
     * @param color - the color of the lines
     * @throws MissingResourceException - if the image writer is null
     */
    public Camera printGrid(int interval, Color color) {
        if (imageWriter == null)
            throw new MissingResourceException("ERROR: The image writer is null", "Camera", "imageWriter");

        int ny = imageWriter.getNy();
        int nx = imageWriter.getNx();
        for (int i = 0; i < ny; i++)
            for (int j = 0; j < nx; j++)
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j,i,color);
        return this;
    }
    public Camera writeToImage()
    {
        if (imageWriter == null) {
            throw new MissingResourceException("ImageWriter field cannot be null", Camera.class.getName(), "");
        }
        // delegates the appropriate method of the ImageWriter.
        imageWriter.writeToImage();
        return this;
    }
    public void renderImageBeam()
    {//track rays, if its a beam and more

        if (imageWriter == null)
            throw new MissingResourceException(RESOURCE, CAMERA_CLASS, IMAGE_WRITER);
        if (rayTracer == null)
            throw new MissingResourceException(RESOURCE, CAMERA_CLASS, RAY_TRACER);

        //if(numOfRaysSuperSampeling==0||numOfRaysSuperSampeling==1)//no supersampeling
        //renderImage();//calls for creating the picture

        //else,supersampleing
        //runs on all the image
        for(int i=0;i<imageWriter.getNx();i++)
        {
            for(int j=0;j<imageWriter.getNy();j++)
            {
//                if(numOfRays == 1 || numOfRays == 0)
//                {
//                    Ray ray = constructRay(imageWriter.getNx(), imageWriter.getNy(), j, i);
//                    Color rayColor = rayTracer.traceRay(ray);
//                    imageWriter.writePixel(j, i, rayColor);
//                }
                //creating a list of all the rays in the beam, calculating their color and eriting the image
//                else {
                    List<Ray> rays = constructBeamForEacjPixel(imageWriter.getNx(), imageWriter.getNy(), j, i,numOfRaysSuperSampeling);
                    Color rayColor = rayTracer.traceRay(rays);//new func,gets a list of rays
                    imageWriter.writePixel(j, i, rayColor);
//                }
            }
        }
    }


public List<Ray> constructBeamForEacjPixel(int nX, int nY, int j, int i, int raysAmountSuper)
    {
        if(isZero(distance))
            throw new IllegalArgumentException(DISTANCE);
        int numOfRays = (int)Math.floor(Math.sqrt(raysAmountSuper)); //num of rays in each row or column

        if (numOfRays==1) //if the beam is only one ray
            return List.of(constructRay(nX, nY, j, i));//return a list of only one ray

        double Ry= height/nY;
        double Rx=width/nX;
        double Yi=(i-(nY-1)/2d)*Ry;
        double Xj=(j-(nX-1)/2d)*Rx;

        double PRy = Ry / numOfRays; //height distance between each ray
        double PRx = Rx / numOfRays; //width distance between each ray

        List<Ray> beamRays = new ArrayList<>();

        for (int row = 0; row < numOfRays; ++row) {//runs on the pixel grid
            for (int column = 0; column < numOfRays; ++column) {
                beamRays.add(constructRaysForEachPixel(PRy,PRx,Yi, Xj, row, column));//add the ray that was shot from target area
            }
        }
        beamRays.add(constructRay(nX, nY, j, i));//add the center screen ray
        return beamRays;
    }


    private Ray constructRaysForEachPixel(double Ry,double Rx, double yi, double xj, int j, int i)
    {//creating a ray of beam rays
        Point Pc = p0.add(vTo.scale(distance)); //the center of the screen point

        double yStartingi =  (i *Ry + Ry/2d); //The pixel starting point on the y axis
        double xStartingj=   (j *Rx + Rx/2d); //The pixel starting point on the x axis

        Point Pij = Pc; //The center point at the pixel through which a beam is fired
        //Moving the point through which a beam is fired on the x axis
        if (!isZero(xStartingj + xj))
        {
            Pij = Pij.add(vRight.scale(xStartingj + xj));
        }
        //Moving the point through which a beam is fired on the y axis
        if (!isZero(yStartingi + yi))
        {
            Pij = Pij.add(vUp.scale(-yStartingi -yi ));
        }
        Vector Vij = Pij.subtract(p0);
        return new Ray(p0,Vij);//create the ray throw the point we calculate here
    }
}
