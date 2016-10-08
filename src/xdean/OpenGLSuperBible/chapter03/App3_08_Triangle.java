package xdean.OpenGLSuperBible.chapter03;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_08_Triangle extends BaseApp {

	public static void main(String[] args) {
		new App3_08_Triangle().setVisible(true);
	}

	boolean iCull, iOutline, iDepth = false;
	float xRot = 75, yRot = 0, zRot = 0;
	int rotateGap = 33, rotateStep = 0;

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
		glut.glutPostRedisplay();
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutCreateMenu(this::processMenu);
		glut.glutAddMenuEntry("Toggle depth test", 1);
		glut.glutAddMenuEntry("Toggle cull backface", 2);
		glut.glutAddMenuEntry("Toggle outline back", 3);
		glut.glutAddMenuEntry("Toggle rotate", 4);
		glut.glutAttachMenu(GLUT_RIGHT_BUTTON);
	}

	private void processMenu(int value) {
		switch (value) {
		case 1:
			iDepth = !iDepth;
			break;
		case 2:
			iCull = !iCull;
			break;
		case 3:
			iOutline = !iOutline;
			break;
		case 4:
			rotateStep = rotateStep == 0 ? 1 : 0;
			break;
		default:
			break;
		}
		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0, 0, 0, 1);
		gl.glColor3f(0, 1, 0);
		gl.glShadeModel(GL2.GL_FLAT);
		// gl.glFrontFace(GL2.GL_CW);
		glut.glutTimerFunc(rotateGap, this::rotateFunc, rotateStep);
	}

	private void rotateFunc(int i) {
		zRot += i;
		glut.glutPostRedisplay();
		glut.glutTimerFunc(rotateGap, this::rotateFunc, rotateStep);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float x, y, angle;
		int iPivot = 1;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		if (iCull)
			gl.glEnable(GL2.GL_CULL_FACE);
		else
			gl.glDisable(GL2.GL_CULL_FACE);
		if (iDepth)
			gl.glEnable(GL2.GL_DEPTH_TEST);
		else
			gl.glDisable(GL2.GL_DEPTH_TEST);
		if (iOutline)
			gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);
		else
			gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1, 0, 0);
		gl.glRotatef(yRot, 0, 1, 0);
		gl.glRotatef(zRot, 0, 0, 1);

		// gl.glFrontFace(GL2.GL_CCW);
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex3f(0, 0, 75);
		for (angle = 0; angle < 2 * PI; angle += PI / 8, iPivot++) {
			x = 50 * (float) Math.sin(angle);
			y = 50 * (float) Math.cos(angle);
			if (iPivot % 2 == 0)
				gl.glColor3f(0, 1, 0);
			else
				gl.glColor3f(1, 0, 0);
			gl.glVertex2f(x, y);
		}
		gl.glEnd();
		// gl.glFrontFace(GL2.GL_CW);
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex3f(0, 0, 0);
		for (angle = 0; angle < 2 * PI; angle += PI / 8, iPivot++) {
			x = 50 * (float) Math.sin(angle);
			y = 50 * (float) Math.cos(angle);
			if (iPivot % 2 == 0)
				gl.glColor3f(0, 1, 0);
			else
				gl.glColor3f(1, 0, 0);
			gl.glVertex2f(x, y);
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
