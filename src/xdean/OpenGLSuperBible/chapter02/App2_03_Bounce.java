package xdean.OpenGLSuperBible.chapter02;

import xdean.OpenGLSuperBible.share.BaseApp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App2_03_Bounce extends BaseApp {

	public static void main(String[] args) {
		new App2_03_Bounce().setVisible(true);
	}

	protected float x = 0f;
	protected float y = 0f;
	protected float rsize = 25;
	private float xstep = 1.5f;
	private float ystep = 1.5f;
	private float windowWidth = 100;
	private float windowHeight = 100;
	private int timeGap = 33;

//	private Random r = new Random();

	private void timerFunction(int value) {
		if (x > windowWidth - rsize || x < -windowWidth)
			xstep = -xstep;
		if (y > windowHeight || y < -windowHeight + rsize)
			ystep = -ystep;
		x += xstep;
		y += ystep;
//		xstep = (float) Math.min(xstep + r.nextFloat() - 0.45, 8);
//		ystep = (float) Math.min(ystep + r.nextFloat() - 0.45, 8);

		if (x > (windowWidth - rsize + xstep))
			x = windowWidth - rsize - 0.1f;
		else if (x < -(windowWidth + xstep))
			x = -windowWidth - 0.1f;
		if (y > windowHeight + ystep)
			y = windowHeight - 0.1f;
		else if (y < -(windowHeight - rsize + ystep))
			y = -windowHeight + rsize - 0.1f;
		glut.glutPostRedisplay();
		glut.glutTimerFunc(timeGap, this::timerFunction, 1);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 1f, 1f);
		glut.glutTimerFunc(timeGap, this::timerFunction, 1);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glColor3f(1f, 0f, 0f);
		gl.glRectf(x, y, x + rsize, y - rsize);
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = ((float) width) / ((float) height);
		if (ratio <= 1) {
			windowWidth = 100;
			windowHeight = 100 / ratio;
		} else {
			windowWidth = 100 * ratio;
			windowHeight = 100;
		}
		gl.glOrthof(-windowWidth, windowWidth, -windowHeight, windowHeight, 1,
				-1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

	}

}
