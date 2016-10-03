package xdean.OpenGLSuperBible.chapter04;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App4_03_Solar extends BaseApp {

	public static void main(String[] args) {
		new App4_03_Solar().setVisible(true);
	}

	FloatBuffer whiteLight = FloatBuffer.wrap(new float[] { 0.2f, 0.2f, 0.2f,
			1.0f });
	FloatBuffer sourceLight = FloatBuffer.wrap(new float[] { 0.8f, 0.8f, 0.8f,
			1.0f });
	FloatBuffer lightPos = FloatBuffer.wrap(new float[] { 0.0f, 0.0f, 0.0f,
			1.0f });
	float fMoonRot, fEarthRot;

	@Override
	protected void frameInit() {
		super.frameInit();
		glutTimerFunc(22, this::timerFunc, 1);
	}

	private void timerFunc(int value) {
		glcanvas.display();
		glutTimerFunc(22, this::timerFunc, value);
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);

		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, whiteLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, sourceLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -300);

		gl.glColor3ub((byte) 255, (byte) 255, (byte) 0);
		gl.glDisable(GL2.GL_LIGHTING);
		glut.glutSolidSphere(15, 15, 15);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, whiteLight);

		gl.glRotatef(fEarthRot, 0, 1, 0);

		gl.glColor3ub((byte) 0, (byte) 0, (byte) 255);
		gl.glTranslatef(105, 0, 0);
		gl.glRotatef(23.26f, 1, 0, 0);
		glut.glutSolidSphere(15, 15, 15);

		gl.glColor3ub((byte) 200, (byte) 200, (byte) 200);
		gl.glRotatef(fMoonRot, 0, 1, 0);
		gl.glTranslatef(30, 0, 0);
		glut.glutSolidSphere(6, 15, 15);
		gl.glPopMatrix();

		gl.glFlush();

		fEarthRot += 1;
		if (fEarthRot > 360)
			fEarthRot = 0;

		fMoonRot += 12;
		if (fMoonRot > 360)
			fMoonRot = 0;

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 50, 1, 425);
	}
}
