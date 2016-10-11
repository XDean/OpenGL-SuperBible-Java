package xdean.OpenGLSuperBible.chapter08;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.Math3d;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.GL.GLFrame;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App8_04_SphereWorld4 extends BaseApp {

	public static void main(String[] args) {
		new App8_04_SphereWorld4().setVisible(true);
	}

	private static final int NUM_SPHERES = 30;
	GLFrame spheres[] = new GLFrame[NUM_SPHERES];
	GLFrame frameCamera;

	FloatBuffer lightPos = FloatBuffer.wrap(new float[] { -100.0f, 100.0f,
			50.0f, 1.0f });
	FloatBuffer noLight = FloatBuffer
			.wrap(new float[] { 0.0f, 0.0f, 0.0f, 0.0f });
	FloatBuffer lowLight = FloatBuffer.wrap(new float[] { 0.25f, 0.25f, 0.25f,
			1.0f });
	FloatBuffer brightLight = FloatBuffer.wrap(new float[] { 1.0f, 1.0f, 1.0f,
			1.0f });

	float shadowMatrix[] = new float[16];

	private static final int GROUND_TEXTURE = 0;
	private static final int TORUS_TEXTURE = 1;
	private static final int SPHERE_TEXTURE = 2;
	private static final int NUM_TEXTURES = 3;
	int textureObjects[] = new int[NUM_TEXTURES];

	String textureFiles[] = { "grass.tga", "wood.tga", "orb.tga" };

	float yRot = 0.0f;

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

	void timerFunction(int value) {
		glut.glutPostRedisplay();
		glut.glutTimerFunc(3, this::timerFunction, 1);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		
		glut.glutSpecialFunc(this::specialKeys);
		glut.glutTimerFunc(3, this::timerFunction, 1);
		
		float points[][] = { { 0.0f, -0.4f, 0.0f }, { 10.0f, -0.4f, 0.0f },
				{ 5.0f, -0.4f, -5.0f } };

		gl.glClearColor(lowLight.get(0), lowLight.get(1), lowLight.get(2),
				lowLight.get(3));

		gl.glStencilOp(GL2.GL_INCR, GL2.GL_INCR, GL2.GL_INCR);
		gl.glClearStencil(0);
		gl.glStencilFunc(GL2.GL_EQUAL, 0x0, 0x01);

		gl.glCullFace(GL2.GL_BACK);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_MULTISAMPLE);
		
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL, GL2.GL_SEPARATE_SPECULAR_COLOR);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, noLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lowLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, brightLight);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, brightLight);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);

		float plane[] = new float[4];
		Math3d.m3dGetPlaneEquation(plane, points[0], points[1], points[2]);
		Math3d.m3dMakePlanarShadowMatrix(shadowMatrix, plane, lightPos.array());

		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, brightLight);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);

		frameCamera = new GLFrame(gl, glu);
		for (int i = 0; i < NUM_SPHERES; i++) {
			float x = (float) ((Math.random() * 40) - 20);
			float z = (float) ((Math.random() * 40) - 20);
			spheres[i] = new GLFrame(gl, glu);
			spheres[i].setOrigin(x, 0.0f, z);
		}

		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glGenTextures(NUM_TEXTURES, textureObjects, 0);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);

		for (int i = 0; i < NUM_TEXTURES; i++) {
			IntBuffer imageBytes;
			IntWrapper width = new IntWrapper(), height = new IntWrapper();
			IntWrapper type = new IntWrapper(), format = new IntWrapper();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[i]);
			imageBytes = glt.gltLoadTGA(textureFiles[i], width, height, type,
					format);
			glu.gluBuild2DMipmaps(GL2.GL_TEXTURE_2D, format.get(), width.get(),
					height.get(), format.get(), type.get(), imageBytes);

			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
					GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
					GL2.GL_LINEAR_MIPMAP_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
					GL2.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
					GL2.GL_CLAMP_TO_EDGE);
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT
				| GL2.GL_STENCIL_BUFFER_BIT);

		gl.glPushMatrix();
		frameCamera.ApplyCameraTransform();

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos);

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		drawGround();

		gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_STENCIL_TEST);
		gl.glPushMatrix();
		gl.glMultMatrixf(shadowMatrix, 0);
		drawInhabitants(true);
		gl.glPopMatrix();
		gl.glDisable(GL2.GL_STENCIL_TEST);
		gl.glDisable(GL2.GL_BLEND);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glEnable(GL2.GL_DEPTH_TEST);

		drawInhabitants(false);

		gl.glPopMatrix();

		gl.glFlush();
	}

	protected void drawGround() {
		float extent = 20.0f;
		float step = 1.0f;
		float y = -0.4f;
		float s = 0.0f;
		float t = 0.0f;
		float texStep = 1.0f / (extent * .075f);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[GROUND_TEXTURE]);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_REPEAT);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_REPEAT);

		for (int strip = (int) -extent; strip <= extent; strip += step) {
			t = 0.0f;
			gl.glBegin(GL2.GL_TRIANGLE_STRIP);

			for (int run = (int) extent; run >= -extent; run -= step) {
				gl.glTexCoord2f(s, t);
				gl.glNormal3f(0.0f, 1.0f, 0.0f);
				gl.glVertex3f(strip, y, run);

				gl.glTexCoord2f(s + texStep, t);
				gl.glNormal3f(0.0f, 1.0f, 0.0f);
				gl.glVertex3f(strip + step, y, run);

				t += texStep;
			}
			gl.glEnd();
			s += texStep;
		}
	}

	protected void drawInhabitants(boolean isShadow) {
		if (isShadow == false) {
			yRot += 0.5f;
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		} else
			gl.glColor4f(0.00f, 0.00f, 0.00f, .6f);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[SPHERE_TEXTURE]);
		for (int i = 0; i < NUM_SPHERES; i++) {
			gl.glPushMatrix();
			spheres[i].ApplyActorTransform();
			glt.gltDrawSphere(0.3f, 21, 11);
			gl.glPopMatrix();
		}

		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.1f, -2.5f);

		gl.glPushMatrix();
		gl.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(1.0f, 0.0f, 0.0f);
		glt.gltDrawSphere(0.1f, 21, 11);
		gl.glPopMatrix();

		if (isShadow == false) {
			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, brightLight);
		}

		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[TORUS_TEXTURE]);
		glt.gltDrawTorus(0.35f, 0.15f, 61, 37);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, noLight);
		gl.glPopMatrix();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		super.dispose(drawable);
		gl.glDeleteTextures(NUM_TEXTURES, textureObjects, 0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
	}
}