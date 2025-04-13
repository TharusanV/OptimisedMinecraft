package entities;

import static org.lwjgl.opengl.GL30.*;
import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;

public class ObjectCreator {	
	private int vaoId;
    private int vboId;
    private float[] vertices;
    private int dimensions; // 2 = 2D, 3 = 3D
	
	public ObjectCreator(float[] p_vertices, int p_dimensions) {
		this.vertices = p_vertices;
		this.dimensions = p_dimensions;
		
		//VAO - basically an array of data thats stored for every vertex.
        vaoId = glGenVertexArrays(); //Generate a new VAO ID
        glBindVertexArray(vaoId); //Bind that VAO (start recording attribute config)
        
        //VBO — this is the raw vertex data that lives on the GPU.
        vboId = glGenBuffers(); //Generate a new VBO ID
        glBindBuffer(GL_ARRAY_BUFFER, vboId); //Bind it as an array buffer

        // Upload vertex data
        FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length); //Allocate native memory 
        vertexBuffer.put(vertices).flip(); //Put vertex data in and flip to prepare for reading by making the buffer read from the start.
        
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW); //Upload the buffer to the GPU — this stores our objects's data on the graphics card.

        // Describe vertex layout: 2 floats per vertex with that being X and Y. 
        glVertexAttribPointer(0, dimensions, GL_FLOAT, false, 0, 0); //Tell GPU how to read the vertices
        glEnableVertexAttribArray(0); // Enable attribute slot 0

        // Unbind everything 
        glBindBuffer(GL_ARRAY_BUFFER, 0); //Unbind VBO
        glBindVertexArray(0); //Unbind VAO (finish config)
        
        //Free the CPU-side native memory given the GPU has its own copy now.
        MemoryUtil.memFree(vertexBuffer);
	}
	
	public void draw() {
        glBindVertexArray(vaoId); //Bind our VAO (activates our object layout)
        glEnableVertexAttribArray(0); //Enable vertex attribute at location 0
        glDrawArrays(GL_TRIANGLES, 0, vertices.length / dimensions);  
        glDisableVertexAttribArray(0); 
        glBindVertexArray(0); //Unbind VAO
    }

    public void cleanup() {
        glDeleteBuffers(vboId); // Delete VBO from GPU memory
        glDeleteVertexArrays(vaoId); // Delete VAO from GPU memory
    }

}
