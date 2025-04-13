package core;

public class Game implements Runnable  {
	
	private Thread gameThread;
    private boolean running = false;
	
	private Window window;
    private Renderer renderer;
    private InputHandler input;
   

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
        
        renderer = new Renderer();
        
        input = new InputHandler(window);
    }

    private void gameLoop() {
    	final int FPS = 60;
        final double drawInterval = 1000000000.0 / FPS; 
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (!window.shouldClose()) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // Update game logic here

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
