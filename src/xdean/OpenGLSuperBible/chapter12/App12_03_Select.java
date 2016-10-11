package xdean.OpenGLSuperBible.chapter12;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App12_03_Select extends BaseApp {

	public static void main(String[] args) {
		new App12_03_Select().setVisible(true);
	}

	private static class Rectangle {
		float top;
		float bottom;
		float left;
		float right;
	};

	private static final int TORUS = 1;
	private static final int SPHERE = 2;

	private static final int FEED_BUFF_SIZE = 32768;
	private static final int BUFFER_LENGTH = 64;

	Rectangle boundingRect = new Rectangle();
	int selectedObject = 0;
	FloatBuffer feedBackBuff = Buffers.newDirectFloatBuffer(FEED_BUFF_SIZE);
	IntBuffer selectBuff = Buffers.newDirectIntBuffer(BUFFER_LENGTH);

	void MouseCallback(int button, int state, int x, int y) {
		if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN)
			ProcessSelection(x, y);
	}

	void ProcessSelection(int xPos, int yPos) {
		float fAspect;
		int hits;
		IntBuffer viewport = IntBuffer.allocate(4);

		gl.glSelectBuffer(BUFFER_LENGTH, selectBuff);

		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPushMatrix();

		gl.glRenderMode(GL2.GL_SELECT);

		gl.glLoadIdentity();
		glu.gluPickMatrix(xPos, viewport.get(3) - yPos + viewport.get(1), 2, 2,
				viewport);

		fAspect = (float) viewport.get(2) / (float) viewport.get(3);
		glu.gluPerspective(60.0f, fAspect, 1.0, 425.0);

		DrawObjects();

		hits = gl.glRenderMode(GL2.GL_RENDER);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		if (hits == 1) {
			MakeSelection(selectBuff.get(3));
			if (selectedObject == selectBuff.get(3))
				selectedObject = 0;
			else
				selectedObject = selectBuff.get(3);
		}

		glut.glutPostRedisplay();
	}

	void MakeSelection(int nChoice) {
		int size, i, j, count;

		boundingRect.right = boundingRect.bottom = -999999.0f;
		boundingRect.left = boundingRect.top = 999999.0f;

		gl.glFeedbackBuffer(FEED_BUFF_SIZE, GL2.GL_2D, feedBackBuff);

		gl.glRenderMode(GL2.GL_FEEDBACK);

		DrawObjects();

		size = gl.glRenderMode(GL2.GL_RENDER);

		i = 0;
		while (i < size) {
			if (feedBackBuff.get(i) == GL2.GL_PASS_THROUGH_TOKEN)
				if (feedBackBuff.get(i + 1) == (float) nChoice) {
					i += 2;
					while (i < size
							&& feedBackBuff.get(i) != GL2.GL_PASS_THROUGH_TOKEN) {
						if (feedBackBuff.get(i) == GL2.GL_POLYGON_TOKEN) {
							count = (int) feedBackBuff.get(++i);
							i++;

							for (j = 0; j < count; j++) {
								if (feedBackBuff.get(i) > boundingRect.right)
									boundingRect.right = feedBackBuff.get(i);

								if (feedBackBuff.get(i) < boundingRect.left)
									boundingRect.left = feedBackBuff.get(i);
								i++;

								if (feedBackBuff.get(i) > boundingRect.bottom)
									boundingRect.bottom = feedBackBuff.get(i);

								if (feedBackBuff.get(i) < boundingRect.top)
									boundingRect.top = feedBackBuff.get(i);
								i++;
							}
						} else
							i++;
					}
					break;
				}
			i++;
		}
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		glut.glutMouseFunc(this::MouseCallback);

		float dimLight[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		float sourceLight[] = { 0.65f, 0.65f, 0.65f, 1.0f };
		float lightPos[] = { 0.0f, 0.0f, 0.0f, 1.0f };

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, dimLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, sourceLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glEnable(GL2.GL_LIGHT0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

		gl.glClearColor(0.60f, 0.60f, 0.60f, 1.0f);
		gl.glLineWidth(2.0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		DrawObjects();

		if (selectedObject != 0) {
			IntBuffer viewport = IntBuffer.allocate(4);

			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();

			gl.glOrtho(viewport.get(0), viewport.get(2), viewport.get(3),
					viewport.get(1), -1, 1);
			gl.glMatrixMode(GL2.GL_MODELVIEW);

			gl.glDisable(GL2.GL_LIGHTING);
			gl.glColor3f(1.0f, 0.0f, 0.0f);
			gl.glBegin(GL2.GL_LINE_LOOP);
			gl.glVertex2f(boundingRect.left, boundingRect.top);
			gl.glVertex2f(boundingRect.left, boundingRect.bottom);
			gl.glVertex2f(boundingRect.right, boundingRect.bottom);
			gl.glVertex2f(boundingRect.right, boundingRect.top);
			gl.glEnd();
			gl.glEnable(GL2.GL_LIGHTING);
		}

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		glut.glutSwapBuffers();
	}

	void DrawObjects() {
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glPushMatrix();

		gl.glTranslatef(-0.75f, 0.0f, -2.5f);

		gl.glInitNames();
		gl.glPushName(0);

		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glLoadName(TORUS);
		gl.glPassThrough((float) TORUS);
		glt.gltDrawTorus(0.35f, 0.15f, 40, 20);

		gl.glColor3f(0.5f, 0.0f, 0.0f);
		gl.glTranslatef(1.5f, 0.0f, 0.0f);
		gl.glLoadName(SPHERE);
		gl.glPassThrough((float) SPHERE);
		glt.gltDrawSphere(0.5f, 26, 13);

		gl.glPopMatrix();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl
				.reshapWithPerspective(drawable, width, height, 60, 1, 425);
	}

}
