package xdean.OpenGLSuperBible.chapter07;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App7_05_Operations extends BaseApp {

	public static void main(String[] args) {
		new App7_05_Operations().setVisible(true);
	}

	IntBuffer imageBuffer;
	ByteBuffer modifiedBuffer;
	IntBuffer viewport = IntBuffer.allocate(4);
	int width, height, type, format;

	int renderMode = 1;
	boolean toSave = false;
	float invertMap[];

	@Override
	protected void frameInit() {
		super.frameInit();
		glutCreateMenu(this::processMenu);
		glutAddMenuEntry("Save Image", 0);
		glutAddMenuEntry("Draw Pixels", 1);
		glutAddMenuEntry("Flip Pixels", 2);
		glutAddMenuEntry("Zoom Pixels", 3);
		glutAddMenuEntry("Just Red Channel", 4);
		glutAddMenuEntry("Just Green Channel", 5);
		glutAddMenuEntry("Just Blue Channel", 6);
		glutAddMenuEntry("Black and White", 7);
		glutAddMenuEntry("Invert Colors", 8);
	}

	void processMenu(int value) {
		if (value == 0)
			toSave = true;
		else
			renderMode = value;
		glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

		IntWrapper iWidth = new IntWrapper(), iHeight = new IntWrapper(), iComponents = new IntWrapper(), eFormat = new IntWrapper();
		imageBuffer = glt.gltLoadTGA("horse.tga", iWidth, iHeight, iComponents,
				eFormat);
		this.width = iWidth.get();
		this.height = iHeight.get();
		this.type = iComponents.get();
		this.format = eFormat.get();

		invertMap = new float[256];
		invertMap[0] = 1.0f;
		for (int i = 1; i < 256; i++)
			invertMap[i] = 1.0f - (1.0f / 255.0f * (float) i);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if (toSave){
			glt.gltWriteTGA("ScreenShot.tga");
			toSave = false;
		}

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glRasterPos2i(0, 0);

		switch (renderMode) {
		case 2:
			gl.glPixelZoom(-1.0f, -1.0f);
			gl.glRasterPos2i(width, height);
			break;

		case 3:
			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
			gl.glPixelZoom((float) viewport.get(2) / (float) width,
					(float) viewport.get(3) / (float) height);
			break;

		case 4:
			gl.glPixelTransferf(GL2.GL_RED_SCALE, 1.0f);
			gl.glPixelTransferf(GL2.GL_GREEN_SCALE, 0.0f);
			gl.glPixelTransferf(GL2.GL_BLUE_SCALE, 0.0f);
			break;

		case 5:
			gl.glPixelTransferf(GL2.GL_RED_SCALE, 0.0f);
			gl.glPixelTransferf(GL2.GL_GREEN_SCALE, 1.0f);
			gl.glPixelTransferf(GL2.GL_BLUE_SCALE, 0.0f);
			break;

		case 6:
			gl.glPixelTransferf(GL2.GL_RED_SCALE, 0.0f);
			gl.glPixelTransferf(GL2.GL_GREEN_SCALE, 0.0f);
			gl.glPixelTransferf(GL2.GL_BLUE_SCALE, 1.0f);
			break;

		case 7:
			gl.glDrawPixels(width, height, format, GL2.GL_UNSIGNED_BYTE,
					imageBuffer);

			modifiedBuffer = ByteBuffer.allocate(width * height);

			gl.glPixelTransferf(GL2.GL_RED_SCALE, 0.3f);
			gl.glPixelTransferf(GL2.GL_GREEN_SCALE, 0.59f);
			gl.glPixelTransferf(GL2.GL_BLUE_SCALE, 0.11f);

			gl.glReadPixels(0, 0, width, height, GL2.GL_LUMINANCE,
					GL2.GL_UNSIGNED_BYTE, modifiedBuffer);

			gl.glPixelTransferf(GL2.GL_RED_SCALE, 1.0f);
			gl.glPixelTransferf(GL2.GL_GREEN_SCALE, 1.0f);
			gl.glPixelTransferf(GL2.GL_BLUE_SCALE, 1.0f);
			break;

		case 8:
			gl.glPixelMapfv(GL2.GL_PIXEL_MAP_R_TO_R, 255, invertMap, 0);
			gl.glPixelMapfv(GL2.GL_PIXEL_MAP_G_TO_G, 255, invertMap, 0);
			gl.glPixelMapfv(GL2.GL_PIXEL_MAP_B_TO_B, 255, invertMap, 0);
			gl.glPixelTransferi(GL2.GL_MAP_COLOR, GL2.GL_TRUE);
			break;

		case 1:
		default:
			break;
		}

		if (modifiedBuffer == null)
			gl.glDrawPixels(width, height, format, type, imageBuffer);
		else {
			gl.glDrawPixels(width, height, GL2.GL_LUMINANCE,
					GL2.GL_UNSIGNED_BYTE, modifiedBuffer);
			modifiedBuffer.clear();
			modifiedBuffer = null;
		}

		gl.glPixelTransferi(GL2.GL_MAP_COLOR, GL2.GL_FALSE);
		gl.glPixelTransferf(GL2.GL_RED_SCALE, 1.0f);
		gl.glPixelTransferf(GL2.GL_GREEN_SCALE, 1.0f);
		gl.glPixelTransferf(GL2.GL_BLUE_SCALE, 1.0f);
		gl.glPixelZoom(1.0f, 1.0f);
		gl.glFlush();

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeBySize(drawable, width, height);
	}

}
