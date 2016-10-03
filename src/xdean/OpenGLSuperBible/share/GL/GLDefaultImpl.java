package xdean.OpenGLSuperBible.share.GL;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.glu.GLU;

public class GLDefaultImpl {

	public static void reshapeBySize(GLAutoDrawable drawable, int width,
			int height) {
		reshapeBySize(drawable, width, height, 100);
	}
	
	public static void reshapeBySize(GLAutoDrawable drawable, int width,
			int height, int zDeep) {
		GL2 gl = drawable.getGL().getGL2();
		if (height == 0)
			height = 1;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, width, 0, height, 0, zDeep);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public static void reshapeAtRadio(GLAutoDrawable drawable, int width,
			int height, int range) {
		GL2 gl = drawable.getGL().getGL2();
		if (height == 0)
			height = 1;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio = ((float) width) / ((float) height);
		if (ratio <= 1)
			gl.glOrthof(-range, range, -range / ratio, range / ratio, range,
					-range);
		else
			gl.glOrthof(-range * ratio, range * ratio, -range, range, range,
					-range);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public static void reshapWithPerspective(GLAutoDrawable drawable,
			int width, int height, float fovy, float zNear, float zFar) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = GLU.createGLU(gl);
		if (height == 0)
			height = 1;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(fovy, (float) width / (float) height, zNear, zFar);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}
