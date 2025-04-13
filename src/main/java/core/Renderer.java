package core;

import static org.lwjgl.opengl.GL11.*;

import entities.ObjectCreator;
import graphics.Shader;

public class Renderer {
	
	private Shader shader;
    private ObjectCreator triangle;

    // Vertex and fragment shader source
    private final String vertexShaderSource = """
        #version 330 core
        layout (location = 0) in vec3 aPos;

        void main() {
            gl_Position = vec4(aPos, 1.0);
        }
    """;

    private final String fragmentShaderSource = """
        #version 330 core
        out vec4 FragColor;

        void main() {
            FragColor = vec4(1.0, 0.3, 0.2, 1.0); // Red
        }
    """;
	
	public Renderer() {
		glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		
		shader = new Shader(vertexShaderSource, fragmentShaderSource);
		
		float[] triangleVerts3D = {
	             0.0f,  0.5f, 0.0f,   // Top
	            -0.5f, -0.5f, 0.0f,   // Bottom left
	             0.5f, -0.5f, 0.0f    // Bottom right
	        };
		
		triangle = new ObjectCreator(triangleVerts3D, 3); 
		
		
		
	}

    public void draw() {    	
    	shader.use();   
    	
        triangle.draw();
    }

    public void cleanup() {
    	triangle.cleanup(); // Free VAO/VBO
    	
        shader.cleanup();   // Free shader program
    }
}