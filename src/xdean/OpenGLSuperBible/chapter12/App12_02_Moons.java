package xdean.OpenGLSuperBible.chapter12;

import java.nio.IntBuffer;
import java.util.Arrays;

import jogamp.opengl.glu.GLUquadricImpl;
import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public class App12_02_Moons extends BaseApp {

	public static void main(String[] args) {
		new App12_02_Moons().setVisible(true);
	}

	private static final int EARTH = 1;
	private static final int MARS = 2;
	private static final int MOON1 = 3;
	private static final int MOON2 = 4;

	private static final int BUFFER_LENGTH = 64;

	void MouseCallback(int button, int state, int x, int y) {
		if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN)
			ProcessSelection(x, y);
	}

	void ProcessSelection(int xPos, int yPos) {
		float fAspect;
		IntBuffer selectBuff = Buffers.newDirectIntBuffer(BUFFER_LENGTH);

		int hits;
		IntBuffer viewport = IntBuffer.allocate(4);

		gl.glSelectBuffer(BUFFER_LENGTH, selectBuff);

		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();

		gl.glRenderMode(GL2.GL_SELECT);

		gl.glLoadIdentity();
		glu.gluPickMatrix(xPos, viewport.get(3) - yPos + viewport.get(1), 2, 2,
				viewport);

		fAspect = (float) viewport.get(2) / (float) viewport.get(3);
		glu.gluPerspective(45.0f, fAspect, 1.0, 425.0);

		glut.glutPostRedisplay(false);

		hits = gl.glRenderMode(GL2.GL_RENDER);

		if (hits == 1)
			ProcessPlanet(selectBuff);
		else
			glut.glutSetWindowTitle("You clicked empty space!");

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();

		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	void ProcessPlanet(IntBuffer pSelectBuff) {
		int id, count;
		String cMessage;
		cMessage = "Error, no selection detected";

		count = pSelectBuff.get(0);

		id = pSelectBuff.get(3);
		int dst[] = new int[64];
		pSelectBuff.get(dst);
		System.out.println(Arrays.toString(dst));
		switch (id) {
		case EARTH:
			cMessage = "You clicked Earth.";
			if (count == 2)
				cMessage += " - Specifically the moon.";
			break;
		case MARS:
			cMessage = "You clicked Mars.";
			if (count == 2) {
				if (pSelectBuff.get(4) == MOON1)
					cMessage += " - Specifically Moon #1.";
				else
					cMessage += " - Specifically Moon #2.";
			}
			break;
		}

		glut.glutSetWindowTitle(cMessage);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glut.glutMouseFunc(this::MouseCallback);

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

		gl.glPushMatrix();
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glTranslatef(-100.0f, 0.0f, 0.0f);
		gl.glLoadName(EARTH);
		DrawSphere(30.0f);

		gl.glTranslatef(45.0f, 0.0f, 0.0f);
		gl.glColor3f(0.85f, 0.85f, 0.85f);
		gl.glPushName(MOON1);
		DrawSphere(5.0f);
		gl.glPopName();
		gl.glPopMatrix();

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glPushMatrix();
		gl.glTranslatef(100.0f, 0.0f, 0.0f);
		gl.glLoadName(MARS);
		DrawSphere(20.0f);

		gl.glTranslatef(-40.0f, 40.0f, 0.0f);
		gl.glColor3f(0.85f, 0.85f, 0.85f);
		gl.glPushName(MOON1);
		DrawSphere(5.0f);
		gl.glPopName();

		gl.glTranslatef(0.0f, -80.0f, 0.0f);
		gl.glPushName(MOON2);
		DrawSphere(5.0f);
		gl.glPopName();
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
