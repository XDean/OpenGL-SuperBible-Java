package xdean.OpenGLSuperBible.chapter06;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;

/**
 * XXX: I don't know why it is so lag when maximize.
 * @author XDean
 *
 */
public class App6_04_MotionBlur extends BaseApp {
	public static void main(String[] args) {
		new App6_04_MotionBlur().setVisible(true);
	}

	protected FloatBuffer fLightPos = FloatBuffer.wrap(new float[] { -100.0f,
			100.0f, 50.0f, 1.0f });
	protected FloatBuffer fLightPosMirror = FloatBuffer.wrap(new float[] {
			fLightPos.get(0), -fLightPos.get(1), fLightPos.get(2),
			fLightPos.get(3) });
	protected FloatBuffer fNoLight = FloatBuffer.wrap(new float[] { 0.0f, 0.0f,
			0.0f, 0.0f });
	protected FloatBuffer fLowLight = FloatBuffer.wrap(new float[] { 0.25f,
			0.25f, 0.25f, 1.0f });
	protected FloatBuffer fBrightLight = FloatBuffer.wrap(new float[] { 1.0f,
			1.0f, 1.0f, 1.0f });

	protected float yRot;
	protected float yRotSnap;

	private void func(int i) {
		yRot += i;
		glut.glutPostRedisplay();
		glut.glutTimerFunc(3, this::func, i);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glut.glutTimerFunc(33, this::func, 3);

		gl.glClearColor(fLowLight.get(0), fLowLight.get(1), fLowLight.get(2),
				fLowLight.get(3));

		gl.glCullFace(GL2.GL_BACK);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, fNoLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fNoLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fBrightLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fBrightLight);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClearAccum(0, 0, 0, 0);
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT
				| GL2.GL_ACCUM_BUFFER_BIT);
		drawGround();
		
		float fPasses = 5.0f;
		yRotSnap = yRot;
		for (float fPass = 0.0f; fPass < fPasses; fPass += 1.0f) {
			yRotSnap -= 1.5f;
			DrawGeometry();

			if (fPass == 0.0f)
				gl.glAccum(GL2.GL_LOAD, 0.5f);
			else
				gl.glAccum(GL2.GL_ACCUM, 0.5f * (1.0f / fPasses));
		}

		gl.glAccum(GL2.GL_RETURN, 1.0f);
		gl.glFlush();
	}

	private void DrawGeometry() {
		gl.glPushMatrix();
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glTranslatef(0.0f, 0.5f, -3.5f);
		gl.glRotatef(-(yRotSnap * 2.0f), 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(1.0f, 0.0f, 0.0f);
		glut.glutSolidSphere(0.1f, 17, 9);
		gl.glPopMatrix();
	}

	protected void drawGround() {
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		float fExtent = 20.0f;
		float fStep = 0.5f;
		float y = 0.0f;
		float fColor;
		int iBounce = 0;

		gl.glShadeModel(GL2.GL_FLAT);
		for (float iStrip = -fExtent; iStrip <= fExtent; iStrip += fStep) {
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for (float iRun = fExtent; iRun >= -fExtent; iRun -= fStep) {
				if ((iBounce % 2) == 0)
					fColor = 1.0f;
				else
					fColor = 0.0f;
				gl.glColor4f(fColor, fColor, fColor, 0.5f);
				gl.glVertex3f(iStrip, y, iRun);
				gl.glVertex3f(iStrip + fStep, y, iRun);
				iBounce++;
			}
			gl.glEnd();
		}
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glDisable(GL.GL_BLEND);
		gl.glEnable(GL2.GL_LIGHTING);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
		gl.glTranslatef(0.0f, -0.4f, 0.0f);
	}

	@Override
	protected GLCapabilities getGLCapabilities() {
		GLCapabilities glCapabilities = super.getGLCapabilities();
		glCapabilities.setAccumAlphaBits(16);
		glCapabilities.setAccumRedBits(16);
		glCapabilities.setAccumGreenBits(16);
		glCapabilities.setAccumBlueBits(16);
		return glCapabilities;
	}
}
