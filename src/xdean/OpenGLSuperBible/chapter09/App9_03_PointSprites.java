package xdean.OpenGLSuperBible.chapter09;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App9_03_PointSprites extends BaseApp {

	public static void main(String[] args) {
		new App9_03_PointSprites().setVisible(true);
	}

	private static final int SMALL_STARS = 100;
	float[] vSmallStars[] = new float[SMALL_STARS][2];

	private static final int MEDIUM_STARS = 40;
	float[] vMediumStars[] = new float[MEDIUM_STARS][2];

	private static final int LARGE_STARS = 15;
	float[] vLargeStars[] = new float[LARGE_STARS][2];

	private static final int SCREEN_X = 1600;
	private static final int SCREEN_Y = 900;

	int drawMode = 1;
	int textureObjects[] = new int[2];
	Random r = new Random();
	List<int[]> horizon;

	@Override
	protected void frameInit() {
		super.frameInit();
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		glut.glutCreateMenu(this::ProcessMenu);
		glut.glutAddMenuEntry("Normal Points", 1);
		glut.glutAddMenuEntry("Antialiased Points", 2);
		glut.glutAddMenuEntry("Point Sprites", 3);
	}

	void ProcessMenu(int value) {
		drawMode = value;

		switch (value) {
		case 1:
			gl.glDisable(GL2.GL_BLEND);
			gl.glDisable(GL2.GL_LINE_SMOOTH);
			gl.glDisable(GL2.GL_POINT_SMOOTH);
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glDisable(GL2.GL_POINT_SPRITE);
			break;

		case 2:
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL2.GL_BLEND);
			gl.glEnable(GL2.GL_POINT_SMOOTH);
			gl.glHint(GL2.GL_POINT_SMOOTH_HINT, GL2.GL_NICEST);
			gl.glEnable(GL2.GL_LINE_SMOOTH);
			gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
			gl.glDisable(GL2.GL_TEXTURE_2D);
			gl.glDisable(GL2.GL_POINT_SPRITE);
			break;

		case 3:
			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_COLOR, GL2.GL_ONE_MINUS_SRC_COLOR);
			gl.glDisable(GL2.GL_LINE_SMOOTH);
			gl.glDisable(GL2.GL_POINT_SMOOTH);
			gl.glDisable(GL2.GL_POLYGON_SMOOTH);
			break;

		default:
			break;
		}

		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		int i;

		for (i = 0; i < SMALL_STARS; i++) {
			vSmallStars[i][0] = (float) (r.nextInt(Integer.MAX_VALUE) % SCREEN_X);
			vSmallStars[i][1] = (float) (r.nextInt(Integer.MAX_VALUE) % (SCREEN_Y - 100)) + 100.0f;
		}

		for (i = 0; i < MEDIUM_STARS; i++) {
			vMediumStars[i][0] = (float) (r.nextInt(Integer.MAX_VALUE)
					% SCREEN_X * 10) / 10.0f;
			vMediumStars[i][1] = (float) (r.nextInt(Integer.MAX_VALUE) % (SCREEN_Y - 100)) + 100.0f;
		}

		for (i = 0; i < LARGE_STARS; i++) {
			vLargeStars[i][0] = (float) (r.nextInt(Integer.MAX_VALUE)
					% SCREEN_X * 10) / 10.0f;
			vLargeStars[i][1] = (float) (r.nextInt(Integer.MAX_VALUE)
					% (SCREEN_Y - 100) * 10.0f) / 10.0f + 100.0f;
		}

		horizon = new ArrayList<>();
		boolean even = true;
		for (int lineX = 0; lineX < SCREEN_X; lineX += r.nextInt(50) + 50, even = !even)
			horizon.add(new int[] { lineX, r.nextInt(75) * (even ? 1 : -1) + 50 });
		horizon.add(new int[] { SCREEN_X, r.nextInt(50) + 50 });

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		gl.glColor3f(0.0f, 0.0f, 0.0f);

		gl.glGenTextures(2, textureObjects, 0);
		IntBuffer pBytes;
		IntWrapper iWidth = new IntWrapper(), iHeight = new IntWrapper();
		IntWrapper iComponents = new IntWrapper(), eFormat = new IntWrapper();

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[0]);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP,
				GL2.GL_TRUE);

		pBytes = glt.gltLoadTGA("star.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[1]);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP,
				GL2.GL_TRUE);
		pBytes = glt.gltLoadTGA("moon.tga", iWidth, iHeight, iComponents,
				eFormat);
		gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, eFormat.get(), iWidth.get(),
				iHeight.get(), 0, eFormat.get(), iComponents.get(), pBytes);

		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER,
				GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
				GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S,
				GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T,
				GL2.GL_CLAMP_TO_EDGE);

		gl.glTexEnvi(GL2.GL_POINT_SPRITE, GL2.GL_COORD_REPLACE, GL2.GL_TRUE);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);

		ProcessMenu(3);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		int i;
		float moonX = SCREEN_X - 100;
		float moonY = SCREEN_Y - 200;

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glColor3f(1.0f, 1.0f, 1.0f);

		if (drawMode == 3) {
			gl.glEnable(GL2.GL_POINT_SPRITE);
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[0]);
			gl.glEnable(GL2.GL_BLEND);
		}

		gl.glPointSize(7.0f);
		gl.glBegin(GL2.GL_POINTS);
		for (i = 0; i < SMALL_STARS; i++)
			gl.glVertex2fv(vSmallStars[i], 0);
		gl.glEnd();

		gl.glPointSize(12.0f);
		gl.glBegin(GL2.GL_POINTS);
		for (i = 0; i < MEDIUM_STARS; i++)
			gl.glVertex2fv(vMediumStars[i], 0);
		gl.glEnd();

		gl.glPointSize(20.0f);
		gl.glBegin(GL2.GL_POINTS);
		for (i = 0; i < LARGE_STARS; i++)
			gl.glVertex2fv(vLargeStars[i], 0);
		gl.glEnd();

		gl.glPointSize(120.0f);
		if (drawMode == 3) {
			gl.glDisable(GL2.GL_BLEND);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textureObjects[1]);
		}

		gl.glBegin(GL2.GL_POINTS);
		gl.glVertex2f(moonX, moonY);
		gl.glEnd();

		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glDisable(GL2.GL_POINT_SPRITE);

		gl.glLineWidth(3.5f);
		gl.glBegin(GL2.GL_LINE_STRIP);

		for (int[] is : horizon)
			gl.glVertex2f(is[0], is[1]);
		gl.glEnd();
		glut.glutSwapBuffers();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeBySize(drawable, SCREEN_X, SCREEN_Y);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		super.dispose(drawable);
		gl.glDeleteTextures(2, textureObjects, 0);
	}
}
