package core;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {

    private final Window window;

    public InputHandler(Window window) {
        this.window = window;
    }

    public void handleInput() {
        if (glfwGetKey(window.getWindowHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window.getWindowHandle(), true);
        }
    }
}