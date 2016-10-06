package xdean.OpenGLSuperBible.chapter03;

import java.nio.ByteBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.Util;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_10_PStipple extends BaseApp {

	public static void main(String[] args) {
		new App3_10_PStipple().setVisible(true);
	}

	private static ByteBuffer fire = ByteBuffer.wrap(Util
			.intArrayToByteArray(new int[] { 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
					0x00, 0x00, 0xc0, 0x00, 0x00, 0x01, 0xf0, 0x00, 0x00, 0x07,
					0xf0, 0x0f, 0x00, 0x1f, 0xe0, 0x1f, 0x80, 0x1f, 0xc0, 0x0f,
					0xc0, 0x3f, 0x80, 0x07, 0xe0, 0x7e, 0x00, 0x03, 0xf0, 0xff,
					0x80, 0x03, 0xf5, 0xff, 0xe0, 0x07, 0xfd, 0xff, 0xf8, 0x1f,
					0xfc, 0xff, 0xe8, 0xff, 0xe3, 0xbf, 0x70, 0xde, 0x80, 0xb7,
					0x00, 0x71, 0x10, 0x4a, 0x80, 0x03, 0x10, 0x4e, 0x40, 0x02,
					0x88, 0x8c, 0x20, 0x05, 0x05, 0x04, 0x40, 0x02, 0x82, 0x14,
					0x40, 0x02, 0x40, 0x10, 0x80, 0x02, 0x64, 0x1a, 0x80, 0x00,
					0x92, 0x29, 0x00, 0x00, 0xb0, 0x48, 0x00, 0x00, 0xc8, 0x90,
					0x00, 0x00, 0x85, 0x10, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00,
					0x00, 0x10, 0x00 }));

	private float xRot = 30, yRot = 30;

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
		glutPostRedisplay();
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glEnable(GL2.GL_POLYGON_STIPPLE);
		gl.glPolygonStipple(fire);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex2f(-20.0f, 50.0f);
		gl.glVertex2f(20.0f, 50.0f);
		gl.glVertex2f(50.0f, 20.0f);
		gl.glVertex2f(50.0f, -20.0f);
		gl.glVertex2f(20.0f, -50.0f);
		gl.glVertex2f(-20.0f, -50.0f);
		gl.glVertex2f(-50.0f, -20.0f);
		gl.glVertex2f(-50.0f, 20.0f);
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
