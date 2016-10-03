package xdean.OpenGLSuperBible.chapter07;

import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App7_02_ImageLoad extends BaseApp {

	private IntBuffer image;
	private IntWrapper width;
	private IntWrapper height;
	private IntWrapper type;
	private IntWrapper format;

	public static void main(String[] args) {
		new App7_02_ImageLoad().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		width = new IntWrapper();
		height = new IntWrapper();
		type = new IntWrapper();
		format = new IntWrapper();
		image = glt.gltLoadTGA("fire.tga", width, height, type, format);
	}

	@Override
	public void display(GLAutoDrawable drawable) {

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glRasterPos2i(0, 0);
		if (image != null)
			gl.glDrawPixels(width.get(), height.get(), format.get(),
					type.get(), image);

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeBySize(drawable, width, height, 1);
	}

}
