package xdean.OpenGLSuperBible.chapter04;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App4_01_Atom extends BaseApp {

	public static void main(String[] args) {
		new App4_01_Atom().setVisible(true);
	}

	float fElect1 = 0.0f;
	float xRot, yRot, zRot;

	@Override
	protected void frameInit() {
		super.frameInit();
		glutTimerFunc(33, this::timerFunc, 1);
	}

	private void timerFunc(int value) {
		xRot++;
		yRot += 2;
		zRot += 3;
		glcanvas.display();
		glutTimerFunc(33, this::timerFunc, value);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1, 0, 0);
		gl.glRotatef(yRot, 0, 1, 0);
		gl.glRotatef(zRot, 0, 0, 1);

		gl.glTranslatef(0.0f, 0.0f, -100.0f);

		gl.glColor3ub((byte) 255, (byte) 0, (byte) 0);
		glut.glutSolidSphere(10.0f, 15, 15);

		gl.glColor3ub((byte) 255, (byte) 255, (byte) 0);
		gl.glPushMatrix();
		gl.glRotatef(fElect1, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(90.0f, 0.0f, 0.0f);
		glut.glutSolidSphere(6.0f, 15, 15);
		gl.glPopMatrix();

		gl.glColor3ub((byte) 0, (byte) 255, (byte) 255);
		gl.glPushMatrix();
		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
		gl.glRotatef(fElect1, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(-70.0f, 0.0f, 0.0f);
		glut.glutSolidSphere(6.0f, 15, 15);
		gl.glPopMatrix();

		gl.glColor3ub((byte) 255, (byte) 0, (byte) 255);
		gl.glPushMatrix();
		gl.glRotatef(360.0f - 45.0f, 0.0f, 0.0f, 1.0f);
		gl.glRotatef(fElect1, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.0f, 60.0f);
		glut.glutSolidSphere(6.0f, 15, 15);
		gl.glPopMatrix();

		fElect1 += 10.0f;
		if (fElect1 > 360.0f)
			fElect1 = 0.0f;

		gl.glPopMatrix();
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 200);
	}
}
