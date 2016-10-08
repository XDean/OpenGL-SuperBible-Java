package xdean.OpenGLSuperBible.chapter03;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

public class App3_11_Star extends BaseApp {
	public static void main(String[] args) {
		new App3_11_Star().setVisible(true);
	}

	private static final int MODE_SOLID = 0, MODE_LINE = 1, MODE_POINT = 2;

	int iMode = MODE_SOLID;
	boolean bEdgeFlag = true;
	int xRot = 0, yRot = 0;

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
		int nModeMenu = glut.glutCreateMenu(this::processMenu);
		glut.glutAddMenuEntry("Solid", 1);
		glut.glutAddMenuEntry("Outline", 2);
		glut.glutAddMenuEntry("Points", 3);

		int nEdgeMenu = glut.glutCreateMenu(this::processMenu);
		glut.glutAddMenuEntry("On", 4);
		glut.glutAddMenuEntry("Off", 5);

		glut.glutCreateMenu(this::processMenu);
		glut.glutAddSubMenu("Mode", nModeMenu);
		glut.glutAddSubMenu("Edges", nEdgeMenu);
	}

	private void processMenu(int value) {
		switch (value) {
		case 1:
			iMode = MODE_SOLID;
			break;

		case 2:
			iMode = MODE_LINE;
			break;

		case 3:
			iMode = MODE_POINT;
			break;

		case 4:
			bEdgeFlag = true;
			break;

		case 5:
		default:
			bEdgeFlag = false;
			break;
		}

		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		if (iMode == MODE_LINE)
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

		if (iMode == MODE_POINT)
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_POINT);

		if (iMode == MODE_SOLID)
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		gl.glBegin(GL2.GL_TRIANGLES);

		gl.glEdgeFlag(bEdgeFlag);
		gl.glVertex2f(-20.0f, 0.0f);
		gl.glEdgeFlag(true);
		gl.glVertex2f(20.0f, 0.0f);
		gl.glVertex2f(0.0f, 40.0f);

		gl.glVertex2f(-20.0f, 0.0f);
		gl.glVertex2f(-60.0f, -20.0f);
		gl.glEdgeFlag(bEdgeFlag);
		gl.glVertex2f(-20.0f, -40.0f);
		gl.glEdgeFlag(true);

		gl.glVertex2f(-20.0f, -40.0f);
		gl.glVertex2f(0.0f, -80.0f);
		gl.glEdgeFlag(bEdgeFlag);
		gl.glVertex2f(20.0f, -40.0f);
		gl.glEdgeFlag(true);

		gl.glVertex2f(20.0f, -40.0f);
		gl.glVertex2f(60.0f, -20.0f);
		gl.glEdgeFlag(bEdgeFlag);
		gl.glVertex2f(20.0f, 0.0f);
		gl.glEdgeFlag(true);

		gl.glEdgeFlag(bEdgeFlag);
		gl.glVertex2f(-20.0f, 0.0f);
		gl.glVertex2f(-20.0f, -40.0f);
		gl.glVertex2f(20.0f, 0.0f);

		gl.glVertex2f(-20.0f, -40.0f);
		gl.glVertex2f(20.0f, -40.0f);
		gl.glVertex2f(20.0f, 0.0f);
		gl.glEdgeFlag(true);

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
