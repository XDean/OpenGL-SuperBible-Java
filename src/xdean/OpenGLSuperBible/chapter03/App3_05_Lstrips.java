package xdean.OpenGLSuperBible.chapter03;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_05_Lstrips extends BaseApp {

	public static void main(String[] args) {
		new App3_05_Lstrips().setVisible(true);
	}

	private float xRot = 45, yRot = 45;

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
		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glColor3f(0f, 1f, 0f);
		glut.glutSpecialFunc(this::specialKeys);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float x, y, z, angle;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1f, 0f, 0f);
		gl.glRotatef(yRot, 0f, 1f, 0f);

		gl.glBegin(GL2.GL_LINE_STRIP);
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

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 100);
	}
}
