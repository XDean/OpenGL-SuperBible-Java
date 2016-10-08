package xdean.OpenGLSuperBible.chapter10;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App10_02_Bezier extends BaseApp {

	public static void main(String[] args) {
		new App10_02_Bezier().setVisible(true);
	}

	int nNumPoints = 4;

	float ctrlPoints[] = { -4.0f, 0.0f, 0.0f, -6.0f, 4.0f, 0.0f, 6.0f, -4.0f,
			0.0f, 4.0f, 0.0f, 0.0f };

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glColor3f(0.0f, 0.0f, 1.0f);
	}

	void DrawPoints() {
		gl.glPointSize(5.0f);

		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < nNumPoints; i++)
			gl.glVertex2fv(ctrlPoints, 3 * i);
		gl.glEnd();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glMap1f(GL2.GL_MAP1_VERTEX_3, 0.0f, 100.0f, 3, nNumPoints,
				ctrlPoints, 0);
		gl.glEnable(GL2.GL_MAP1_VERTEX_3);
		
//		gl.glBegin(GL2.GL_LINE_STRIP);
//		for (int i = 0; i <= 100; i++) {
//			gl.glEvalCoord1f((float) i);
//		}
//		gl.glEnd();

		gl.glMapGrid1f(100, 0, 100);
		gl.glEvalMesh1(GL2.GL_LINE, 0, 100);
		
		DrawPoints();

		glut.glutSwapBuffers();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 10);
	}

}
