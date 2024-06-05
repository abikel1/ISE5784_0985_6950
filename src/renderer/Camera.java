package renderer;

import primitives.*;

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
    public Camera renderImage() throws UnsupportedOperationException {
        if (imageWriter == null)
            throw new UnsupportedOperationException("Camera resource not set");

        if (rayTracer == null)
            throw new UnsupportedOperationException("Camera resource not set");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        for (int j = 0; j < nX; j++) {
            for (int i = 0; i < nY; i++) {
                Color color = castRay(j, i, nX, nY);
                this.imageWriter.writePixel(j, i, color);
            }
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
    public Camera writeToImage() {
        if (imageWriter == null) {
            throw new MissingResourceException("ImageWriter field cannot be null", Camera.class.getName(), "");
        }
        // delegates the appropriate method of the ImageWriter.
        imageWriter.writeToImage();
        return this;
    }
}
