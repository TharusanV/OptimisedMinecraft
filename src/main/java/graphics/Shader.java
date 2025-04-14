package graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public class Shader {

	/*
	Shaders are small programs that run on the GPU. They control how things are drawn on the screen—like how colors and lighting behave. 
	In a typical OpenGL 3D game, you’ll mainly use two types:
		-> Vertex Shader: Runs once per vertex. It’s responsible for transforming 3D coordinates to 2D screen coordinates.
		-> Fragment Shader: Runs once per pixel. It determines the final color of each pixel.
	
	Simplified version of the Shader pipeline is as followed:
		(1) Your CPU sends data (vertex positions, textures, etc.) to the GPU.
		(2) Vertex Shader manipulates vertices (positions, normals, etc.)
		(3) Rasterization happens (turning geometry into pixels).
		(4) Fragment Shader colors the pixels.
		(5) OpenGL draws it to your screen.
	
	
	*/
	
	
    private int shaderProgramId;
    private String vertexShaderSource;
    private String fragmentShaderSource;

    public Shader() {
    	
    	// Loading my shader sources
        try {
		    vertexShaderSource = new String(getClass().getClassLoader().getResourceAsStream("basic.vert").readAllBytes(), StandardCharsets.UTF_8);
		    fragmentShaderSource = new String(getClass().getClassLoader().getResourceAsStream("basic.frag").readAllBytes(), StandardCharsets.UTF_8);
		} 
        catch (IOException | NullPointerException e) {
		    e.printStackTrace();
		}

    	
        // Create and compile vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER); // Create shader object
        glShaderSource(vertexShader, vertexShaderSource);    // Load source code into shader
        glCompileShader(vertexShader);                       // Compile it

        // Check vertex shader compile status
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Vertex shader failed:\n" + glGetShaderInfoLog(vertexShader));
        }

        // Create and compile fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        // Check fragment shader compile status
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Fragment shader failed:\n" + glGetShaderInfoLog(fragmentShader));
        }

        // Create a program and link shaders
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexShader);
        glAttachShader(shaderProgramId, fragmentShader);
        glLinkProgram(shaderProgramId);

        // Check program linking status
        if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program linking failed:\n" + glGetProgramInfoLog(shaderProgramId));
        }

        // Shaders are no longer needed after linking
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    // Use this shader program (bind it for rendering)
    public void use() {
        glUseProgram(shaderProgramId);
    }

    // Free GPU memory when done
    public void cleanup() {
        glDeleteProgram(shaderProgramId);
    }

    // Retrieve shader program ID
    public int getShaderProgramId() {
        return shaderProgramId;
    }

    // Set a mat4 uniform in the shader (e.g. model/view/projection matrix)
    public void setUniformMatrix4f(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16); // Allocate float[16] on stack
            matrix.get(buffer);                         // Convert matrix to buffer
            int location = glGetUniformLocation(shaderProgramId, name); // Get uniform location
            glUniformMatrix4fv(location, false, buffer);          // Upload to shader
        }
    }
}