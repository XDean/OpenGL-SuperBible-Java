package xdean.OpenGLSuperBible.chapter02;

import xdean.OpenGLSuperBible.share.BaseApp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App2_01_Simple extends BaseApp {

	public static void main(String[] args) {
		new App2_01_Simple().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 1f, 1f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

	}

}
