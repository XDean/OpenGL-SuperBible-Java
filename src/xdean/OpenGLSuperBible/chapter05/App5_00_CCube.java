package xdean.OpenGLSuperBible.chapter05;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App5_00_CCube extends BaseApp {
	public static void main(String[] args) {
		new App5_00_CCube().setVisible(true);
	}

	float xRot;
	float yRot;

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}
	
	@Override
	protected void specialKeys(int key, int x, int y) {
		if (key == GLUT_KEY_UP)
			xRot -= 5.0f;
		if (key == GLUT_KEY_DOWN)
			xRot += 5.0f;
		if (key == GLUT_KEY_LEFT)
			yRot += 5.0f;
		if (key == GLUT_KEY_RIGHT)
			yRot -= 5.0f;
		if (xRot > 356.0f)
			xRot = 0.0f;
		if (xRot < -1.0f)
			xRot = 355.0f;
		if (yRot > 356.0f)
			yRot = 0.0f;
		if (yRot < -1.0f)
			yRot = 355.0f;
		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_DITHER);
		gl.glShadeModel(GL2.GL_SMOOTH);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 255);
		gl.glVertex3f(50.0f, 50.0f, 50.0f);
		gl.glColor3ub((byte) 255, (byte) 255, (byte) 0);
		gl.glVertex3f(50.0f, -50.0f, 50.0f);
		gl.glColor3ub((byte) 255, (byte) 0, (byte) 0);
		gl.glVertex3f(-50.0f, -50.0f, 50.0f);
		gl.glColor3ub((byte) 255, (byte) 0, (byte) 255);
		gl.glVertex3f(-50.0f, 50.0f, 50.0f);
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(50.0f, 50.0f, -50.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(50.0f, -50.0f, -50.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(-50.0f, -50.0f, -50.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(-50.0f, 50.0f, -50.0f);
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(50.0f, 50.0f, -50.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(50.0f, 50.0f, 50.0f);
		gl.glColor3f(1.0f, 0.0f, 1.0f);
		gl.glVertex3f(-50.0f, 50.0f, 50.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(-50.0f, 50.0f, -50.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(50.0f, -50.0f, -50.0f);
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(50.0f, -50.0f, 50.0f);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(-50.0f, -50.0f, 50.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(-50.0f, -50.0f, -50.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(50.0f, 50.0f, 50.0f);
		gl.glColor3f(0.0f, 1.0f, 1.0f);
		gl.glVertex3f(50.0f, 50.0f, -50.0f);
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(50.0f, -50.0f, -50.0f);
		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(50.0f, -50.0f, 50.0f);
		gl.glColor3f(1.0f, 0.0f, 1.0f);
		gl.glVertex3f(-50.0f, 50.0f, 50.0f);
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(-50.0f, 50.0f, -50.0f);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(-50.0f, -50.0f, -50.0f);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(-50.0f, -50.0f, 50.0f);
		gl.glEnd();
		gl.glPopMatrix();
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 1000);
	    gl.glTranslatef(0.0f, 0.0f, -400.0f);
	}
}
