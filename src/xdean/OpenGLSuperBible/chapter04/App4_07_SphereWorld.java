package xdean.OpenGLSuperBible.chapter04;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.GL.GLFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App4_07_SphereWorld extends BaseApp {

	public static void main(String[] args) {
		new App4_07_SphereWorld().setVisible(true);
	}

	protected static final int NUM_SPHERES = 50;

	protected GLFrame spheres[];
	protected GLFrame frameCamera;

	protected float yRot;

	private void timerFunction(int value) {
		yRot += 0.5f;
		glut.glutPostRedisplay();
		glut.glutTimerFunc(3, this::timerFunction, value);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glut.glutTimerFunc(3, this::timerFunction, 1);

		spheres = new GLFrame[NUM_SPHERES];
		frameCamera = new GLFrame(gl, glu);

		gl.glClearColor(0.0f, 0.0f, .50f, 1.0f);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);

		for (int sphere = 0; sphere < NUM_SPHERES; sphere++) {
			float x = (float) ((Math.random() * 40) - 20);
			float z = (float) ((Math.random() * 40) - 20);
			spheres[sphere] = new GLFrame(gl, glu);
			spheres[sphere].setOrigin(x, 0.0f, z);
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		drawWorld();
		gl.glFlush();
	}

	protected void drawWorld() {
		gl.glPushMatrix();
		frameCamera.ApplyCameraTransform();

		drawGround();

		drawSpheres();

		gl.glPopMatrix();
	}
	
	protected void drawCenter(){
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -2.5f);

		gl.glPushMatrix();
		gl.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(1.0f, 0.0f, 0.0f);
		glut.glutSolidSphere(0.1f, 13, 26);
		gl.glPopMatrix();

		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		glt.gltDrawTorus(0.35f, 0.05f, 40, 20);
		gl.glPopMatrix();
	}

	protected void drawSpheres() {
		for (int i = 0; i < NUM_SPHERES; i++) {
			gl.glPushMatrix();
			spheres[i].ApplyActorTransform();
			gl.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
			glut.glutSolidSphere(0.1f, 13, 26);
			gl.glPopMatrix();
		}
		
		drawCenter();
	}

	protected void drawGround() {
		float extent = 20.0f;
		float step = 1.0f;
		float y = -0.4f;
		int line;

		gl.glBegin(GL2.GL_LINES);
		for (line = (int) -extent; line <= extent; line += step) {
			gl.glVertex3f(line, y, extent);
			gl.glVertex3f(line, y, -extent);

			gl.glVertex3f(extent, y, line);
			gl.glVertex3f(-extent, y, line);
		}

		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
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

		glut.glutPostRedisplay();
	}
}
