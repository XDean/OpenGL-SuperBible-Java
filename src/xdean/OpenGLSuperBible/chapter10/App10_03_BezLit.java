package xdean.OpenGLSuperBible.chapter10;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App10_03_BezLit extends BaseApp {

	int nNumPoints = 3;

	float ctrlPoints[] = { -4.0f, 0.0f, 4.0f, -2.0f, 4.0f, 4.0f, 4.0f, 0.0f,
			4.0f, -4.0f, 0.0f, 0.0f, -2.0f, 4.0f, 0.0f, 4.0f, 0.0f, 0.0f,
			-4.0f, 0.0f, -4.0f, -2.0f, 4.0f, -4.0f, 4.0f, 0.0f, -4.0f };

	public static void main(String[] args) {
		new App10_03_BezLit().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		float ambientLight[] = { 0.3f, 0.3f, 0.3f, 1.0f };
		float diffuseLight[] = { 0.7f, 0.7f, 0.7f, 1.0f };
		float lightPos[] = { 20.0f, 0.0f, 0.0f, 0.0f };

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glEnable(GL2.GL_AUTO_NORMAL);

		gl.glColor3f(0.0f, 0.0f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();

		gl.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(60.0f, 1.0f, 0.0f, 0.0f);

		gl.glMap2f(GL2.GL_MAP2_VERTEX_3, 0.0f, 10.0f, 3, 3, 0.0f, 10.0f, 9, 3,
				ctrlPoints, 0);
		gl.glEnable(GL2.GL_MAP2_VERTEX_3);
		gl.glMapGrid2f(10, 0.0f, 10.0f, 10, 0.0f, 10.0f);
		gl.glEvalMesh2(GL2.GL_FILL, 0, 10, 0, 10);

		gl.glPopMatrix();
		glut.glutSwapBuffers();
	}

	void DrawPoints() {
		gl.glPointSize(5.0f);
		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < nNumPoints; i++)
			for (int j = 0; j < 3; j++)
				gl.glVertex3fv(ctrlPoints, i * 9 + j * 3);
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 10);
	}

}
