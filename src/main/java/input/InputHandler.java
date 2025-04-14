package input;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import core.Camera;
import core.Window;

public class InputHandler {

    private final Window window;
    private Camera camera;
    private float speed;
    
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;
    
    public InputHandler(Window p_window, Camera p_camera) {
        this.window = p_window;
        this.camera = p_camera;
        
        this.speed = 0.05f;
    }
    
    public void handleMouseLook() {
        double[] xpos = new double[1];
        double[] ypos = new double[1];
        glfwGetCursorPos(window.getWindowHandle(), xpos, ypos);

        if (firstMouse) {
            lastMouseX = xpos[0];
            lastMouseY = ypos[0];
            firstMouse = false;
        }

        float sensitivity = 0.1f;
        float dx = (float) (xpos[0] - lastMouseX) * sensitivity;
        float dy = (float) (lastMouseY - ypos[0]) * sensitivity;

        lastMouseX = xpos[0];
        lastMouseY = ypos[0];

        camera.rotate(new Vector3f(dy, dx, 0f));

        // clamp pitch to prevent flipping
        if (camera.getRotation().x > 89.0f) camera.getRotation().x = 89.0f;
        if (camera.getRotation().x < -89.0f) camera.getRotation().x = -89.0f;
    }

    public void handleKeyboard() {
        if (glfwGetKey(window.getWindowHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window.getWindowHandle(), true);
        }
        
        if (glfwGetKey(window.getWindowHandle(), GLFW_KEY_W) == GLFW_PRESS) {
            camera.moveForward(speed);
        }
        if (glfwGetKey(window.getWindowHandle(), GLFW_KEY_S) == GLFW_PRESS) {
            camera.moveBackward(speed);
        }
        if (glfwGetKey(window.getWindowHandle(), GLFW_KEY_A) == GLFW_PRESS) {
            camera.strafeLeft(speed);
        }
        if (glfwGetKey(window.getWindowHandle(), GLFW_KEY_D) == GLFW_PRESS) {
            camera.strafeRight(speed);
        }
       
    }
    
    public void handleInput() {
        handleKeyboard();
        handleMouseLook();
    }
}