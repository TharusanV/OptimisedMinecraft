package core;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import entities.ObjectCreator;
import graphics.Shader;

public class Renderer {

    private Shader shader;
    private List<ObjectCreator> all3DObjects = new ArrayList<ObjectCreator>();
    private Camera camera;



    public Renderer(Camera p_camera, Shader p_Shader) {
    	
    	this.camera = p_camera;
    	this.shader = p_Shader;
    	
        // Enable OpenGL features
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public void draw() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.use();

        // Create transformation matrices
        Matrix4f model = new Matrix4f().identity();
        Matrix4f view = camera.getViewMatrix();
        Matrix4f projection = camera.getProjectionMatrix();

        // Use the shader's built-in uniform setter
        shader.setUniformMatrix4f("model", model);
        shader.setUniformMatrix4f("view", view);
        shader.setUniformMatrix4f("projection", projection);

        for(ObjectCreator object : all3DObjects) {
        	object.draw();
        }
    }

    public void cleanup() {
        // Free VAO/VBO
    	for(ObjectCreator object : all3DObjects) {
        	object.cleanup();
        }
    	
        shader.cleanup();   // Free shader program
    }
    
    ///////////////////////////////////////////////////////////////////////////////
    
    public void addOjectToRenderObjectList(ObjectCreator p_obj) {
    	all3DObjects.add(p_obj);
    }
}