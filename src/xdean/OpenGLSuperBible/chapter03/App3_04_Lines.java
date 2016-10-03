package xdean.OpenGLSuperBible.chapter03;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_04_Lines extends BaseApp {

	public static void main(String[] args) {
		new App3_04_Lines().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0, 0, 0, 1);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float x, y, z, angle;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glBegin(GL2.GL_LINES);
		z = 0;
		for (angle = 0f; angle <= PI; angle += PI / 10) {
			x = (float) (50 * Math.sin(angle));
			y = (float) (50 * Math.cos(angle));
			gl.glVertex3f(x, y, z);
			x = (float) (50 * Math.sin(angle + PI));
			y = (float) (50 * Math.cos(angle + PI));
			gl.glVertex3f(x, y, z);
		}
		gl.glEnd();
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 100);
	}
}
