package xdean.OpenGLSuperBible.chapter10;

import jogamp.opengl.glu.gl2.nurbs.GLUgl2nurbsImpl;
import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 * XXX: JOGL has not implement these NURB functions. So there are two samples
 * Nurbt and Nurbc absent.
 * 
 * @author XDean
 *
 */
public class App10_04_Nurbs extends BaseApp {

	public static void main(String[] args) {
		new App10_04_Nurbs().setVisible(true);
	}

	GLUgl2nurbsImpl pNurb;

	int nNumPoints = 4;
	float ctrlPoints[] = { -6.0f, -6.0f, 0.0f, -6.0f, -2.0f, 0.0f, -6.0f, 2.0f,
			0.0f, -6.0f, 6.0f, 0.0f, -2.0f, -6.0f, 0.0f, -2.0f, -2.0f, 8.0f,
			-2.0f, 2.0f, 8.0f, -2.0f, 6.0f, 0.0f, 2.0f, -6.0f, 0.0f, 2.0f,
			-2.0f, 8.0f, 2.0f, 2.0f, 8.0f, 2.0f, 6.0f, 0.0f, 6.0f, -6.0f, 0.0f,
			6.0f, -2.0f, 0.0f, 6.0f, 2.0f, 0.0f, 6.0f, 6.0f, 0.0f };
	float Knots[] = { 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f };

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		float specular[] = { 0.7f, 0.7f, 0.7f, 1.0f };
		float shine[] = { 100.0f };

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, shine, 0);

		gl.glEnable(GL2.GL_AUTO_NORMAL);

		pNurb = (GLUgl2nurbsImpl) glu.gluNewNurbsRenderer();

		// pNurb.gluNurbsCallback(pNurb, GLU.GLU_ERROR, (CallBack)
		// NurbsErrorHandler);

		// pNurb.setnurbsproperty(?, NurbsConsts.N_SAMPLING_TOLERANCE, 25);
		// glu.gluNurbsProperty(pNurb, GLU.GLU_SAMPLING_TOLERANCE, 25.0f);
		// glu.gluNurbsProperty(pNurb, GLU.GLU_DISPLAY_MODE, (float)
		// GLU.GLU_FILL);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glColor3ub((byte) 0, (byte) 0, (byte) 220);

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();

		gl.glRotatef(330.0f, 1.0f, 0.0f, 0.0f);

		glu.gluBeginSurface(pNurb);
		glu.gluNurbsSurface(pNurb, 8, Knots, 8, Knots, 4 * 3, 3, ctrlPoints, 4,
				4, GL2.GL_MAP2_VERTEX_3);
		glu.gluEndSurface(pNurb);

		DrawPoints();

		gl.glPopMatrix();

		glut.glutSwapBuffers();
	}

	void DrawPoints() {
		gl.glPointSize(5.0f);
		gl.glColor3ub((byte) 255, (byte) 0, (byte) 0);

		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				gl.glVertex3fv(ctrlPoints, i * 12 + j * 3);
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 45, 1, 40);
		gl.glTranslatef(0.0f, 0.0f, -20.0f);

	}

}
