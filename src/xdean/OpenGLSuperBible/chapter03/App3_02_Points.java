package xdean.OpenGLSuperBible.chapter03;

import xdean.OpenGLSuperBible.share.BaseApp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_02_Points extends BaseApp {

	public static void main(String[] args) {
		new App3_02_Points().setVisible(true);
	}

	private float xRot = 45, yRot = 45;

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glColor3f(0f, 1f, 0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float x, y, z, angle;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1f, 0f, 0f);
		gl.glRotatef(yRot, 0f, 1f, 0f);

		gl.glBegin(GL2.GL_POINTS);
		z = -50;
		for (angle = 0f; angle <= 6 * PI; angle += 0.1f) {
			x = (float) (50 * Math.sin(angle));
			y = (float) (50 * Math.cos(angle));
			gl.glVertex3f(x, y, z);
			z += 0.5f;
		}
		gl.glEnd();
		gl.glPopMatrix();
		gl.glFlush();
	}
}
