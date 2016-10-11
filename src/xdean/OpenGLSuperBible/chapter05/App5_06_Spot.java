package xdean.OpenGLSuperBible.chapter05;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App5_06_Spot extends BaseApp {

	public static void main(String[] args) {
		new App5_06_Spot().setVisible(true);
	}

	float xRot = 0.0f;
	float yRot = 0.0f;

	FloatBuffer lightPos = FloatBuffer.wrap(new float[] { 0.0f, 0.0f, 75.0f,
			1.0f });
	FloatBuffer specular = FloatBuffer.wrap(new float[] { 1.0f, 1.0f, 1.0f,
			1.0f });
	FloatBuffer specref = FloatBuffer
			.wrap(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
	FloatBuffer ambientLight = FloatBuffer.wrap(new float[] { 0.5f, 0.5f, 0.5f,
			1.0f });
	FloatBuffer spotDir = FloatBuffer.wrap(new float[] { 0.0f, 0.0f, -1.0f });

	private static final int MODE_FLAT = 1;
	private static final int MODE_SMOOTH = 2;
	private static final int MODE_VERYLOW = 3;
	private static final int MODE_MEDIUM = 4;
	private static final int MODE_VERYHIGH = 5;

	int iShade = MODE_SMOOTH;
	int iTess = MODE_VERYHIGH;

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutCreateMenu(this::ProcessMenu);
		glut.glutAddMenuEntry("Flat Shading", 1);
		glut.glutAddMenuEntry("Smooth Shading", 2);
		glut.glutAddMenuEntry("VL Tess", 3);
		glut.glutAddMenuEntry("MD Tess", 4);
		glut.glutAddMenuEntry("VH Tess", 5);
		glut.glutAttachMenu(GLUT_RIGHT_BUTTON);
		glut.glutSpecialFunc(this::specialKeys);
	}

	void ProcessMenu(int value) {
		switch (value) {
		case 1:
			iShade = MODE_FLAT;
			break;
		case 2:
			iShade = MODE_SMOOTH;
			break;
		case 3:
			iTess = MODE_VERYLOW;
			break;
		case 4:
			iTess = MODE_MEDIUM;
			break;
		case 5:
		default:
			iTess = MODE_VERYHIGH;
			break;
		}
		glut.glutPostRedisplay();
	}

	protected void specialKeys(int key, int x, int y) {
		if (key == GLUT_KEY_UP)
			xRot -= 5.0f;
		if (key == GLUT_KEY_DOWN)
			xRot += 5.0f;
		if (key == GLUT_KEY_LEFT)
			yRot -= 5.0f;
		if (key == GLUT_KEY_RIGHT)
			yRot += 5.0f;
		if (key > 356.0f)
			xRot = 0.0f;
		if (key < -1.0f)
			xRot = 355.0f;
		if (key > 356.0f)
			yRot = 0.0f;
		if (key < -1.0f)
			yRot = 355.0f;
		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, ambientLight);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, ambientLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);

		gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, 50.0f);

		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specref);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if (iShade == MODE_FLAT)
			gl.glShadeModel(GL2.GL_FLAT);
		else
			gl.glShadeModel(GL2.GL_SMOOTH);

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, spotDir);

		gl.glColor3ub((byte) 255, (byte) 0, (byte) 0);

		gl.glTranslatef(lightPos.get(0), lightPos.get(1), lightPos.get(2));
		glut.glutSolidCone(4.0f, 6.0f, 15, 15);

		gl.glPushAttrib(GL2.GL_LIGHTING_BIT);

		gl.glDisable(GL2.GL_LIGHTING);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 0);
		glut.glutSolidSphere(3.0f, 15, 15);

		gl.glPopAttrib();

		gl.glPopMatrix();

		gl.glColor3ub((byte) 0, (byte) 0, (byte) 255);

		if (iTess == MODE_VERYLOW)
			glut.glutSolidSphere(30.0f, 7, 7);
		else if (iTess == MODE_MEDIUM)
			glut.glutSolidSphere(30.0f, 15, 15);
		else
			glut.glutSolidSphere(30.0f, 50, 50);

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 500);
		gl.glTranslatef(0.0f, 0.0f, -250.0f);
		// DefaultImpl.reshapeAtRadio(drawable, width, height, -100);
	}
}
