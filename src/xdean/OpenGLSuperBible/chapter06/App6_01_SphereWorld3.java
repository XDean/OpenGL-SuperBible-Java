package xdean.OpenGLSuperBible.chapter06;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.chapter05.App5_09_SphereWorld2;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App6_01_SphereWorld3 extends App5_09_SphereWorld2 {

	public static void main(String[] args) {
		new App6_01_SphereWorld3().setVisible(true);
	}

	FloatBuffer fLightPosMirror = FloatBuffer.wrap(new float[] {
			fLightPos.get(0), -fLightPos.get(1), fLightPos.get(2),
			fLightPos.get(3) });

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		frameCamera.ApplyCameraTransform();

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPosMirror);
		gl.glPushMatrix();
		gl.glFrontFace(GL2.GL_CW);
		gl.glScalef(1, -1, 1);
		drawWorld();
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPopMatrix();

		gl.glDisable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		drawGround();
		gl.glDisable(GL.GL_BLEND);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPos);
		drawWorld();
		gl.glPopMatrix();

		gl.glFlush();
	}

	@Override
	protected void drawShadow() {
		// do nothing
	}

	@Override
	protected void drawWorld() {
		gl.glPushMatrix();
		gl.glTranslatef(0, 0.4f, 0);
		super.drawWorld();
		gl.glPopMatrix();
	}

	@Override
	protected void drawGround() {
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
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		super.reshape(drawable, x, y, width, height);
		gl.glTranslatef(0, -0.4f, 0);
	}
}
