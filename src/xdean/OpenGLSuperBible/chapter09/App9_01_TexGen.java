package xdean.OpenGLSuperBible.chapter09;

import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App9_01_TexGen extends BaseApp {

	public static void main(String[] args) {
		new App9_01_TexGen().setVisible(true);
	}

	float xRot = 0.0f;
	float yRot = 0.0f;

	int toTextures[] = new int[2];
	int iRenderMode = 3;

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutCreateMenu(this::ProcessMenu);
		glut.glutAddMenuEntry("Object Linear", 1);
		glut.glutAddMenuEntry("Eye Linear", 2);
		glut.glutAddMenuEntry("Sphere Map", 3);
		glut.glutAttachMenu(GLUT_RIGHT_BUTTON);
	}

	@Override
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
	protected boolean isOpenSpecialKey() {
		return true;
	}

	void ProcessMenu(int value) {
		float zPlane[] = { 0.0f, 0.0f, 1.0f, 0.0f };

		iRenderMode = value;

		switch (value) {
		case 1:
			gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE,
					GL2.GL_OBJECT_LINEAR);
			gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE,
					GL2.GL_OBJECT_LINEAR);
			gl.glTexGenfv(GL2.GL_S, GL2.GL_OBJECT_PLANE, zPlane, 0);
			gl.glTexGenfv(GL2.GL_T, GL2.GL_OBJECT_PLANE, zPlane, 0);
			break;

		case 2:
			gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
			gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_EYE_LINEAR);
			gl.glTexGenfv(GL2.GL_S, GL2.GL_EYE_PLANE, zPlane, 0);
			gl.glTexGenfv(GL2.GL_T, GL2.GL_EYE_PLANE, zPlane, 0);
			break;

		case 3:
		default:
			gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
			gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
			break;
		}

		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);

		gl.glGenTextures(2, toTextures, 0);

		IntBuffer pBytes;
		IntWrapper iWidth = new IntWrapper(), iHeight = new IntWrapper();
		IntWrapper iComponents = new IntWrapper(), eFormat = new IntWrapper();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, toTextures[0]);

		pBytes = glt.gltLoadTGA("stripes.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_REPEAT);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_REPEAT);
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, toTextures[1]);
		pBytes = glt.gltLoadTGA("Environment.tga", iWidth, iHeight,
				iComponents, eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_REPEAT);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_REPEAT);
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glEnable(GL2.GL_TEXTURE_GEN_S);
		gl.glEnable(GL2.GL_TEXTURE_GEN_T);

		gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
		gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		glu.gluOrtho2D(0.0f, 1.0f, 0.0f, 1.0f);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, toTextures[1]);
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);

		gl.glDepthMask(false);

		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex2f(0.0f, 0.0f);

		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex2f(1.0f, 0.0f);

		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex2f(1.0f, 1.0f);

		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex2f(0.0f, 1.0f);
		gl.glEnd();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glEnable(GL2.GL_TEXTURE_GEN_S);
		gl.glEnable(GL2.GL_TEXTURE_GEN_T);
		gl.glDepthMask(true);

		if (iRenderMode != 3)
			gl.glBindTexture(GL2.GL_TEXTURE_2D, toTextures[0]);

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -2.0f);
		gl.glRotatef(xRot, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		glt.gltDrawTorus(0.35f, 0.15f, 61, 37);

		gl.glPopMatrix();

		glut.glutSwapBuffers();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl
				.reshapWithPerspective(drawable, width, height, 45, 1, 225);
	}

}
