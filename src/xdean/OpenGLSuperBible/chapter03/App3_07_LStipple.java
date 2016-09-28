package xdean.OpenGLSuperBible.chapter03;

import xdean.OpenGLSuperBible.share.BaseApp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_07_LStipple extends BaseApp {

	public static void main(String[] args) {
		new App3_07_LStipple().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0, 0, 0, 1);
		gl.glColor3f(0f, 1f, 0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float y;
		int factor = 1;
		short pattern = 0x5555;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glEnable(GL2.GL_LINE_STIPPLE);

		for (y = -90; y < 90; y += 20, factor++) {
			gl.glLineStipple(factor, pattern);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2f(-80, y);
			gl.glVertex2f(80, y);
			gl.glEnd();
		}
		gl.glFlush();
	}
}
