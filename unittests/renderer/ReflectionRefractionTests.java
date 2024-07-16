package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;

public class ReflectionRefractionTests {
    /** Scene for the tests */
    private final Scene scene         = new Scene("Test scene");
    /** Camera builder for the tests with triangles */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0,0,-1),new Vector(0,1,0))
            .setRayTracer(new SimpleRayTracer(scene));

    /** Produce a picture of a sphere lighted by a spot light */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(new Point(0, 0, -50), 50d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setkT(0.3)),
                new Sphere(new Point(0, 0, -50), 25d).setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(150, 150)
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of a sphere lighted by a spot light */
    @Test
    public void twoSpheresOnMirrors() {
        scene.geometries.add(
                new Sphere(new Point(-950, -900, -1000), 400d).setEmission(new Color(0, 50, 100))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                .setkT(new Double3(0.5, 0, 0))),
                new Sphere(new Point(-950, -900, -1000), 200d).setEmission(new Color(100, 50, 20))
                        .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(670, 670, 3000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setkR(1)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setkR(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
                .setKl(0.00001).setKq(0.000005));

        cameraBuilder.setLocation(new Point(0, 0, 10000)).setVpDistance(10000)
                .setVpSize(2500, 2500)
                .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /** Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow */
    @Test
    public void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)),
                new Sphere(new Point(60, 50, -50), 30d).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setkT(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKl(4E-5).setKq(2E-7));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVpDistance(1000)
                .setVpSize(200, 200)
                .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * produce a picture of a teddy bear holding a balloon that has another balloon
     * in it, sitting on a reflected floor
     */
    @Test
    public void TeddyBear() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 1700))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(150, 150)
                .setVpDistance(1000)
                .setImageWriter(new ImageWriter("TeddyBear", 500, 500))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        scene.geometries.add(
                // ears
                new Sphere(new Point(-50, 75, -100), 20) // the center sphere- left ear
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),
                new Sphere(new Point(50, 75, -100), 20) // the center sphere-right ear
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                new Sphere(new Point(-48, 73, -100), 15) // the center sphere- left ear inner
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.3).setkR(0)),
                new Sphere(new Point(48, 73, -100), 15) // the center sphere-right ear inner
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.3).setkR(0)),

                // blush
                new Sphere(new Point(25, 15, -100), 8) // the right sphere- blush
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.1).setkR(0)),
                new Sphere(new Point(-25, 15, -100), 8) // the left sphere- blush
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.2).setkR(0)),

                // eyes
                new Sphere(new Point(18, 22, -45), 7) // right eye
                        .setEmission(new Color(69, 43, 29)),
                new Sphere(new Point(-18, 22, -45), 7) // left eye
                        .setEmission(new Color(69, 43, 29)),
                new Triangle(new Point(-5, 14, 95), new Point(5, 14, 115), new Point(0, 10, 130))
                        .setEmission(new Color(69, 43, 29))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), // triangle of nose

                // head
                new Sphere(new Point(0, 40, -100), 60) // the center sphere-head
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // body
                new Sphere(new Point(0, -40, -120), 50) // the center body
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // hands
                new Sphere(new Point(-47, -17, -100), 13) // the center sphere- left hand
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),
                new Sphere(new Point(47, -17, -100), 13) // the center sphere-right hand
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // legs
                new Sphere(new Point(-40, -70, -100), 20) // the center sphere- left leg
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),
                new Sphere(new Point(40, -70, -100), 20) // the center sphere-right leg
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // balloons
                new Sphere(new Point(-95, 70, -120), 36) // the center sphere- balloon
                        .setEmission(new Color(255, 0, 0)) //
                        .setMaterial(new Material().setKd(0).setKs(0.2).setShininess(1000).setkT(0.6).setkR(0.2)),
                new Sphere(new Point(-95, 70, -120), 20) // the center sphere- balloon
                        .setEmission(new Color(0, 100, 100))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0).setkR(1)),
                new Triangle(new Point(-70, 42, 95), new Point(-67, 42, 115), new Point(-40, -17, 130))
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))); // line of balloons

//                // floor
//                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -115), new Point(150, 0, -150)) // floor
//                        .setEmission(new Color(167, 199, 231)).setMaterial(new Material().setkT(0.2)),
//                new Triangle(new Point(-150, -150, -115), new Point(-150, -5, -150), new Point(150, 0, -150)) // floor
//                        .setEmission(new Color(167, 199, 231)).setMaterial(new Material().setkT(0.2)));

        scene.lights.add( // add spot light
                new SpotLight(new Color(1000, 600, 400), new Point(-250, 400, 1500), new Vector(-40, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));
        scene.lights.add(new DirectionalLight(new Color(50, 100, 0), new Vector(-50, -1, -1))); // add directional light

        scene.setBackground(new Color(167, 199, 231));

        camera.renderImage().writeToImage();
    }
    @Test
    public void TeddyBearAntiAliasing() {
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 1700))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVpSize(150, 150)
                .setVpDistance(1000)
                .setImageWriter(new ImageWriter("TeddyBearAntiAliasing", 500, 500))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();
                camera.setAntialising(true).setnumOfRaysSuperSampeling(40);

        scene.geometries.add(
                // ears
                new Sphere(new Point(-50, 75, -100), 20) // the center sphere- left ear
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),
                new Sphere(new Point(50, 75, -100), 20) // the center sphere-right ear
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                new Sphere(new Point(-48, 73, -100), 15) // the center sphere- left ear inner
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.3).setkR(0)),
                new Sphere(new Point(48, 73, -100), 15) // the center sphere-right ear inner
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.3).setkR(0)),

                // blush
                new Sphere(new Point(25, 15, -100), 8) // the right sphere- blush
                       i .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.1).setkR(0)),
                new Sphere(new Point(-25, 15, -100), 8) // the left sphere- blush
                        .setEmission(new Color(255, 0, 128))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.2).setkR(0)),

                // eyes
                new Sphere(new Point(18, 22, -45), 7) // right eye
                        .setEmission(new Color(69, 43, 29)),
                new Sphere(new Point(-18, 22, -45), 7) // left eye
                        .setEmission(new Color(69, 43, 29)),
                new Triangle(new Point(-5, 14, 95), new Point(5, 14, 115), new Point(0, 10, 130))
                        .setEmission(new Color(69, 43, 29))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30)), // triangle of nose

                // head
                new Sphere(new Point(0, 40, -100), 60) // the center sphere-head
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // body
                new Sphere(new Point(0, -40, -120), 50) // the center body
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // hands
                new Sphere(new Point(-47, -17, -100), 13) // the center sphere- left hand
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),
                new Sphere(new Point(47, -17, -100), 13) // the center sphere-right hand
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // legs
                new Sphere(new Point(-40, -70, -100), 20) // the center sphere- left leg
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),
                new Sphere(new Point(40, -70, -100), 20) // the center sphere-right leg
                        .setEmission(new Color(153, 102, 0))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0.15).setkR(0)),

                // balloons
                new Sphere(new Point(-95, 70, -120), 36) // the center sphere- balloon
                        .setEmission(new Color(255, 0, 0)) //
                        .setMaterial(new Material().setKd(0).setKs(0.2).setShininess(1000).setkT(0.6).setkR(0.2)),
                new Sphere(new Point(-95, 70, -120), 20) // the center sphere- balloon
                        .setEmission(new Color(0, 100, 100))
                        .setMaterial(new Material().setKd(0.6).setKs(0.9).setShininess(1000).setkT(0).setkR(1)),
                new Triangle(new Point(-70, 42, 95), new Point(-67, 42, 115), new Point(-40, -17, 130))
                        .setEmission(new Color(java.awt.Color.BLACK))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(30))); // line of balloons

//                // floor
//                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -115), new Point(150, 0, -150)) // floor
//                        .setEmission(new Color(167, 199, 231)).setMaterial(new Material().setkT(0.2)),
//                new Triangle(new Point(-150, -150, -115), new Point(-150, -5, -150), new Point(150, 0, -150)) // floor
//                        .setEmission(new Color(167, 199, 231)).setMaterial(new Material().setkT(0.2)));

        scene.lights.add( // add spot light
                new SpotLight(new Color(1000, 600, 400), new Point(-250, 400, 1500), new Vector(-40, -1, -2))
                        .setKl(0.0004).setKq(0.0000006));
        scene.lights.add(new DirectionalLight(new Color(50, 100, 0), new Vector(-50, -1, -1))); // add directional light

        scene.setBackground(new Color(167, 199, 231));

        camera.renderImage().writeToImage();
    }

}
