package sample;

import javafx.animation.RotateTransition;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class Planet {
    private final Point3D position;
    private final Point3D trajectory;
    private final String name;
    private final Sphere planet;
    private final int radius;

    public Planet(Sphere planet, int radius, String texture, Point3D initialPosition, Point3D initialTrajectory, String name){
        this.position = initialPosition;
        this.trajectory = initialTrajectory;
        this.name = name;
        this.planet = planet;
        this.radius = radius;

        // Planet initial paramaters
        planet.setTranslateX(initialPosition.getX());
        planet.setTranslateY(initialPosition.getY());
        planet.setRadius(radius);
        planet.setCullFace(CullFace.BACK);

        // Planet materials and/or textures
        PhongMaterial mat_planet = new PhongMaterial();
        mat_planet.setDiffuseMap(new Image(getClass().getResourceAsStream(texture)));
        planet.setMaterial(mat_planet);
    }

    // -- Position getter -- //
    public Point3D getPosition() {
        return position;
    }

    // -- Trajectory getter -- //
    public Point3D getTrajectory() {
        return trajectory;
    }

    // -- Name getter -- //
    public String getName() {
        return name;
    }

    // -- Asteroid getter -- //
    public Sphere getPlanet() {
        return planet;
    }

    // -- Radius getter -- //
    public int getRadius(){
        return radius;
    }

    // -- Planet evolution along the time -- //
    public void evolve(int rotationTime, Point3D rotationAxis){
        // -- Animates globe -- //

        // Planet rotation axis
        planet.setRotationAxis(rotationAxis);

        RotateTransition rt = new RotateTransition(Duration.seconds(rotationTime), planet);
        rt.setByAngle(360);
        rt.setCycleCount((int)Double.POSITIVE_INFINITY);
        rt.play();
    }
}
