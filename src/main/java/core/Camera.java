package core;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;

    private float fov = 70.0f;
    private float nearPlane = 0.01f;
    private float farPlane = 1000.0f;

    private float aspectRatio;

    public Camera(float aspectRatio) {
        position = new Vector3f(0, 0, 0); // Start at origin
        rotation = new Vector3f(0, 0, 0); // No rotation initially
        this.aspectRatio = aspectRatio;   // window width / height
    }
    
    // Creates the projection matrix using perspective projection - makes objects farther away appear smaller
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(
            (float) Math.toRadians(fov),
            aspectRatio,
            nearPlane, // Closest visible distance
            farPlane 	// Furthest visible distance
        );
    }


    // Builds the view matrix based on the camera's position and rotation - simulates moving the camera through space
    public Matrix4f getViewMatrix() {
        Matrix4f view = new Matrix4f();

        // Rotate the scene to match the camera's rotation.
        view.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
        view.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        view.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));

        // Translate it in the opposite direction of the camera position (to simulate "moving" the camera)
        view.translate(new Vector3f(position).negate());

        return view;
    }
    
    
    //////////////////////////////////////////////////////////////////////////
    
    public void moveForward(float distance) {
        position.x += (float) Math.sin(Math.toRadians(rotation.y)) * distance;
        position.z -= (float) Math.cos(Math.toRadians(rotation.y)) * distance;
    }

    public void moveBackward(float distance) {
        moveForward(-distance);
    }

    public void strafeLeft(float distance) {
        position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * distance;
        position.z -= (float) Math.cos(Math.toRadians(rotation.y - 90)) * distance;
    }

    public void strafeRight(float distance) {
        strafeLeft(-distance);
    }



    //////////////////////////////////////////////////////////////////////////
    
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void move(Vector3f offset) {
        this.position.add(offset);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }

    public void rotate(Vector3f offset) {
        this.rotation.add(offset);
    }
}