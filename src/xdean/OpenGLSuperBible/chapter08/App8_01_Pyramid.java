package xdean.OpenGLSuperBible.chapter08;

import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.Math3d;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App8_01_Pyramid extends BaseApp {

	public static void main(String[] args) {
		new App8_01_Pyramid().setVisible(true);
	}

	private float[] vCorners[] = { { 0.0f, .80f, 0.0f },
			{ -0.5f, 0.0f, -.50f }, { 0.5f, 0.0f, -0.50f },
			{ 0.5f, 0.0f, 0.5f }, { -0.5f, 0.0f, 0.5f } };
	private float xRot = 45, yRot = 45;

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
		glut.glutPostRedisplay();
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		IntBuffer imageBuffer;
		IntWrapper width = new IntWrapper(), height = new IntWrapper();
		IntWrapper type = new IntWrapper(), format = new IntWrapper();

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

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		imageBuffer = glt.gltLoadTGA("stone.tga", width, height, type, format);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, format.get(), width.get(),
				height.get(), 0, format.get(), type.get(), imageBuffer);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
		gl.glEnable(GL2.GL_TEXTURE_2D);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, -0.25f, -4.0f);
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin(GL2.GL_TRIANGLES);
		gl.glNormal3f(0.0f, -1.0f, 0.0f);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3fv(vCorners[2], 0);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3fv(vCorners[4], 0);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3fv(vCorners[1], 0);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3fv(vCorners[2], 0);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3fv(vCorners[3], 0);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3fv(vCorners[4], 0);

		float[] vNormal = new float[3];
		Math3d.m3dFindNormal(vNormal, vCorners[0], vCorners[4], vCorners[3]);
		gl.glNormal3fv(vNormal, 0);
		gl.glTexCoord2f(0.5f, 1.0f);
		gl.glVertex3fv(vCorners[0], 0);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3fv(vCorners[4], 0);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3fv(vCorners[3], 0);

		Math3d.m3dFindNormal(vNormal, vCorners[0], vCorners[1], vCorners[4]);
		gl.glNormal3fv(vNormal, 0);
		gl.glTexCoord2f(0.5f, 1.0f);
		gl.glVertex3fv(vCorners[0], 0);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3fv(vCorners[1], 0);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3fv(vCorners[4], 0);

		Math3d.m3dFindNormal(vNormal, vCorners[0], vCorners[2], vCorners[1]);
		gl.glNormal3fv(vNormal, 0);
		gl.glTexCoord2f(0.5f, 1.0f);
		gl.glVertex3fv(vCorners[0], 0);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3fv(vCorners[2], 0);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3fv(vCorners[1], 0);

		Math3d.m3dFindNormal(vNormal, vCorners[0], vCorners[3], vCorners[2]);
		gl.glNormal3fv(vNormal, 0);
		gl.glTexCoord2f(0.5f, 1.0f);
		gl.glVertex3fv(vCorners[0], 0);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3fv(vCorners[3], 0);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3fv(vCorners[2], 0);
		gl.glEnd();

		gl.glPopMatrix();

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 40);
	}

}
