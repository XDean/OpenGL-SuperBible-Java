package xdean.OpenGLSuperBible.chapter12;

import java.nio.IntBuffer;

import jogamp.opengl.glu.GLUquadricImpl;
import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public class App12_01_Planets extends BaseApp {

	public static void main(String[] args) {
		new App12_01_Planets().setVisible(true);
	}

	private static final int SUN = 1;
	private static final int MERCURY = 2;
	private static final int VENUS = 3;
	private static final int EARTH = 4;
	private static final int MARS = 5;

	private static final int BUFFER_LENGTH = 64;

	IntBuffer selectBuff = Buffers.newDirectIntBuffer(BUFFER_LENGTH);

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutMouseFunc(this::MouseCallback);
	}

	void MouseCallback(int button, int state, int x, int y) {
		if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN)
			ProcessSelection(x, y);
	}

	void ProcessSelection(int xPos, int yPos) {
		float fAspect;

		int hits;
		IntBuffer viewport = IntBuffer.allocate(4);

		gl.glSelectBuffer(BUFFER_LENGTH, selectBuff);
		
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();

		gl.glRenderMode(GL2.GL_SELECT);

		gl.glLoadIdentity();
		glu.gluPickMatrix(xPos, viewport.get(1) + viewport.get(3) - yPos, 2, 2,
				viewport);

		fAspect = (float) viewport.get(2) / (float) viewport.get(3);
		glu.gluPerspective(45.0f, fAspect, 1.0, 425.0);

		glut.glutPostRedisplay(false);

		hits = gl.glRenderMode(GL2.GL_RENDER);
		if (hits == 1)
			ProcessPlanet(selectBuff.get(3));
		else
			glut.glutSetWindowTitle("Nothing was clicked on!");

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	void ProcessPlanet(int id) {
		switch (id) {
		case SUN:
			glut.glutSetWindowTitle("You clicked on the Sun!");
			break;

		case MERCURY:
			glut.glutSetWindowTitle("You clicked on Mercury!");
			break;

		case VENUS:
			glut.glutSetWindowTitle("You clicked on Venus!");
			break;

		case EARTH:
			glut.glutSetWindowTitle("You clicked on Earth!");
			break;

		case MARS:
			glut.glutSetWindowTitle("You clicked on Mars!");
			break;

		default:
			glut.glutSetWindowTitle("Nothing was clicked on!");
			break;
		}
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		float dimLight[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		float sourceLight[] = { 0.65f, 0.65f, 0.65f, 1.0f };
		float lightPos[] = { 0.0f, 0.0f, 0.0f, 1.0f };

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, dimLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, sourceLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glClearColor(0.60f, 0.60f, 0.60f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();

		gl.glTranslatef(0.0f, 0.0f, -300.0f);

		gl.glInitNames();
		gl.glPushName(0);

		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glLoadName(SUN);
		DrawSphere(15.0f);

		gl.glColor3f(0.5f, 0.0f, 0.0f);
		gl.glPushMatrix();
		gl.glTranslatef(24.0f, 0.0f, 0.0f);
		gl.glLoadName(MERCURY);
		DrawSphere(2.0f);
		gl.glPopMatrix();

		gl.glColor3f(0.5f, 0.5f, 1.0f);
		gl.glPushMatrix();
		gl.glTranslatef(60.0f, 0.0f, 0.0f);
		gl.glLoadName(VENUS);
		DrawSphere(4.0f);
		gl.glPopMatrix();

		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glPushMatrix();
		gl.glTranslatef(100.0f, 0.0f, 0.0f);
		gl.glLoadName(EARTH);
		DrawSphere(8.0f);
		gl.glPopMatrix();

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glPushMatrix();
		gl.glTranslatef(150.0f, 0.0f, 0.0f);
		gl.glLoadName(MARS);
		DrawSphere(4.0f);
		gl.glPopMatrix();

		gl.glPopMatrix();
		glut.glutSwapBuffers();
	}

	void DrawSphere(float radius) {
		GLUquadricImpl pObj;
		pObj = (GLUquadricImpl) glu.gluNewQuadric();
		glu.gluQuadricNormals(pObj, GLU.GLU_SMOOTH);
		glu.gluSphere(pObj, radius, 26, 13);
		glu.gluDeleteQuadric(pObj);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl
				.reshapWithPerspective(drawable, width, height, 45, 1, 425);
	}

}
