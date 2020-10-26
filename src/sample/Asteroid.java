/*
 * \file Asteroid.java
 * \brief Implements Asteroids
 * \author Nongma SORGHO
 * \date 10.26.2020
 * \version 1.0.0
 */

package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class Asteroid {
    public Point3D position;
    private final Point3D trajectory;
    private final String name;
    private final Sphere asteroid;
    private final int radius;

    public Asteroid(Sphere asteroid, int radius, Point3D initialPosition, Point3D initialTrajectory, String name){
        /*
         * \func public Asteroid
         * \brief Constructs an Asteroid object
         *
         * \param Sphere asteroid
         * Sphere asteroid
         * \param int radius
         * Radius of the asteroid
         * \param Point3D initialPosition
         * Initial position of the asteroid
         * \param Point3D initialTrajectory
         * Initial trajectory of the asteroid
         * \param name
         * Name of the asteroid
         */
        this.position = initialPosition;
        this.trajectory = initialTrajectory;
        this.name = name;
        this.asteroid = asteroid;
        this.radius = radius;
        asteroid.setTranslateX(initialPosition.getX());
        asteroid.setTranslateY(initialPosition.getY());
        asteroid.setTranslateZ(initialPosition.getZ());
        asteroid.setRadius(radius);
        asteroid.setCullFace(CullFace.BACK);
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
    public Sphere getAsteroid() {
        return asteroid;
    }

    // -- Radius getter -- //
    public int getRadius() { return radius; }

    public void evolve(int speed){
        // -- Animates asteroid -- //
        KeyValue keyValue = new KeyValue(asteroid.translateXProperty(), trajectory.getX() * speed);
        KeyValue keyValue2 = new KeyValue(asteroid.translateYProperty(), trajectory.getY() * speed);
        KeyValue keyValue3 = new KeyValue(asteroid.translateZProperty(), trajectory.getZ() * speed);

        // Asteroids are evolving in the universe for 60 seconds
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(60), keyValue, keyValue2, keyValue3);
        Timeline timeline = new Timeline(keyFrame);

        this.position = new Point3D(asteroid.getTranslateX(), asteroid.getTranslateY(), asteroid.getTranslateZ());

        // Implements asteroids movement
        timeline.play();

        // Setter for the newer values of asteroid's Translate
        this.position = new Point3D(asteroid.getTranslateX(), asteroid.getTranslateY(), asteroid.getTranslateZ());

    }
}
