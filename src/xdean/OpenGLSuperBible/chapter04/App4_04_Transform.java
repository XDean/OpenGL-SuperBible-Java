package xdean.OpenGLSuperBible.chapter04;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App4_04_Transform extends BaseApp {

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0.0f, 0.0f, .50f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
	}
}
