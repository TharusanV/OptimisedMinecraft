package core;

public class Game {
	private Window window;
    private Renderer renderer;
    private InputHandler input;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        window = new Window(1280, 720, "Minecraft Optimised");
        renderer = new Renderer();
        input = new InputHandler(window);

        window.create();
        renderer.init();
    }

    private void loop() {
        while (!window.shouldClose()) {
            window.pollEvents();
            input.handleInput();

            renderer.render();

            window.update();
        }
    }

    private void cleanup() {
        renderer.cleanup();
        window.destroy();
    }
}
