package core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {

    private long window;
    private int width, height;
    private String title;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void create() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
            window,
            (vidmode.width() - width) / 2,
            (vidmode.height() - height) / 2
        );

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable V-Sync
        glfwShowWindow(window);

        GL.createCapabilities();

        glClearColor(0.1f, 0.1f, 0.2f, 0.0f);
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void update() {
        glfwSwapBuffers(window);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public long getWindowHandle() {
        return window;
    }

    public void destroy() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}