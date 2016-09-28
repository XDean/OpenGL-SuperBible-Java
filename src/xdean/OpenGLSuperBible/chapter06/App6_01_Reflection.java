package xdean.OpenGLSuperBible.chapter06;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.DefaultImpl;
import xdean.OpenGLSuperBible.share.GL.GLFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App6_01_Reflection extends BaseApp {

	public static void main(String[] args) {
		new App6_01_Reflection().setVisible(true);
	}

	FloatBuffer fLightPos = FloatBuffer.wrap(new float[] { -100.0f, 100.0f,
			50.0f, 1.0f });
	FloatBuffer fLightPosMirror = FloatBuffer.wrap(new float[] {
			fLightPos.get(0), -fLightPos.get(1), fLightPos.get(2),
			fLightPos.get(3) });
	FloatBuffer fNoLight = FloatBuffer.wrap(new float[] { 0.0f, 0.0f, 0.0f,
			0.0f });
	FloatBuffer fLowLight = FloatBuffer.wrap(new float[] { 0.25f, 0.25f, 0.25f,
			1.0f });
	FloatBuffer fBrightLight = FloatBuffer.wrap(new float[] { 1.0f, 1.0f, 1.0f,
			1.0f });

	protected GLFrame frameCamera;
	protected float yRot;

	private void timerFunction(int value) {
		yRot += 0.5f;
		glutPostRedisplay();
		glutTimerFunc(3, this::timerFunction, value);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glutTimerFunc(3, this::timerFunction, 1);
		
		frameCamera = new GLFrame(gl, glu);

		gl.glClearColor(fLowLight.get(0), fLowLight.get(1), fLowLight.get(2),
				fLowLight.get(3));

//		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		gl.glCullFace(GL2.GL_BACK);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, fNoLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fNoLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fBrightLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fBrightLight);

		gl.glEnable(GL2.GL_LIGHTING_BIT);
		gl.glEnable(GL2.GL_LIGHT0);

		// float[] planeEquation = new float[4];
		// Math3d.m3dGetPlaneEquation(planeEquation, new float[] { 0, -0.4f, 0
		// },
		// new float[] { 10, -0.4f, 0 }, new float[] { 5, -0.4f, -5 });
		// Math3d.m3dMakePlanarShadowMatrix(mShadowMatrix, planeEquation,
		// fLightPos.array());

		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		frameCamera.ApplyCameraTransform();

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPosMirror);
		gl.glPushMatrix();
		gl.glFrontFace(GL2.GL_CW);
		gl.glScalef(1f, -1f, 1f);
		drawWorld();
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPopMatrix();

		gl.glDisable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		drawGround();
		gl.glDisable(GL.GL_BLEND);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPos);
		drawWorld();
		gl.glPopMatrix();

		gl.glFlush();
	}

	protected void drawWorld() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.5f, -3.5f);

		gl.glPushMatrix();
		gl.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(1.0f, 0.0f, 0.0f);
		glut.glutSolidSphere(0.1f, 17, 9);
		gl.glPopMatrix();

		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		glut.glutSolidTorus(0.35, 0.15, 61, 37);

		gl.glPopMatrix();
	}

	protected void drawGround() {
		float fExtent = 20.0f;
		float fStep = 0.5f;
		float y = 0.0f;
		float fColor;
		int iBounce = 0;

		gl.glShadeModel(GL2.GL_FLAT);
		for (float iStrip = -fExtent; iStrip <= fExtent; iStrip += fStep) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for (float iRun = fExtent; iRun >= -fExtent; iRun -= fStep) {
				if ((iBounce % 2) == 0)
					fColor = 1.0f;
				else
					fColor = 0.0f;
				gl.glColor4f(fColor, fColor, fColor, 0.5f);
				gl.glVertex3f(iStrip, y, iRun);
				gl.glVertex3f(iStrip + fStep, y, iRun);
				iBounce++;
			}
			gl.glEnd();
		}
		gl.glShadeModel(GL2.GL_SMOOTH);
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		DefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
	    gl.glTranslatef(0.0f, -0.4f, 0.0f);
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	protected void specialKeys(int key, int x, int y) {

		if (key == GLUT_KEY_UP)
			frameCamera.MoveForward(0.1f);

		if (key == GLUT_KEY_DOWN)
			frameCamera.MoveForward(-0.1f);

		if (key == GLUT_KEY_LEFT)
			frameCamera.RotateLocalY(0.1f);

		if (key == GLUT_KEY_RIGHT)
			frameCamera.RotateLocalY(-0.1f);

		glutPostRedisplay();
	}
}
