package core;

import org.joml.Vector3f;

import entities.ObjectCreator;
import graphics.Shader;
import input.InputHandler;

public class Game implements Runnable  {
	
	private Thread gameThread;
    private boolean running = false;
	
	private Window window;
	private Shader shader;
    private Renderer renderer;
    private InputHandler input;
	private Camera camera;
   

    public void start() {
        running = true;
        gameThread = new Thread(this, "Optimised Minecraft Game Thread"); //Launches the game loop on its own thread
        gameThread.start();
    }
    
    
    @Override
    public void run() {
        init();
        gameLoop();
        cleanup();
    }

    private void init() {
        window = new Window(1280, 720, "Minecraft Optimised");
        window.create();
        
        shader = new Shader();
        
        camera = new Camera(1280f / 720f);
        camera.setPosition(new Vector3f(0, 0, 2)); 
        
        renderer = new Renderer(camera, shader);
        
        input = new InputHandler(window, camera);
        
        //Initial drawing
    	float[] exampleTriangleVerts3D = {
                0.0f,  0.5f, 0.0f,   // Top
               -0.5f, -0.5f, 0.0f,   // Bottom left
                0.5f, -0.5f, 0.0f    // Bottom right
           };
    	
    	renderer.addOjectToRenderObjectList(new ObjectCreator(exampleTriangleVerts3D, 3));
    }

    private void gameLoop() {
    	final int FPS = 60;
        final double drawInterval = 1000000000.0 / FPS; 
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        float angle = 0;

        while (!window.shouldClose()) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // Update game logic here
            	input.handleInput();
            	
                // Render everything
                renderer.draw();

                // Swap buffers and poll input
                window.update();
                window.pollEvents();

                delta--; 
            }
        }
    }

    private void cleanup() {
        renderer.cleanup();
        window.destroy();
    }
    
    
    private void printFPS(long timer, int frames) {
    	// Print FPS every second
        if (System.currentTimeMillis() - timer >= 1000) {
            System.out.println("FPS: " + frames);
            frames = 0;
            timer += 1000;
        }
    }
}
