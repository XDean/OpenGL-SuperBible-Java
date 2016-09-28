package xdean.OpenGLSuperBible.chapter02;

import xdean.OpenGLSuperBible.share.BaseApp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App2_02_Glrect extends BaseApp{
	
	public static void main(String[] args) {
		new App2_02_Glrect().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 1f, 1f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glColor3f(1f, 0f, 0f);
		gl.glRectf(-25f, 25f, 25f, -25f);
		gl.glFlush();
	}
}
