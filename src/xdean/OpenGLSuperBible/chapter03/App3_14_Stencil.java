package xdean.OpenGLSuperBible.chapter03;

import xdean.OpenGLSuperBible.chapter02.App2_03_Bounce;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;

public class App3_14_Stencil extends App2_03_Bounce {

	public static void main(String[] args) {
		new App3_14_Stencil().setVisible(true);
	}

	@Override
	protected GLCapabilities getGLCapabilities() {
		GLCapabilities glCapabilities = super.getGLCapabilities();
		glCapabilities.setStencilBits(8);
		return glCapabilities;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		double dRadius = 0.1;
		double dAngle;
		
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);

		gl.glClearStencil(0);
		gl.glEnable(GL2.GL_STENCIL_TEST);

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);

		gl.glStencilFunc(GL2.GL_NEVER, 0x0, 0x0);
		gl.glStencilOp(GL2.GL_INCR, GL2.GL_INCR, GL2.GL_INCR);

		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin(GL2.GL_LINE_STRIP);
		for (dAngle = 0; dAngle < 400.0; dAngle += 0.1, dRadius *= 1.002)
			gl.glVertex2d(dRadius * Math.cos(dAngle),
					dRadius * Math.sin(dAngle));

		gl.glEnd();

		gl.glStencilFunc(GL2.GL_NOTEQUAL, 0x1, 0x1);
		gl.glStencilOp(GL2.GL_KEEP, GL2.GL_KEEP, GL2.GL_KEEP);

		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glRectf(x, y, x + rsize, y - rsize);

		gl.glFlush();
	}

}
