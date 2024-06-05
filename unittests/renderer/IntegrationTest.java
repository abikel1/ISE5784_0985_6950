package renderer;

import static org.junit.jupiter.api.Assertions.*;

import geometries.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import scene.Scene;

import java.util.LinkedList;
import java.util.List;

/**
 * This class contains JUnit tests for the ray construction and intersection methods.
 */
public class IntegrationTest
{

    // Camera builder used for all tests with a default setup
    private final Scene scene= new Scene("Test");
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setImageWriter(new ImageWriter("output",100,100))
            .setRayTracer(new SimpleRayTracer(scene))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVpSize(3,3)
            .setVpDistance(1);
    private Camera camera = cameraBuilder.setVpSize(3, 3).build();

    private int clacIntersecs(Geometry geometry)
    {
        int intersections = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                List<Point> findIntersection = null;
                findIntersection = geometry.findIntersections(camera.constructRay(3, 3, j , i));
                if(findIntersection != null) intersections += findIntersection.size();
            }
        }
        return intersections;
    }

    /**
     * Tests ray intersections with spheres.
     */
    @Test
    void testSphere() {
        // Setting up the camera for sphere tests
        cameraBuilder.setLocation(Point.ZERO);
        camera = cameraBuilder.setVpSize(3, 3).build();

        // TC01 : 2 intersections
        Sphere sphere1 = new Sphere(new Point(0,0,-3),1);
        assertEquals(2,clacIntersecs(sphere1),"WRONG number of intersections");

        // TC02 : 9 intersections
        Sphere sphere4 = new Sphere(new Point(0,0,-1.5),4);
        assertEquals(9,clacIntersecs(sphere4),"WRONG number of intersections");

        // TC03 : 0 intersections
        Sphere sphere5 = new Sphere( new Point(0,0,1),0.5);
        assertEquals(0,clacIntersecs(sphere5),"WRONG number of intersections");

        // Change the position of the camera
        cameraBuilder.setLocation(new Point(0,0,0.5));
        camera = cameraBuilder.setVpSize(3, 3).build();

        // TC04 : 18 intersections
        Sphere sphere2 = new Sphere(new Point(0,0,-2.5),2.5);
        assertEquals(18,clacIntersecs(sphere2),"WRONG number of intersections");

        // TC05 : 10 intersections
        Sphere sphere3 = new Sphere(new Point(0,0,-2),2);
        assertEquals(10,clacIntersecs(sphere3),"WRONG number of intersections");

    }

    /**
     * Tests ray intersections with planes.
     */
    @Test
    void testPlane() {
        // Setting up the camera for plane tests
        cameraBuilder.setLocation(Point.ZERO);
        camera = cameraBuilder.setVpSize(3, 3).build();

        // TC01 : 9 intersections
        Plane plane1 = new Plane(new Point(0,0,-2.5),new Vector(0,0,1));
        assertEquals(9,clacIntersecs(plane1),"WRONG number of intersections");

        // TC02 : 9 intersections
        Plane plane2 = new Plane(new Point(0,0,-2),new Vector(0,-1,3));
        assertEquals(9,clacIntersecs(plane2),"WRONG number of intersections");

        // TC03 : 6 intersections
        Plane plane3 = new Plane(new Point(0,0,-2.5),new Vector(0,-1,1));
        assertEquals(6,clacIntersecs(plane3),"WRONG number of intersections");
    }

    /**
     * Tests ray intersections with triangles.
     */
    @Test
    void testTriangle() {
        // Setting up the camera for sphere tests
        cameraBuilder.setLocation(Point.ZERO);
        camera = cameraBuilder.setVpSize(3, 3).build();

        // TC01 : 1 intersection
        Triangle triangle1 = new Triangle(new Point(0,1,-2),new Point(-1,-1,-2),new Point(1,-1,-2));
        assertEquals(1,clacIntersecs(triangle1),"WRONG number of intersections");

        // TC02 : 2 intersections
        Triangle triangle2 = new Triangle(new Point(0,20,-2),new Point(-1,-1,-2),new Point(1,-1,-2));
        assertEquals(2,clacIntersecs(triangle2),"WRONG number of intersections");
    }
}

