package xdean.OpenGLSuperBible.chapter03;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import xdean.OpenGLSuperBible.share.BaseApp;

/**
 * It will be blinking because the GLCanvas don't support single buffer.
 * 
 * @author XDean
 *
 */
public class App3_12_Single extends BaseApp {
	public static void main(String[] args) {
		new App3_12_Single().setVisible(true);
	}

	double dRadius = 0.1;
	double dAngle = 0.0;

	private void timerFunction(int value) {
		glutPostRedisplay();
		glutTimerFunc(50, this::timerFunction, 0);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		glutTimerFunc(50, this::timerFunction, 0);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);

		if (dAngle == 0.0 || dAngle == 0.2)
			gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glBegin(GL2.GL_POINTS);
		gl.glVertex2d(dRadius * Math.cos(dAngle), dRadius * Math.sin(dAngle));
		gl.glEnd();

		dRadius *= 1.02;
		dAngle += 0.2;

		if (dAngle > 30.0) {
			dRadius = 0.1;
			dAngle = 0.0;
		}
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		GL2 gl = drawable.getGL().getGL2();
		if (h == 0)
			h = 1;
		gl.glViewport(0, 0, w, h);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		gl.glOrtho(-4.0, 4.0, -3.0, 3.0, -10, 10);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
