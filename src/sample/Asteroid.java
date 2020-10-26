package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class Asteroid {
    private final Point3D position;
    private final Point3D trajectory;
    private final String name;
    private final Sphere asteroid;

    public Asteroid(Sphere asteroid, int radius, Point3D initialPosition, Point3D initialTrajectory, String name){
        this.position = initialPosition;
        this.trajectory = initialTrajectory;
        this.name = name;
        this.asteroid = asteroid;
        asteroid.setTranslateX(initialPosition.getX());
        asteroid.setTranslateY(initialPosition.getY());
        asteroid.setRadius(radius);
        asteroid.setCullFace(CullFace.BACK);
    }

    // Position getter
    public Point3D getPosition() {
        return position;
    }

    // Trajectory getter
    public Point3D getTrajectory() {
        return trajectory;
    }

    // Name getter
    public String getName() {
        return name;
    }

    // Asteroid getter
    public Sphere getAsteroid() {
        return asteroid;
    }

    public void evolve(int world_size){
        // Animates asteroid
        KeyValue keyValue = new KeyValue(asteroid.translateXProperty(), trajectory.getX() * world_size);
        KeyValue keyValue2 = new KeyValue(asteroid.translateYProperty(), trajectory.getY() * world_size);
        KeyValue keyValue3 = new KeyValue(asteroid.translateZProperty(), trajectory.getZ() * world_size);

        // Evolution cycle is the cycle on the planet
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(60), keyValue, keyValue2, keyValue3);
        Timeline timeline = new Timeline(keyFrame);

        timeline.play();
    }
}
