package main;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {

	public static final float FOV = (float) Math.toRadians(60);
	public static final float Z_NEAR = 0.01f;
	public static final float Z_FAR = 1000f;
	
	private final String gameName;
	
	private int windowWidth, windowHeight;
	private long window;
	
	private boolean resizeBool, vSyncBool;
	
	private final Matrix4f projectionMatrix;

	public WindowManager(String p_gameName, int p_windowWidth, int p_windowHeight, boolean p_vSyncBool) {
		this.gameName = p_gameName;
		this.windowWidth = p_windowWidth;
		this.windowHeight = p_windowHeight;
		this.vSyncBool = p_vSyncBool;
		projectionMatrix = new Matrix4f();
	}
	
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set(); //This is an error callback which will be used to give more info on errors
		
		// Initialize GLFW.
		if ( glfwInit() == false ) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //Initially the window will be invisible until everything is populated before showing it
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		
		
		//Create window
		window = glfwCreateWindow(windowWidth, windowHeight, gameName, MemoryUtil.NULL, MemoryUtil.NULL);
		if ( window == MemoryUtil.NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		//For resizing the window (if enabled) where taking in the window as a paramater, returns the window itself and its height/width
		GLFW.glfwSetFramebufferSizeCallback(window, (window, windowWidth, windowHeight) -> {
			this.windowWidth = windowWidth;
			this.windowHeight = windowHeight;
		});
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); 
			}
		});

		
		//Center the window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor()); // Get the resolution of the primary monitor
		glfwSetWindowPos( window,  (vidmode.width() - windowWidth) / 2,  (vidmode.height() - windowHeight) / 2);
		
		// Make the OpenGL context current
		GLFW.glfwMakeContextCurrent(window);
		
		// Enable v-sync
		if(isVsyncOn()) {
			GLFW.glfwSwapInterval(1); //Remember in C, 1 means True and 0 means False.
		}

		// Make the window visible
		GLFW.glfwShowWindow(window);
		
		// This line is critical for LWJGL's interoperation with GLFW's OpenGL context, or any context that is managed externally. LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //Black background to start
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void update() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents(); //This will tell opengl to start rendering all the objects we have placced in a queue
	
	}
	

	public void cleanUp() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////////
	
	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public boolean isKeyPressed(int keycode) {
		return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
	}
	
	public String getTitle() {
		return gameName;
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(window, title);
	}
	
	public void setClearColour(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}
	
	public boolean isVsyncOn() {
		return vSyncBool;
	}
	
	public void setResizeablePossible(boolean resize) {
		this.resizeBool = resize;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public long getWindow() {
		return window;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Matrix4f updateProjectionMatrix() {
		float aspectRatio = (float) windowWidth/windowHeight;
		return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
	}
	
	public Matrix4f updateProjectionMatrix(Matrix4f p_matrix, int p_width, int p_height) {
		float aspectRatio = (float) p_width / p_height;
		return p_matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
	}
	
}
