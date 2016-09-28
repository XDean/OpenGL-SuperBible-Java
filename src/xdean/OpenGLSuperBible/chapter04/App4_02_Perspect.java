package xdean.OpenGLSuperBible.chapter04;

import xdean.OpenGLSuperBible.share.DefaultImpl;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App4_02_Perspect extends App4_01_Atom {

	public static void main(String[] args) {
		new App4_02_Perspect().setVisible(true);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		DefaultImpl.reshapWithPerspective(drawable, width, height, 60, 1, 400);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glTranslatef(0.0f, 0.0f, -250.0f);
	}
}
