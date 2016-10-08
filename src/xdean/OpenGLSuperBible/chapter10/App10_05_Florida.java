package xdean.OpenGLSuperBible.chapter10;

import jogamp.opengl.glu.tessellator.GLUtessellatorImpl;
import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUtessellatorCallbackAdapter;

public class App10_05_Florida extends BaseApp {

	public static void main(String[] args) {
		new App10_05_Florida().setVisible(true);
	}

	private static final int COAST_POINTS = 24;
	double vCoast[][] = { { -70.0, 30.0, 0.0 }, { -50.0, 30.0, 0.0 },
			{ -50.0, 27.0, 0.0 }, { -5.0, 27.0, 0.0 }, { 0.0, 20.0, 0.0 },
			{ 8.0, 10.0, 0.0 }, { 12.0, 5.0, 0.0 }, { 10.0, 0.0, 0.0 },
			{ 15.0, -10.0, 0.0 }, { 20.0, -20.0, 0.0 }, { 20.0, -35.0, 0.0 },
			{ 10.0, -40.0, 0.0 }, { 0.0, -30.0, 0.0 }, { -5.0, -20.0, 0.0 },
			{ -12.0, -10.0, 0.0 }, { -13.0, -5.0, 0.0 }, { -12.0, 5.0, 0.0 },
			{ -20.0, 10.0, 0.0 }, { -30.0, 20.0, 0.0 }, { -40.0, 15.0, 0.0 },
			{ -50.0, 15.0, 0.0 }, { -55.0, 20.0, 0.0 }, { -60.0, 25.0, 0.0 },
			{ -70.0, 25.0, 0.0 } };

	private static final int LAKE_POINTS = 4;
	double vLake[][] = { { 10.0, -20.0, 0.0 }, { 15.0, -25.0, 0.0 },
			{ 10.0, -30.0, 0.0 }, { 5.0, -25.0, 0.0 } };

	private static final int DRAW_LOOPS = 0;
	private static final int DRAW_CONCAVE = 1;
	private static final int DRAW_COMPLEX = 2;
	int iMethod = DRAW_LOOPS;

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutCreateMenu(this::ProcessMenu);
		glut.glutAddMenuEntry("Line Loops", DRAW_LOOPS);
		glut.glutAddMenuEntry("Concave Polygon", DRAW_CONCAVE);
		glut.glutAddMenuEntry("Complex Polygon", DRAW_COMPLEX);
		glut.glutAttachMenu(GLUT_RIGHT_BUTTON);
	}

	void ProcessMenu(int value) {
		iMethod = value;

		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glLineWidth(2.0f);
		gl.glEnable(GL2.GL_LINE_SMOOTH);
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	void tessError(int error) {
		String szError = glu.gluErrorString(error);

		glut.glutSetWindowTitle(szError);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		int i;
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		switch (iMethod) {
		case DRAW_LOOPS: {
			gl.glColor3f(0.0f, 0.0f, 0.0f);
			gl.glBegin(GL2.GL_LINE_LOOP);
			for (i = 0; i < COAST_POINTS; i++)
				gl.glVertex3dv(vCoast[i], 0);
			gl.glEnd();

			gl.glBegin(GL2.GL_LINE_LOOP);
			for (i = 0; i < LAKE_POINTS; i++)
				gl.glVertex3dv(vLake[i], 0);
			gl.glEnd();
		}
			break;

		case DRAW_CONCAVE: {
			GLUtessellatorImpl pTess;

			gl.glColor3f(0.0f, 1.0f, 0.0f);

			pTess = (GLUtessellatorImpl) GLU.gluNewTess();

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_BEGIN,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void begin(int type) {
							gl.glBegin(type);
						}
					});

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_END,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void end() {
							gl.glEnd();
						}
					});

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_VERTEX,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void vertex(Object vertexData) {
							gl.glVertex3dv((double[]) vertexData, 0);
						}
					});

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_ERROR,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void error(int errnum) {
							tessError(errnum);
						}
					});

			GLU.gluTessBeginPolygon(pTess, null);

			GLU.gluTessBeginContour(pTess);

			for (i = 0; i < COAST_POINTS; i++)
				GLU.gluTessVertex(pTess, vCoast[i], 0, vCoast[i]);
			GLU.gluTessEndContour(pTess);
			GLU.gluTessEndPolygon(pTess);

			GLU.gluDeleteTess(pTess);
		}
			break;

		case DRAW_COMPLEX: {
			GLUtessellatorImpl pTess;

			gl.glColor3f(0.0f, 1.0f, 0.0f);

			pTess = (GLUtessellatorImpl) GLU.gluNewTess();

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_BEGIN,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void begin(int type) {
							gl.glBegin(type);
						}
					});

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_END,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void end() {
							gl.glEnd();
						}
					});

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_VERTEX,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void vertex(Object vertexData) {
							gl.glVertex3dv((double[]) vertexData, 0);
						}
					});

			GLU.gluTessCallback(pTess, GLU.GLU_TESS_ERROR,
					new GLUtessellatorCallbackAdapter() {
						@Override
						public void error(int errnum) {
							tessError(errnum);
						}
					});

			GLU.gluTessProperty(pTess, GLU.GLU_TESS_WINDING_RULE,
					GLU.GLU_TESS_WINDING_ODD);

			GLU.gluTessBeginPolygon(pTess, null);
			GLU.gluTessBeginContour(pTess);
			for (i = 0; i < COAST_POINTS; i++)
				GLU.gluTessVertex(pTess, vCoast[i], 0, vCoast[i]);
			GLU.gluTessEndContour(pTess);

			GLU.gluTessBeginContour(pTess);
			for (i = 0; i < LAKE_POINTS; i++)
				GLU.gluTessVertex(pTess, vLake[i], 0, vLake[i]);
			GLU.gluTessEndContour(pTess);

			GLU.gluTessEndPolygon(pTess);

			GLU.gluDeleteTess(pTess);
		}
			break;
		}

		glut.glutSwapBuffers();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 80);
	}

}
