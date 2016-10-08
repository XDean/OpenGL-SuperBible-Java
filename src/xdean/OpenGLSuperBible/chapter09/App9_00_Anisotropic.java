package xdean.OpenGLSuperBible.chapter09;

import java.nio.FloatBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import xdean.OpenGLSuperBible.chapter08.App8_03_Tunnel;

/**
 * 
 * @see App8_03_Tunnel
 * @author XDean
 *
 */
public class App9_00_Anisotropic extends App8_03_Tunnel {

	private float fLargest;

	public static void main(String[] args) {
		new App9_00_Anisotropic().setVisible(true);
	}

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutAddMenuEntry("Anisotropic Filter", 6);
		glut.glutAddMenuEntry("Anisotropic Off", 7);
	}

	@Override
	protected void processMenu(int value) {
		for (int iLoop = 0; iLoop < TEXTURE_COUNT; iLoop++) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, textures[iLoop]);
			switch (value) {
			case 6:
				gl.glTexParameterf(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, fLargest);
				break;

			case 7:
				gl.glTexParameterf(GL2.GL_TEXTURE_2D,
						GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, 1.0f);
				break;
			}
		}
		super.processMenu(value);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		if (glt.gltIsExtSupported("GL_EXT_texture_filter_anisotropic")) {
			FloatBuffer fLargest = FloatBuffer.allocate(1);
			gl.glGetFloatv(GL2.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, fLargest);
			this.fLargest = fLargest.get(0);
		} else
			System.out.println("Anisotropic not supported");
	}

}
