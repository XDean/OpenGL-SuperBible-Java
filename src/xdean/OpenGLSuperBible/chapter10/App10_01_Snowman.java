package xdean.OpenGLSuperBible.chapter10;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

public class App10_01_Snowman extends BaseApp {

	public static void main(String[] args) {
		new App10_01_Snowman().setVisible(true);
	}

	private float xRot, yRot;

	protected void specialKeys(int key, int x, int y) {
		if (key == GLUT_KEY_UP)
			xRot -= 5.0f;
		if (key == GLUT_KEY_DOWN)
			xRot += 5.0f;
		if (key == GLUT_KEY_LEFT)
			yRot -= 5.0f;
		if (key == GLUT_KEY_RIGHT)
			yRot += 5.0f;
		xRot = (float) (xRot % 360);
		yRot = (float) (yRot % 360);

		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		
		glut.glutSpecialFunc(this::specialKeys);
		
		float whiteLight[] = { 0.05f, 0.05f, 0.05f, 1.0f };
		float sourceLight[] = { 0.25f, 0.25f, 0.25f, 1.0f };
		float lightPos[] = { -10.f, 5.0f, 5.0f, 1.0f };

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, whiteLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, sourceLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, sourceLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glClearColor(0.25f, 0.25f, 0.50f, 1.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GLUquadric pObj;
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, -1.0f, -5.0f);
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		pObj = glu.gluNewQuadric();
		glu.gluQuadricNormals(pObj, GLU.GLU_SMOOTH);

		gl.glPushMatrix();
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		glu.gluSphere(pObj, .40f, 26, 13);
		gl.glTranslatef(0.0f, .550f, 0.0f);
		glu.gluSphere(pObj, .3f, 26, 13);

		gl.glTranslatef(0.0f, 0.45f, 0.0f);
		glu.gluSphere(pObj, 0.24f, 26, 13);

		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.1f, 0.1f, 0.21f);
		glu.gluSphere(pObj, 0.02f, 26, 13);

		gl.glTranslatef(-0.2f, 0.0f, 0.0f);
		glu.gluSphere(pObj, 0.02f, 26, 13);

		gl.glColor3f(1.0f, 0.3f, 0.3f);
		gl.glTranslatef(0.1f, -0.12f, 0.0f);
		glu.gluCylinder(pObj, 0.04f, 0.0f, 0.3f, 26, 13);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, 1.17f, 0.0f);
		gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		glu.gluCylinder(pObj, 0.17f, 0.17f, 0.4f, 26, 13);

		gl.glDisable(GL2.GL_CULL_FACE);
		glu.gluDisk(pObj, 0.17f, 0.28f, 26, 13);
		gl.glEnable(GL2.GL_CULL_FACE);

		gl.glTranslatef(0.0f, 0.0f, 0.40f);
		glu.gluDisk(pObj, 0.0f, 0.17f, 26, 13);
		gl.glPopMatrix();

		gl.glPopMatrix();
		glut.glutSwapBuffers();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 40);
	}

}
