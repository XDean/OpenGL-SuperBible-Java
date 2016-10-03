package xdean.OpenGLSuperBible.chapter03;

import java.nio.FloatBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App3_06_LinesW extends BaseApp {

	public static void main(String[] args) {
		new App3_06_LinesW().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glColor3f(0f, 1f, 0f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		float y, curSize;
		FloatBuffer sizes = FloatBuffer.allocate(2);

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glGetFloatv(GL2.GL_LINE_WIDTH_RANGE, sizes);

		curSize = sizes.get(0) + 0.1f;

		for (y = -90f; y <= 90; y += 20, curSize++) {
			gl.glLineWidth(curSize);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex2f(-80, y);
			gl.glVertex2f(80, y);
			gl.glEnd();
		}
		gl.glFlush();
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeAtRadio(drawable, width, height, 100);
	}
}
