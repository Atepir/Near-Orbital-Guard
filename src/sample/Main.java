/*
 * \file Main.java
 * \brief Implements Main Routine
 * \author Nongma SORGHO
 * \date 10.26.2020
 * \version 1.0.0
 */

package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import java.util.Random;

public class Main extends Application {

    private double startDragX;
    private double startDragY;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Final variables
        final int NB_ASTEROIDS = 50;

        // Initializing earth
        Sphere earth = new Sphere();

        // Initializing search area
        Sphere searchA = new Sphere();

        // Earth params
        Point3D inipos_earth = new Point3D(714, 332, 0);
        Point3D initra_earth = new Point3D(0, 0, 0);
        String tex_earth = "textures/tex_earth02.jpg";
        String name_earth = "Earth";
        int rad_earth = 100;
        Planet obj_earth = new Planet(earth, rad_earth, tex_earth, inipos_earth, initra_earth, name_earth);

        // Search area params
        Point3D inipos_searchA = new Point3D(inipos_earth.getX(), inipos_earth.getY(), inipos_earth.getZ());
        Point3D initra_searchA = new Point3D(0, 0, 0);
        String tex_searchA = "textures/tex_earth01.jpg";
        String name_searchA = "Danger Zone";
        int rad_searchA = 120;
        Planet obj_searchA = new Planet(searchA, rad_searchA, tex_searchA, inipos_searchA, initra_searchA, name_searchA);

        // Search area special alpha filter
        PhongMaterial mat_orbit = new PhongMaterial();
        mat_orbit.setDiffuseColor(new Color(0,0,0.9,0.05));
        obj_searchA.getPlanet().setMaterial(mat_orbit);

        // Asteroid(s) params

        Sphere[] array_asteroids = new Sphere[NB_ASTEROIDS];

        Point3D[] array_inipos_asteroids = new Point3D[NB_ASTEROIDS];

        Point3D[] array_initra_asteroids = new Point3D[NB_ASTEROIDS];

        String[] array_name_asteroids = new String[NB_ASTEROIDS];

        int[] array_rad_asteroids = new int[NB_ASTEROIDS];

        Asteroid[] array_obj_asteroids = new Asteroid[NB_ASTEROIDS];

        for (int i_asteroid=0; i_asteroid < NB_ASTEROIDS; i_asteroid ++){
            int max = 1428;
            Random generator = new Random();
            array_asteroids[i_asteroid] = new Sphere();
            array_inipos_asteroids[i_asteroid] = new Point3D(generator.nextInt(max), generator.nextInt(max), generator.nextInt(max));
            array_initra_asteroids[i_asteroid] = new Point3D(generator.nextInt(max), generator.nextInt(max), generator.nextInt(max));
            array_name_asteroids[i_asteroid] = "A-"+i_asteroid;
            if (i_asteroid >= 3 && i_asteroid <= 20) array_rad_asteroids[i_asteroid] = i_asteroid;
            else array_rad_asteroids[i_asteroid] = generator.nextInt(20);
            array_obj_asteroids[i_asteroid] = new Asteroid(array_asteroids[i_asteroid], array_rad_asteroids[i_asteroid], array_inipos_asteroids[i_asteroid], array_initra_asteroids[i_asteroid], array_name_asteroids[i_asteroid]);
        }

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(0);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);

        // Render
        Group root = new Group();
        root.getChildren().addAll(obj_earth.getPlanet(), obj_searchA.getPlanet());
        for (int i=0; i<NB_ASTEROIDS; i++){
            root.getChildren().add(array_obj_asteroids[i].getAsteroid());
            array_obj_asteroids[i].evolve(2);
        }
        Scene scene = new Scene(root,1428,764, Color.BLACK);
        scene.setCamera(camera);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Near Orbital Guard - Copyright S.N.");

        // Planet and search area evolution
        Point3D earthRotationAxis = new Point3D(1, 2, 0);
        obj_earth.evolve(60, earthRotationAxis);
        obj_searchA.evolve(60, earthRotationAxis);

        // Checking collisions
        PhongMaterial mat_onCollision = new PhongMaterial();
        mat_onCollision.setDiffuseColor(new Color(0.9,0,0,0.05));

        // Controlls
        scene.setOnKeyPressed(event -> {
            double deltaX;
            double deltaY;
            double deltaZ;
            double distance_to_asteroids;
            switch (event.getCode()) {
                case UP -> {
                    turnUp(camera, inipos_earth);
                }
                case DOWN -> {
                    turnDown(camera, inipos_earth);
                }
                case LEFT -> {
                    turnLeft(camera, inipos_earth);
                }
                case RIGHT -> {
                    turnRight(camera, inipos_earth);
                }
                // SPACE button shows asteroids who are or have been recently in collision with the planet
                case SPACE -> {
                    for (int j=0; j<NB_ASTEROIDS; j++){
                        deltaX = array_obj_asteroids[j].getAsteroid().getTranslateX() - obj_earth.getPlanet().getTranslateX();
                        deltaY = array_obj_asteroids[j].getAsteroid().getTranslateY() - obj_earth.getPlanet().getTranslateY();
                        deltaZ = array_obj_asteroids[j].getAsteroid().getTranslateZ() - obj_earth.getPlanet().getTranslateZ();
                        distance_to_asteroids = Math.sqrt(Math.pow(deltaX, 2)+Math.pow(deltaY, 2)+Math.pow(deltaZ, 2));

                        if (distance_to_asteroids <= obj_searchA.getPlanet().getRadius() + array_obj_asteroids[j].getAsteroid().getRadius()) {
                            array_obj_asteroids[j].getAsteroid().setMaterial(mat_onCollision);
                        }
                    }
                }
            }
        });

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                scene.setOnMousePressed(e -> {
                    startDragX = e.getSceneX();
                    startDragY = e.getSceneY();
                });

                scene.setOnMouseDragged(e -> {
                    if ((startDragX > startDragY) || (startDragX < 0 && startDragY < 0 && startDragX < startDragY)) {
                        Point3D point3D_01 = new Point3D(0, -1, 0);
                        camera.setRotationAxis(point3D_01);
                        camera.setRotate(e.getSceneX() - startDragX);
                    }else{
                        Point3D point3D_02 = new Point3D(1, 0, 0);
                        camera.setRotationAxis(point3D_02);
                        camera.setRotate(e.getSceneY() - startDragY);
                    }
                });
            }
        });

        primaryStage.show();
    }

    void turnUp(PerspectiveCamera camera, Point3D earth_position){
        // Before passing to the next axis, we sanitize our input with the previous changes
        Point3D point3D = new Point3D(earth_position.getX(), 0, 0);
        camera.setRotationAxis(point3D);
        camera.setRotate(camera.getRotate() - 20);
    }

    void turnDown(PerspectiveCamera camera, Point3D earth_position){
        // Before passing to the next axis, we sanitize our input with the previous changes
        Point3D point3D = new Point3D(earth_position.getX(), 0, 0);
        camera.setRotationAxis(point3D);
        camera.setRotate(camera.getRotate() + 20);
    }

    void turnLeft(PerspectiveCamera camera, Point3D earth_position){
        // Before passing to the next axis, we sanitize our input with the previous changes
        Point3D point3D = new Point3D(0, earth_position.getY(), 0);
        camera.setRotationAxis(point3D);
        camera.setRotate(camera.getRotate() + 20);
    }

    void turnRight(PerspectiveCamera camera, Point3D earth_position){
        // Before passing to the next axis, we sanitize our input with the previous changes
        Point3D point3D = new Point3D(0, earth_position.getY(), 0);
        camera.setRotationAxis(point3D);
        camera.setRotate(camera.getRotate() - 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
