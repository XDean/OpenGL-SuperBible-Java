package xdean.OpenGLSuperBible.chapter03;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import xdean.OpenGLSuperBible.share.BaseApp;

public class App3_11_Star extends BaseApp {
	public static void main(String[] args) {
		new App3_11_Star().setVisible(true);
	}

	private static final int MODE_SOLID = 0, MODE_LINE = 1, MODE_POINT = 2;

	int iMode = MODE_SOLID;
	boolean bEdgeFlag = true;
	int xRot = 0, yRot = 0;

	@Override
	protected void frameInit() {
		super.frameInit();
		int nModeMenu = glutCreateMenu(this::processMenu);
		glutAddMenuEntry("Solid", 1);
		glutAddMenuEntry("Outline", 2);
		glutAddMenuEntry("Points", 3);

		int nEdgeMenu = glutCreateMenu(this::processMenu);
		glutAddMenuEntry("On", 4);
		glutAddMenuEntry("Off", 5);

		glutCreateMenu(this::processMenu);
		glutAddSubMenu("Mode", nModeMenu);
		glutAddSubMenu("Edges", nEdgeMenu);
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

		glutPostRedisplay();
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

}
