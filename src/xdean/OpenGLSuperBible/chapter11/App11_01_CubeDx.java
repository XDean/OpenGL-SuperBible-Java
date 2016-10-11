package xdean.OpenGLSuperBible.chapter11;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App11_01_CubeDx extends BaseApp {

	FloatBuffer corners = Buffers.newDirectFloatBuffer(new float[] { -25.0f,
			25.0f, 25.0f, 25.0f, 25.0f, 25.0f, 25.0f, -25.0f, 25.0f, -25.0f,
			-25.0f, 25.0f, -25.0f, 25.0f, -25.0f, 25.0f, 25.0f, -25.0f, 25.0f,
			-25.0f, -25.0f, -25.0f, -25.0f, -25.0f });

	ShortBuffer indexes = Buffers.newDirectShortBuffer(new short[] { 0, 1, 2,
			3, 4, 5, 1, 0, 3, 2, 6, 7, 5, 4, 7, 6, 1, 5, 6, 2, 4, 0, 3, 7 });

	float xRot = 0.0f;
	float yRot = 0.0f;

	public static void main(String[] args) {
		new App11_01_CubeDx().setVisible(true);
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
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 0);
		glut.glutSpecialFunc(this::specialKeys);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -200.0f);

		gl.glRotatef(yRot, 0.0f, 0.0f, 1.0f);
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);

		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, corners);

		gl.glDrawElements(GL2.GL_QUADS, 24, GL2.GL_UNSIGNED_SHORT, indexes);

		gl.glPopMatrix();

		glut.glutSwapBuffers();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1,
				1000);
	}

}
