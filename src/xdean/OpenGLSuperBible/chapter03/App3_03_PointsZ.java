package xdean.OpenGLSuperBible.chapter03;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_03_PointsZ extends BaseApp {

	public static void main(String[] args) {
		new App3_03_PointsZ().setVisible(true);
	}

	private float xRot = 60, yRot = 60;

	@Override
	protected void specialKeys(int key, int x, int y) {
		switch (key) {
		case GLUT_KEY_UP:
			xRot -= 5;
			break;
		case GLUT_KEY_DOWN:
			xRot += 5;
			break;
		case GLUT_KEY_LEFT:
			yRot -= 5;
			break;
		case GLUT_KEY_RIGHT:
			yRot += 5;
			break;
		}
		glutPostRedisplay();
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glColor3f(0f, 1f, 0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float x, y, z, angle, curSize;
		FloatBuffer sizes = FloatBuffer.allocate(2);
		FloatBuffer step = FloatBuffer.allocate(1);

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glGetFloatv(GL2.GL_POINT_SIZE_RANGE, sizes);
		gl.glGetFloatv(GL2.GL_POINT_SIZE_GRANULARITY, step);

		curSize = sizes.get(0);

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1f, 0f, 0f);
		gl.glRotatef(yRot, 0f, 1f, 0f);

		z = -50;
		for (angle = 0f; angle <= 6 * PI; angle += 0.1f, curSize += step.get(0)) {
			x = (float) (50 * Math.sin(angle));
			y = (float) (50 * Math.cos(angle));
			gl.glPointSize(curSize);
			gl.glBegin(GL2.GL_POINTS);
			gl.glVertex3f(x, y, z);
			gl.glEnd();
			z += 0.5f;
		}
		gl.glPopMatrix();
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 100);
	}
}
