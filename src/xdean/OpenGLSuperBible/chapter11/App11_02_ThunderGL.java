package xdean.OpenGLSuperBible.chapter11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.CTriangleMesh;
import xdean.OpenGLSuperBible.share.Math3d;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.GL.GLFrame;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App11_02_ThunderGL extends App11_02_AirplaneBase {

	public static void main(String[] args) {
		new App11_02_ThunderGL().setVisible(true);
	}

	GLFrame frameCamera;
	CTriangleMesh thunderBirdBody;
	CTriangleMesh thunderBirdGlass;

	int textureObjects[] = new int[3];
	private static final int CUBE_MAP = 0;
	private static final int BODY_TEXTURE = 1;
	private static final int GLASS_TEXTURE = 2;

	String szCubeFaces[] = { "pos_x.tga", "neg_x.tga", "pos_y.tga",
			"neg_y.tga", "pos_z.tga", "neg_z.tga" };

	int cube[] = { GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
			GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
			GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
			GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
			GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
			GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

	float yRot = 0.0f;

	void TimerFunction(int value) {
		glut.glutPostRedisplay();
		glut.glutTimerFunc(3, this::TimerFunction, 1);
	}

	protected void specialKeys(int key, int x, int y) {
		if (key == GLUT_KEY_UP)
			frameCamera.MoveForward(0.1f);

		if (key == GLUT_KEY_DOWN)
			frameCamera.MoveForward(-0.1f);

		if (key == GLUT_KEY_LEFT)
			frameCamera.RotateLocalY(0.1f);

		if (key == GLUT_KEY_RIGHT)
			frameCamera.RotateLocalY(-0.1f);

		if (key == GLUT_KEY_PAGE_UP)
			frameCamera.RotateLocalX(0.1f);

		if (key == GLUT_KEY_PAGE_DOWN)
			frameCamera.RotateLocalX(-0.1f);

		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		
		glut.glutSpecialFunc(this::specialKeys);

		frameCamera = new GLFrame(gl, glu);
		thunderBirdBody = new CTriangleMesh(gl);
		thunderBirdGlass = new CTriangleMesh(gl);

		glut.glutTimerFunc(3, this::TimerFunction, 1);

		float fScale = 0.01f;
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

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[BODY_TEXTURE]);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP,
				GL2.GL_TRUE);
		pBytes = glt.gltLoadTGA("body.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		FloatBuffer fLargest = FloatBuffer.allocate(1);
		gl.glGetFloatv(GL2.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, fLargest);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D,
				GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, fLargest.get(0));

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[GLASS_TEXTURE]);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP,
				GL2.GL_TRUE);

		pBytes = glt.gltLoadTGA("glass.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glBindTexture(GL2.GL_TEXTURE_CUBE_MAP, textureObjects[CUBE_MAP]);
		gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
		gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
		gl.glTexGeni(GL2.GL_R, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_REFLECTION_MAP);
		gl.glEnable(GL2.GL_TEXTURE_CUBE_MAP);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);

		float[] vVerts[] = new float[3][3];
		float[] vNorms[] = new float[3][3];
		float[] vTex[] = new float[3][2];

		thunderBirdBody.BeginMesh(3704 * 3);

		for (int iFace = 0; iFace < 3704; iFace++) {
			for (int iPoint = 0; iPoint < 3; iPoint++) {
				System.arraycopy(vertices,
						face_indicies[iFace * 9 + iPoint] * 3, vVerts[iPoint],
						0, 3);
				System.arraycopy(normals,
						face_indicies[iFace * 9 + iPoint + 3] * 3,
						vNorms[iPoint], 0, 3);
				System.arraycopy(textures,
						face_indicies[iFace * 9 + iPoint + 6] * 2,
						vTex[iPoint], 0, 2);
			}
			thunderBirdBody.AddTriangle(vVerts, vNorms, vTex);
		}

		thunderBirdBody.Scale(fScale);
		thunderBirdBody.EndMesh();

		thunderBirdGlass.BeginMesh(352 * 3);

		for (int iFace = 0; iFace < 352; iFace++) {
			for (int iPoint = 0; iPoint < 3; iPoint++) {
				System.arraycopy(verticesGlass, face_indiciesGlass[iFace * 9
						+ iPoint] * 3, vVerts[iPoint], 0, 3);
				System.arraycopy(normalsGlass, face_indiciesGlass[iFace * 9
						+ iPoint + 3] * 3, vNorms[iPoint], 0, 3);
				System.arraycopy(texturesGlass, face_indiciesGlass[iFace * 9
						+ iPoint + 6] * 2, vTex[iPoint], 0, 2);
			}

			thunderBirdGlass.AddTriangle(vVerts, vNorms, vTex);
		}

		thunderBirdGlass.Scale(fScale);
		thunderBirdGlass.EndMesh();

		float fAmbLight[] = { 0.075f, 0.075f, 0.075f, 0.0f };
		float fDiffLight[] = { 1.0f, 1.0f, 1.0f, 0.0f };
		float fSpecLight[] = { 0.25f, 0.25f, 0.25f, 0.0f };
		float lightPos[] = { -50.0f, 100.0f, 100.0f, 1.0f };

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fDiffLight, 0);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fAmbLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fDiffLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fSpecLight, 0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, fAmbLight, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL,
				GL2.GL_SEPARATE_SPECULAR_COLOR);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

		frameCamera.MoveUp(20.0f);
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
		gl.glDisable(GL2.GL_TEXTURE_CUBE_MAP);

		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glPushMatrix();

		float[] m = new float[16], invert = new float[16];
		frameCamera.GetCameraOrientation(m);
		Math3d.m3dInvertMatrix44(invert, m);
		gl.glMultMatrixf(invert, 0);
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		yRot += 0.1f;
		gl.glTranslatef(0.0f, 19.6f, -3.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);
		DrawThunderBird();

		gl.glPushMatrix();
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
		gl.glRotatef(-10.0f, 1.0f, 0.0f, 0.0f);
		
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 11.5f, -4.0f);
		DrawThunderBird();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.85f, 10.75f, -3.5f);
//		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
//		gl.glRotatef(-10.0f, 1.0f, 0.0f, 0.0f);
		DrawThunderBird();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(-1.0f, 9.75f, -4.0f);
//		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
//		gl.glRotatef(-10.0f, 1.0f, 0.0f, 0.0f);
		DrawThunderBird();
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(-0.15f, 9.0f, -3.5f);
//		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
//		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
//		gl.glRotatef(-10.0f, 1.0f, 0.0f, 0.0f);
		DrawThunderBird();
		gl.glPopMatrix();
		gl.glPopMatrix();

		gl.glMatrixMode(GL2.GL_TEXTURE);
		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glPopMatrix();
		gl.glActiveTexture(GL2.GL_TEXTURE0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glPopMatrix();

		glut.glutSwapBuffers();
	}

	void DrawSkyBox() {
		float fExtent = 50.0f;

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

	void DrawThunderBird() {
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glDisable(GL2.GL_TEXTURE_CUBE_MAP);
		gl.glActiveTexture(GL2.GL_TEXTURE0);

		gl.glPushMatrix();
		gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		gl.glTexEnvi(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[BODY_TEXTURE]);
		thunderBirdBody.Draw();
		gl.glPopMatrix();

		gl.glActiveTexture(GL2.GL_TEXTURE1);
		gl.glEnable(GL2.GL_TEXTURE_CUBE_MAP);
		gl.glActiveTexture(GL2.GL_TEXTURE0);

		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.25f);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[GLASS_TEXTURE]);

		gl.glTranslatef(0.0f, 0.132f, 0.555f);

		gl.glFrontFace(GL2.GL_CW);
		thunderBirdGlass.Draw();
		gl.glFrontFace(GL2.GL_CCW);
		thunderBirdGlass.Draw();
		gl.glDisable(GL2.GL_BLEND);

		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 60, 1,
				1000);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		super.dispose(drawable);
		gl.glDeleteTextures(2, textureObjects, 0);
	}
}
