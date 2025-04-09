package main;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
	
	public static void main(String[] args) {
		WindowManager window = new WindowManager("Minecraft Optimised", 1280, 720, false);
		
		window.init();
		
		//Game Loop
		while(window.shouldClose() == false) {
			window.update();
		}
		
		window.cleanUp();
	}

}
