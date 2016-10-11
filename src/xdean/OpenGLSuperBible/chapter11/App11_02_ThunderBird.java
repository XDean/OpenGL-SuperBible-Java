package xdean.OpenGLSuperBible.chapter11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App11_02_ThunderBird extends App11_02_AirplaneBase {

	public static void main(String[] args) {
		new App11_02_ThunderBird().setVisible(true);
	}

	private static final int BODY_TEXTURE = 0;
	private static final int GLASS_TEXTURE = 1;
	int textureObjects[] = new int[2];

	int bodyList, glassList;
	float yRot = 0.0f;

	void TimerFunction(int value) {
		glut.glutPostRedisplay();
		glut.glutTimerFunc(5, this::TimerFunction, 1);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glut.glutTimerFunc(5, this::TimerFunction, 1);

		IntBuffer pBytes;
		IntWrapper iWidth = new IntWrapper(), iHeight = new IntWrapper();
		IntWrapper iComponents = new IntWrapper(), eFormat = new IntWrapper();

		float fAmbLight[] = { 0.1f, 0.1f, 0.1f, 0.0f };
		float fDiffLight[] = { 1.0f, 1.0f, 1.0f, 0.0f };
		float fSpecLight[] = { 0.5f, 0.5f, 0.5f, 0.0f };
		float lightPos[] = { -100.0f, 100.0f, 100.0f, 1.0f };

		gl.glClearColor(0.0f, 0.0f, .50f, 1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_CULL_FACE);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);

		gl.glGenTextures(2, textureObjects, 0);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[BODY_TEXTURE]);

		pBytes = glt.gltLoadTGA("body.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		FloatBuffer fLargest = FloatBuffer.allocate(1);
		gl.glGetFloatv(GL2.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, fLargest);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D,
				GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, fLargest.get(0));

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[GLASS_TEXTURE]);

		pBytes = glt.gltLoadTGA("glass.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP);

		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, fDiffLight, 0);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 128);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, fAmbLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, fDiffLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, fSpecLight, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_COLOR_CONTROL,
				GL2.GL_SEPARATE_SPECULAR_COLOR);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

		gl.glEnable(GL2.GL_RESCALE_NORMAL);

		bodyList = gl.glGenLists(2);
		glassList = bodyList + 1;

		gl.glNewList(bodyList, GL2.GL_COMPILE);
		DrawBody();
		gl.glEndList();

		gl.glNewList(glassList, GL2.GL_COMPILE);
		DrawGlass();
		gl.glEndList();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		yRot += 0.5f;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glPushMatrix();

		gl.glTranslatef(0.0f, 0.0f, -4.0f);
		gl.glRotatef(10.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRot, 0.0f, 1.0f, 0.0f);

		gl.glPushMatrix();
		gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[BODY_TEXTURE]);
		gl.glScalef(.01f, .01f, .01f);
		gl.glCallList(bodyList);
		gl.glPopMatrix();

		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glTranslatef(0.0f, 0.132f, 0.555f);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[GLASS_TEXTURE]);

		gl.glScalef(0.01f, 0.01f, 0.01f);
		gl.glFrontFace(GL2.GL_CW);
		gl.glCallList(glassList);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glCallList(glassList);
		gl.glDisable(GL2.GL_BLEND);

		gl.glPopMatrix();

		glut.glutSwapBuffers();
	}

	void DrawBody() {
		int iFace, iPoint;
		gl.glBegin(GL2.GL_TRIANGLES);
		for (iFace = 0; iFace < 3704; iFace++)
			for (iPoint = 0; iPoint < 3; iPoint++) {
				gl.glTexCoord2fv(textures,
						face_indicies[iFace * 9 + iPoint + 6] * 2);

				gl.glNormal3fv(normals,
						face_indicies[iFace * 9 + iPoint + 3] * 3);

				gl.glVertex3fv(vertices, face_indicies[iFace * 9 + iPoint] * 3);
			}
		gl.glEnd();
	}

	void DrawGlass() {
		int iFace, iPoint;
		gl.glBegin(GL2.GL_TRIANGLES);
		for (iFace = 0; iFace < 352; iFace++)
			for (iPoint = 0; iPoint < 3; iPoint++) {
				gl.glTexCoord2fv(texturesGlass, face_indiciesGlass[iFace * 9
						+ iPoint + 6] * 2);

				gl.glNormal3fv(normalsGlass, face_indiciesGlass[iFace * 9
						+ iPoint + 3] * 3);

				gl.glVertex3fv(verticesGlass, face_indiciesGlass[iFace * 9
						+ iPoint] * 3);
			}
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapWithPerspective(drawable, width, height, 35, 1,
				1000);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		super.dispose(drawable);
		gl.glDeleteLists(bodyList, 2);
		gl.glDeleteTextures(2, textureObjects, 0);
	}
}
