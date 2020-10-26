package sample;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class Main extends Application {

    private double startDragX;
    private double startDragY;
    Point3D formerRotation = new Point3D(0, 0, 0);

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Initializing earth
        Sphere earth = new Sphere();

        // Initializing search area
        Sphere searchA = new Sphere();

        // Earth params
        Point3D inipos_earth = new Point3D(300, 250, 0);
        Point3D initra_earth = new Point3D(0, 0, 0);
        String tex_earth = "tex_earth02.jpg";
        String name_earth = "Earth";
        int rad_earth = 100;
        Planet obj_earth = new Planet(earth, rad_earth, tex_earth, inipos_earth, initra_earth, name_earth);

        // Search area params
        Point3D inipos_searchA = new Point3D(300, 250, 0);
        Point3D initra_searchA = new Point3D(0, 0, 0);
        String tex_searchA = "tex_earth01.jpg";
        String name_searchA = "Danger Zone";
        int rad_searchA = 120;
        Planet obj_searchA = new Planet(searchA, rad_searchA, tex_searchA, inipos_searchA, initra_searchA, name_searchA);

        // Search area special alpha filter
        PhongMaterial mat_orbit = new PhongMaterial();
        mat_orbit.setDiffuseColor(new Color(0.5,0.5,0.5,0.1));
        obj_searchA.getPlanet().setMaterial(mat_orbit);

        // Asteroid(s) params
        Sphere asteroid01 = new Sphere();
        Sphere asteroid02 = new Sphere();

        Point3D inipos_asteroid01 = new Point3D(400, 400, 0);
        Point3D inipos_asteroid02 = new Point3D(100, 100, 0);

        Point3D initra_asteroid01 = new Point3D(500, 300, 0);
        Point3D initra_asteroid02 = new Point3D(200, 300, 0);

        String name_asteroid01 = "A1";
        String name_asteroid02 = "A2";

        int rad_asteroid01 = 10;
        int rad_asteroid02 = 5;

        Asteroid obj_asteroid01 = new Asteroid(asteroid01, rad_asteroid01, inipos_asteroid01, initra_asteroid01, name_asteroid01);
        Asteroid obj_asteroid02 = new Asteroid(asteroid02, rad_asteroid02, inipos_asteroid02, initra_asteroid02, name_asteroid02);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(-50);
        camera.setTranslateY(0);
        camera.setTranslateZ(0);

        // Render
        Group root = new Group();
        root.getChildren().addAll(obj_earth.getPlanet(), obj_searchA.getPlanet(), obj_asteroid01.getAsteroid(), obj_asteroid02.getAsteroid());
        Scene scene = new Scene(root,700,500,Color.BLACK);
        scene.setCamera(camera);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Near Orbital Guard - Copyright S.N.");

        // Asteroids evolution
        obj_asteroid01.evolve(10);
        obj_asteroid02.evolve(10);

        // Planet and search area evolution
        Point3D earthRotationAxis = new Point3D(1, 2, 1);
        obj_earth.evolve(60, earthRotationAxis);
        obj_searchA.evolve(60, earthRotationAxis);

        // Controlls
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP -> {
                        turnUp(earth, formerRotation);
                        formerRotation = turnUp(earth, formerRotation);
                    }
                    case DOWN -> {
                        turnDown(earth, formerRotation);
                        formerRotation = turnDown(earth, formerRotation);
                    }
                    case LEFT -> {
                        turnLeft(earth, formerRotation);
                        formerRotation = turnLeft(earth, formerRotation);
                    }
                    case RIGHT -> {
                        turnRight(earth, formerRotation);
                        formerRotation = turnRight(earth, formerRotation);
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
                        earth.setRotationAxis(point3D_01);
                        earth.setRotate(e.getSceneX() - startDragX);
                    }else{
                        Point3D point3D_02 = new Point3D(1, 0, 0);
                        earth.setRotationAxis(point3D_02);
                        earth.setRotate(e.getSceneY() - startDragY);

                    }
                });
            }
        });

        primaryStage.show();
    }

    Point3D turnUp(Sphere globe, Point3D formerRotation){
        Point3D point3D = new Point3D(1, 0, 0);

        // Before passing to the next axis, we sanitize our input with the previous changes
        globe.setRotationAxis(formerRotation);
        globe.setRotate(globe.getRotate());

        globe.setRotationAxis(point3D);
        globe.setRotate(globe.getRotate() + 10);
        return point3D;
    }

    Point3D turnDown(Sphere globe, Point3D formerRotation){
        Point3D point3D = new Point3D(1, 0, 0);

        // Before passing to the next axis, we sanitize our input with the previous changes
        globe.setRotationAxis(formerRotation);
        globe.setRotate(globe.getRotate());

        globe.setRotationAxis(point3D);
        globe.setRotate(globe.getRotate() - 10);
        return point3D;
    }

    Point3D turnLeft(Sphere globe, Point3D formerRotation){
        Point3D point3D = new Point3D(0, 1, 0);

        // Before passing to the next axis, we sanitize our input with the previous changes
        globe.setRotationAxis(formerRotation);
        globe.setRotate(globe.getRotate());

        globe.setRotationAxis(point3D);
        globe.setRotate(globe.getRotate() - 10);
        return point3D;
    }

    Point3D turnRight(Sphere globe, Point3D formerRotation){
        Point3D point3D = new Point3D(0, 1, 0);

        // Before passing to the next axis, we sanitize our input with the previous changes
        globe.setRotationAxis(formerRotation);
        globe.setRotate(globe.getRotate());

        globe.setRotationAxis(point3D);
        globe.setRotate(globe.getRotate() + 10);
        return point3D;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
