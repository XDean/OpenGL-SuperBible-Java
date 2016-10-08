package xdean.OpenGLSuperBible.chapter09;
/**
 * It is same as App8_04_SphereWorld4
 */

//package xdean.OpenGLSuperBible.chapter09;
//
//import java.nio.IntBuffer;
//import java.util.Random;
//
//import xdean.OpenGLSuperBible.share.BaseApp;
//import xdean.OpenGLSuperBible.share.Math3d;
//import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
//import xdean.OpenGLSuperBible.share.GL.GLFrame;
//import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;
//
//import com.jogamp.opengl.GL2;
//import com.jogamp.opengl.GLAutoDrawable;
//
//public class App9_SphereWorld5 extends BaseApp {
//
//	public static void main(String[] args) {
//		new App9_SphereWorld5().setVisible(true);
//	}
//
//	private static final int NUM_SPHERES = 30;
//	GLFrame spheres[] = new GLFrame[NUM_SPHERES];
//	GLFrame frameCamera;
//
//	float fLightPos[] = { -100.0f, 100.0f, 50.0f, 1.0f };
//	float fNoLight[] = { 0.0f, 0.0f, 0.0f, 0.0f };
//	float fLowLight[] = { 0.25f, 0.25f, 0.25f, 1.0f };
//	float fBrightLight[] = { 1.0f, 1.0f, 1.0f, 1.0f };
//
//	float mShadowMatrix[] = new float[16];
//
//	private static final int GROUND_TEXTURE = 0;
//	private static final int TORUS_TEXTURE = 1;
//	private static final int SPHERE_TEXTURE = 2;
//	private static final int NUM_TEXTURES = 3;
//	int textureObjects[] = new int[NUM_TEXTURES];
//
//	String szTextureFiles[] = { "grass.tga", "wood.tga", "orb.tga" };
//
//	Random r = new Random();
//	float yRot;
//
//	void TimerFunction(int value) {
//		glut.glutPostRedisplay();
//		glut.glutTimerFunc(3, this::TimerFunction, 1);
//	}
//
//	@Override
//	protected void specialKeys(int key, int x, int y) {
//
//		if (key == GLUT_KEY_UP)
//			frameCamera.MoveForward(0.1f);
//
//		if (key == GLUT_KEY_DOWN)
//			frameCamera.MoveForward(-0.1f);
//
//		if (key == GLUT_KEY_LEFT)
//			frameCamera.RotateLocalY(0.1f);
//
//		if (key == GLUT_KEY_RIGHT)
//			frameCamera.RotateLocalY(-0.1f);
//
//		glut.glutPostRedisplay();
//	}
//
//	@Override
//	protected boolean isOpenSpecialKey() {
//		return true;
//	}
//
//	@Override
//	public void init(GLAutoDrawable drawable) {
//		super.init(drawable);
//		glut.glutTimerFunc(3, this::TimerFunction, 1);
//		float[] vPoints[] = { { 0.0f, -0.4f, 0.0f }, { 10.0f, -0.4f, 0.0f },
//				{ 5.0f, -0.4f, -5.0f } };
//		int iSphere;
//		int i;
//
//		gl.glClearColor(fLowLight[0], fLowLight[1], fLowLight[2], fLowLight[3]);
//
//		gl.glStencilOp(GL2.GL_INCR, GL2.GL_INCR, GL2.GL_INCR);
//		gl.glClearStencil(0);
//		gl.glStencilFunc(GL2.GL_EQUAL, 0x0, 0x01);
//
//		gl.glCullFace(GL2.GL_BACK);
//		gl.glFrontFace(GL2.GL_CCW);
//		gl.glEnable(GL2.GL_CULL_FACE);
//		gl.glEnable(GL2.GL_DEPTH_TEST);
//		gl.glEnable(GL2.GL_MULTISAMPLE);
//
//		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, fNoLight, 0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fLowLight, 0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fBrightLight, 0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fBrightLight, 0);
//		gl.glEnable(GL2.GL_LIGHTING);
//		gl.glEnable(GL2.GL_LIGHT0);
//
//		gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL,
//				GL2.GL_SEPARATE_SPECULAR_COLOR);
//
//		float pPlane[] = new float[4];
//		Math3d.m3dGetPlaneEquation(pPlane, vPoints[0], vPoints[1], vPoints[2]);
//		Math3d.m3dMakePlanarShadowMatrix(mShadowMatrix, pPlane, fLightPos);
//
//		gl.glEnable(GL2.GL_COLOR_MATERIAL);
//		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fBrightLight, 0);
//		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);
//
//		frameCamera = new GLFrame(gl, glu);
//		for (iSphere = 0; iSphere < NUM_SPHERES; iSphere++) {
//			spheres[iSphere] = new GLFrame(gl, glu);
//			spheres[iSphere].setOrigin(((float) (r.nextInt(400) - 200) * 0.1f),
//					0.0f, (float) (r.nextInt(400) - 200) * 0.1f);
//		}
//
//		gl.glEnable(GL2.GL_TEXTURE_2D);
//		gl.glGenTextures(NUM_TEXTURES, textureObjects, 0);
//		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
//				GL2.GL_MODULATE);
//
//		for (i = 0; i < NUM_TEXTURES; i++) {
//
//			IntBuffer imageBytes;
//			IntWrapper width = new IntWrapper(), height = new IntWrapper();
//			IntWrapper type = new IntWrapper(), format = new IntWrapper();
//			gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[i]);
//			imageBytes = glt.gltLoadTGA(szTextureFiles[i], width, height, type,
//					format);
//			glu.gluBuild2DMipmaps(GL2.GL_TEXTURE_2D, format.get(), width.get(),
//					height.get(), format.get(), type.get(), imageBytes);
//
//			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
//					GL2.GL_LINEAR);
//			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
//					GL2.GL_LINEAR_MIPMAP_LINEAR);
//			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
//					GL2.GL_CLAMP_TO_EDGE);
//			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
//					GL2.GL_CLAMP_TO_EDGE);
//		}
//	}
//
//	@Override
//	public void display(GLAutoDrawable drawable) {
//		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT
//				| GL2.GL_STENCIL_BUFFER_BIT);
//
//		gl.glPushMatrix();
//		frameCamera.ApplyCameraTransform();
//
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, fLightPos, 0);
//
//		gl.glColor3f(1.0f, 1.0f, 1.0f);
//		DrawGround();
//
//		gl.glDisable(GL2.GL_DEPTH_TEST);
//		gl.glDisable(GL2.GL_LIGHTING);
//		gl.glDisable(GL2.GL_TEXTURE_2D);
//		gl.glEnable(GL2.GL_BLEND);
//		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
//		gl.glEnable(GL2.GL_STENCIL_TEST);
//		gl.glPushMatrix();
//		gl.glMultMatrixf(mShadowMatrix, 0);
//		DrawInhabitants(1);
//		gl.glPopMatrix();
//		gl.glDisable(GL2.GL_STENCIL_TEST);
//		gl.glDisable(GL2.GL_BLEND);
//		gl.glEnable(GL2.GL_LIGHTING);
//		gl.glEnable(GL2.GL_TEXTURE_2D);
//		gl.glEnable(GL2.GL_DEPTH_TEST);
//
//		DrawInhabitants(0);
//
//		gl.glPopMatrix();
//
//		glut.glutSwapBuffers();
//
//	}
//
//	void DrawGround() {
//		float fExtent = 20.0f;
//		float fStep = 1.0f;
//		float y = -0.4f;
//		float s = 0.0f;
//		float t = 0.0f;
//		float texStep = 1.0f / (fExtent * .075f);
//
//		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[GROUND_TEXTURE]);
//		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
//				GL2.GL_REPEAT);
//		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
//				GL2.GL_REPEAT);
//
//		for (float iStrip = -fExtent; iStrip <= fExtent; iStrip += fStep) {
//			t = 0.0f;
//			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//
//			for (float iRun = fExtent; iRun >= -fExtent; iRun -= fStep) {
//				gl.glTexCoord2f(s, t);
//				gl.glNormal3f(0.0f, 1.0f, 0.0f);
//				gl.glVertex3f(iStrip, y, iRun);
//
//				gl.glTexCoord2f(s + texStep, t);
//				gl.glNormal3f(0.0f, 1.0f, 0.0f);
//				gl.glVertex3f(iStrip + fStep, y, iRun);
//
//				t += texStep;
//			}
//			gl.glEnd();
//			s += texStep;
//		}
//	}
//
//	void DrawInhabitants(int nShadow) {
//		int i;
//
//		if (nShadow == 0) {
//			yRot += 0.5f;
//			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//		} else
//			gl.glColor4f(0.00f, 0.00f, 0.00f, .6f);
//		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[SPHERE_TEXTURE]);
//		for (i = 0; i < NUM_SPHERES; i++) {
//			gl.glPushMatrix();
//			spheres[i].ApplyActorTransform();
//			glt.gltDrawSphere(0.3f, 21, 11);
//			gl.glPopMatrix();
//		}
//
//		gl.glPushMatrix();
//		gl.glTranslatef(0.0f, 0.1f, -2.5f);
//
//		gl.glPushMatrix();
//		gl.glRotatef(-yRot * 2.0f, 0.0f, 1.0f, 0.0f);
//		gl.glTranslatef(1.0f, 0.0f, 0.0f);
//		glt.gltDrawSphere(0.1f, 21, 11);
//		gl.glPopMatrix();
//
//		if (nShadow == 0) {
//			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fBrightLight, 0);
//		}
//
//		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
//		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[TORUS_TEXTURE]);
//		glt.gltDrawTorus(0.35f, 0.15f, 61, 37);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fNoLight, 0);
//		gl.glPopMatrix();
//	}
//
//	@Override
//	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
//			int height) {
//		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1, 50);
//	}
//
//	@Override
//	public void dispose(GLAutoDrawable drawable) {
//		super.dispose(drawable);
//		gl.glDeleteTextures(NUM_TEXTURES, textureObjects, 0);
//	}
//}
