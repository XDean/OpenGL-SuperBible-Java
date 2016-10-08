package xdean.OpenGLSuperBible.chapter08;

import java.nio.Buffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

/**
 *
 * XXX:I don't know why there was nothing happened when I clicked the menu to
 * alter the texture filter.
 * 
 * @author XDean
 *
 */
public class App8_03_Tunnel extends BaseApp {

	public static void main(String[] args) {
		new App8_03_Tunnel().setVisible(true);
	}

	protected static final int TEXTURE_BRICK = 0;
	protected static final int TEXTURE_FLOOR = 1;
	protected static final int TEXTURE_CEILING = 2;
	protected static final int TEXTURE_COUNT = 3;

	protected int textures[] = new int[TEXTURE_COUNT];
	protected String textureFiles[] = { "brick.tga", "floor.tga", "ceiling.tga" };

	float zPos = -60.0f;

	@Override
	protected void specialKeys(int key, int x, int y) {
		if (key == GLUT_KEY_UP)
			zPos += 1.0f;
		if (key == GLUT_KEY_DOWN)
			zPos -= 1.0f;
		glut.glutPostRedisplay();
	}

	@Override
	protected boolean isOpenSpecialKey() {
		return true;
	}

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutCreateMenu(this::processMenu);
		glut.glutAddMenuEntry("GL_NEAREST", 0);
		glut.glutAddMenuEntry("GL_LINEAR", 1);
		glut.glutAddMenuEntry("GL_NEAREST_MIPMAP_NEAREST", 2);
		glut.glutAddMenuEntry("GL_NEAREST_MIPMAP_LINEAR", 3);
		glut.glutAddMenuEntry("GL_LINEAR_MIPMAP_NEAREST", 4);
		glut.glutAddMenuEntry("GL_LINEAR_MIPMAP_LINEAR", 5);
	}

	protected void processMenu(int value) {

		for (int i = 0; i < TEXTURE_COUNT; i++) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[i]);
			switch (value) {
			case 0:
				gl.glTexParameteri(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
				break;
			case 1:
				gl.glTexParameteri(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
				break;
			case 2:
				gl.glTexParameteri(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MIN_FILTER,
						GL2.GL_NEAREST_MIPMAP_NEAREST);
				break;
			case 3:
				gl.glTexParameteri(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST_MIPMAP_LINEAR);
				break;
			case 4:
				gl.glTexParameteri(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_NEAREST);
				break;
			case 5:
			default:
				gl.glTexParameteri(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
				break;
			}
		}
		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);

		gl.glGenTextures(TEXTURE_COUNT, textures, 0);
		for (int i = 0; i < TEXTURE_COUNT; i++) {
			Buffer imageBytes;
			IntWrapper width = new IntWrapper(), height = new IntWrapper();
			IntWrapper type = new IntWrapper(), format = new IntWrapper();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[i]);
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
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, zPos);

		for (float z = 60.0f; z >= 0.0f; z -= 10) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[TEXTURE_FLOOR]);
			gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-10.0f, -10.0f, z);

			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(10.0f, -10.0f, z);

			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(10.0f, -10.0f, z - 10.0f);

			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-10.0f, -10.0f, z - 10.0f);
			gl.glEnd();

			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[TEXTURE_CEILING]);
			gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-10.0f, 10.0f, z - 10.0f);

			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(10.0f, 10.0f, z - 10.0f);

			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(10.0f, 10.0f, z);

			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-10.0f, 10.0f, z);
			gl.glEnd();

			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[TEXTURE_BRICK]);
			gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(-10.0f, -10.0f, z);

			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(-10.0f, -10.0f, z - 10.0f);

			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(-10.0f, 10.0f, z - 10.0f);

			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(-10.0f, 10.0f, z);
			gl.glEnd();

			gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(0.0f, 1.0f);
			gl.glVertex3f(10.0f, 10.0f, z);

			gl.glTexCoord2f(1.0f, 1.0f);
			gl.glVertex3f(10.0f, 10.0f, z - 10.0f);

			gl.glTexCoord2f(1.0f, 0.0f);
			gl.glVertex3f(10.0f, -10.0f, z - 10.0f);

			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(10.0f, -10.0f, z);
			gl.glEnd();
		}
		gl.glPopMatrix();
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl
				.reshapWithPerspective(drawable, width, height, 90, 1, 120);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		super.dispose(drawable);
		gl.glDeleteTextures(TEXTURE_COUNT, textures, 0);
	}
}
