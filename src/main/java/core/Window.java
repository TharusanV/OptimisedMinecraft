package core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

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
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE); //Mac OS only but adding it because why not


        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); //Retrieves screen resolution and refresh rate info 
        glfwSetWindowPos( window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window); //Tells GLFW and OpenGL that all OpenGL calls should apply to this window (OpenGL needs to know which context to apply commands to)
        
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        
        glfwSwapInterval(1); // 1 Enable V-Sync which syncs to monitor's refresh rate (Prevents screen tearing) / 0 would mean no-sync therefore maxing out the framerate (can cause tearing but lowers latency)
        
        glfwShowWindow(window);

        GL.createCapabilities(); //This acts as OpenGL activation but for java, with Until this is called, OpenGL functions like glClearColor() or glDrawArrays() won’t work

        glClearColor(0.1f, 0.1f, 0.2f, 0.0f);
		
    }

    public void update() {
    	//Shows the rendered frame by swapping the back buffer (where you just drew everything) to the front buffer (the screen). 
        //Double buffering is standard in modern rendering — draw in the background, then swap it to avoid flickering.
        //Should be called once per frame after rendering is done.
        glfwSwapBuffers(window); 

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void destroy() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }
    
    ///////////////////////////////////////////////////////////////////////////
    
    public void pollEvents() {
        //Checks for OS-level input/events (keyboard, mouse, window resizing, etc.) and updates internal GLFW state.
        //Without this, the window would freeze and you’d never get input or window interactions.
        //You’ll call this every frame, typically once per game loop.
        glfwPollEvents(); 
    }    
    
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public long getWindowHandle() {
        return window;
    }
}