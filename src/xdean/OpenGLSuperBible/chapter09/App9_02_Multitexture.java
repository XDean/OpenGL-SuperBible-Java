package xdean.OpenGLSuperBible.chapter09;

import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.Math3d;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.GL.GLFrame;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App9_02_Multitexture extends BaseApp {

	public static void main(String[] args) {
		new App9_02_Multitexture().setVisible(true);
	}

	GLFrame frameCamera;
	int textureObjects[] = new int[2];
	private static final int CUBE_MAP = 0;
	private static final int COLOR_MAP = 1;

	String szCubeFaces[] = { "pos_x.tga", "neg_x.tga", "pos_y.tga",
			"neg_y.tga", "pos_z.tga", "neg_z.tga" };

	int cube[] = { GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
			GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
			GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
			GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
			GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
			GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

	void TimerFunction(int value) {
		glut.glutPostRedisplay();
		glut.glutTimerFunc(3, this::TimerFunction, 1);
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

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		frameCamera = new GLFrame(gl, glu);
		glut.glutTimerFunc(33, this::TimerFunction, 1);

		IntBuffer pBytes;
		IntWrapper iWidth = new IntWrapper(), iHeight = new IntWrapper();
		IntWrapper iComponents = new IntWrapper(), eFormat = new IntWrapper();
		int i;

		gl.glCullFace(GL2.GL_BACK);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		gl.glGenTextures(2, textureObjects, 0);

		gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP, textureObjects[CUBE_MAP]);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R,
				GL2.GL_CLAMP_TO_EDGE);

		for (i = 0; i < 6; i++) {
			gl.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_GENERATE_MIPMAP,
					GL2.GL_TRUE);

			pBytes = glt.gltLoadTGA(szCubeFaces[i], iWidth, iHeight,
					iComponents, eFormat);
			gl.glTexImage2D(cube[i], 0, eFormat.get(), iWidth.get(),
					iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);
		}

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[COLOR_MAP]);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP,
				GL2.GL_TRUE);
		pBytes = glt.gltLoadTGA("tarnish.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[COLOR_MAP]);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP, textureObjects[CUBE_MAP]);
		gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
		gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
		gl.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
		gl.glEnable(GL2.GL_TEXTURE_CUBE_MAP);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glPushMatrix();
		frameCamera.ApplyCameraTransform();
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glActiveTexture(GL2.GL_TEXTURE1);

		gl.glEnable(GL2.GL_TEXTURE_CUBE_MAP);
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);
		gl.glDisable(GL2.GL_TEXTURE_GEN_R);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
		DrawSkyBox();

		gl.glEnable(GL2.GL_TEXTURE_GEN_S);
		gl.glEnable(GL2.GL_TEXTURE_GEN_T);
		gl.glEnable(GL2.GL_TEXTURE_GEN_R);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);

		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -3.0f);

		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glPushMatrix();

		float[] m = new float[16], invert = new float[16];
		frameCamera.GetCameraOrientation(m);
		Math3d.m3dInvertMatrix44(invert, m);
		gl.glMultMatrixf(invert, 0);

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		glt.gltDrawSphere(0.75f, 41, 41);

		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPopMatrix();

		gl.glPopMatrix();

		glut.glutSwapBuffers();
	}

	void DrawSkyBox() {
		float fExtent = 15.0f;

		gl.glBegin(GL2.GL_QUADS);
		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, -1.0f, 1.0f);
		gl.glVertex3f(-fExtent, -fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, -1.0f, -1.0f);
		gl.glVertex3f(-fExtent, -fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, 1.0f, -1.0f);
		gl.glVertex3f(-fExtent, fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, 1.0f, 1.0f);
		gl.glVertex3f(-fExtent, fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, -1.0f, -1.0f);
		gl.glVertex3f(fExtent, -fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, -1.0f, 1.0f);
		gl.glVertex3f(fExtent, -fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, 1.0f, 1.0f);
		gl.glVertex3f(fExtent, fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, 1.0f, -1.0f);
		gl.glVertex3f(fExtent, fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, -1.0f, -1.0f);
		gl.glVertex3f(-fExtent, -fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, -1.0f, -1.0f);
		gl.glVertex3f(fExtent, -fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, 1.0f, -1.0f);
		gl.glVertex3f(fExtent, fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, 1.0f, -1.0f);
		gl.glVertex3f(-fExtent, fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, -1.0f, 1.0f);
		gl.glVertex3f(fExtent, -fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, -1.0f, 1.0f);
		gl.glVertex3f(-fExtent, -fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, 1.0f, 1.0f);
		gl.glVertex3f(-fExtent, fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, 1.0f, 1.0f);
		gl.glVertex3f(fExtent, fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, 1.0f, 1.0f);
		gl.glVertex3f(-fExtent, fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, 1.0f, -1.0f);
		gl.glVertex3f(-fExtent, fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, 1.0f, -1.0f);
		gl.glVertex3f(fExtent, fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, 1.0f, 1.0f);
		gl.glVertex3f(fExtent, fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, -1.0f, -1.0f);
		gl.glVertex3f(-fExtent, -fExtent, -fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, -1.0f, -1.0f, 1.0f);
		gl.glVertex3f(-fExtent, -fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, -1.0f, 1.0f);
		gl.glVertex3f(fExtent, -fExtent, fExtent);

		gl.glMultiTexCoord3f(GL2.GL_TEXTURE1, 1.0f, -1.0f, -1.0f);
		gl.glVertex3f(fExtent, -fExtent, -fExtent);
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1,
				2000);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		super.dispose(drawable);

		gl.glDeleteTextures(2, textureObjects, 0);
	}
}
