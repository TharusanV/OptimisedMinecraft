package graphics;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Shader {

    private int programId;

    public Shader(String vertexSrc, String fragmentSrc) {
    	
        //Create and compile vertex shader
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSrc);
        glCompileShader(vertexShader);
        
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Vertex shader failed:\n" + glGetShaderInfoLog(vertexShader));
        }

        //Create and compile fragment shader
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSrc);
        glCompileShader(fragmentShader);
        
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Fragment shader failed:\n" + glGetShaderInfoLog(fragmentShader));
        }

        //Link shaders into a program
        programId = glCreateProgram();
        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Program linking failed:\n" + glGetProgramInfoLog(programId));
        }

        // Delete shaders after linking (they’re now part of the program)
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void use() {
        glUseProgram(programId); //Tell OpenGL to use this shader program
    }

    public void cleanup() {
        glDeleteProgram(programId); //Free GPU memory when done
    }

    public int getId() {
        return programId;
    }
}