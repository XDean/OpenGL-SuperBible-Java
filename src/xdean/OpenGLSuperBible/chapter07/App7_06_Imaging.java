package xdean.OpenGLSuperBible.chapter07;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.BaseApp;
import xdean.OpenGLSuperBible.share.GL.GLDefaultImpl;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App7_06_Imaging extends BaseApp {

	public static void main(String[] args) {
		new App7_06_Imaging().setVisible(true);
	}

	float lumMat[] = new float[] { 0.30f, 0.30f, 0.30f, 0.0f, 0.59f, 0.59f,
			0.59f, 0.0f, 0.11f, 0.11f, 0.11f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
	ByteBuffer invertTable = ByteBuffer.allocate(256 * 3);
	FloatBuffer sharpen = FloatBuffer.wrap(new float[] { 0.0f, -1.0f, 0.0f,
			-1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f });
	FloatBuffer emboss = FloatBuffer.wrap(new float[] { 2.0f, 0.0f, 0.0f, 0.0f,
			-1.0f, 0.0f, 0.0f, 0.0f, -1.0f });
	IntBuffer histoGram = IntBuffer.allocate(256);
	IntBuffer imageBuffer;
	int width, height, type, format;

	int renderMode = 1;
	boolean histogram = false;
	boolean toSave = false;

	@Override
	protected void frameInit() {
		super.frameInit();
		glut.glutCreateMenu(this::processMenu);
		glut.glutAddMenuEntry("Save Image", 0);
		glut.glutAddMenuEntry("Raw Stretched Image", 1);
		glut.glutAddMenuEntry("Increase Contrast", 2);
		glut.glutAddMenuEntry("Invert Color", 3);
		glut.glutAddMenuEntry("Emboss Image", 4);
		glut.glutAddMenuEntry("Sharpen Image", 5);
		glut.glutAddMenuEntry("Histogram", 6);
	}

	void processMenu(int value) {
		if (value == 6)
			histogram = true;
		else if (value == 0)
			toSave = true;
		else
			renderMode = value;
		glut.glutPostRedisplay();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);

		if (glt.gltIsExtSupported("GL_ARB_imaging") == false) {
			System.out.println("Imaging subset not supported");
			System.exit(0);
		}

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);

		IntWrapper iWidth = new IntWrapper(), iHeight = new IntWrapper(), iComponents = new IntWrapper(), eFormat = new IntWrapper();
		imageBuffer = glt.gltLoadTGA("horse.tga", iWidth, iHeight, iComponents,
				eFormat);
		this.width = iWidth.get();
		this.height = iHeight.get();
		this.type = iComponents.get();
		this.format = eFormat.get();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if (toSave) {
			glt.gltWriteTGA("ScreenShot.tga");
			toSave = false;
		}

		int i;
		int iViewport[] = new int[4];
		int iLargest;
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		gl.glRasterPos2i(0, 0);
		gl.glGetIntegerv(GL2.GL_VIEWPORT, iViewport, 0);
		gl.glPixelZoom((float) iViewport[2] / (float) width,
				(float) iViewport[3] / (float) height);

		if (histogram == true) {
			gl.glMatrixMode(GL2.GL_COLOR);
			gl.glLoadMatrixf(lumMat, 0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);

			gl.glHistogram(GL2.GL_HISTOGRAM, 256, GL2.GL_LUMINANCE, false);
			gl.glEnable(GL2.GL_HISTOGRAM);
		}

		switch (renderMode) {
		case 5:
			gl.glConvolutionFilter2D(GL2.GL_CONVOLUTION_2D, GL2.GL_RGB, 3, 3,
					GL2.GL_LUMINANCE, GL2.GL_FLOAT, sharpen);
			gl.glEnable(GL2.GL_CONVOLUTION_2D);
			break;

		case 4:
			gl.glConvolutionFilter2D(GL2.GL_CONVOLUTION_2D, GL2.GL_RGB, 3, 3,
					GL2.GL_LUMINANCE, GL2.GL_FLOAT, emboss);
			gl.glEnable(GL2.GL_CONVOLUTION_2D);
			gl.glMatrixMode(GL2.GL_COLOR);
			gl.glLoadMatrixf(lumMat, 0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			break;

		case 3:
			for (i = 0; i < 255; i++) {
				invertTable.put(i * 3, (byte) (255 - i));
				invertTable.put(i * 3 + 1, (byte) (255 - i));
				invertTable.put(i * 3 + 2, (byte) (255 - i));
			}
			gl.glColorTable(GL2.GL_COLOR_TABLE, GL2.GL_RGB, 256, GL2.GL_RGB,
					GL2.GL_UNSIGNED_BYTE, invertTable);
			gl.glEnable(GL2.GL_COLOR_TABLE);
			break;

		case 2:
			gl.glMatrixMode(GL2.GL_COLOR);
			gl.glScalef(1.25f, 1.25f, 1.25f);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			break;

		case 1:
		default:
			break;
		}

		gl.glDrawPixels(width, height, format, type, imageBuffer);

		if (histogram == true) {
			gl.glGetHistogram(GL2.GL_HISTOGRAM, true, GL2.GL_LUMINANCE,
					GL2.GL_INT, histoGram);

			iLargest = 0;
			for (i = 0; i < 255; i++)
				if (iLargest < histoGram.get(i))
					iLargest = histoGram.get(i);

			gl.glColor3f(1.0f, 1.0f, 1.0f);
			gl.glBegin(GL2.GL_LINE_STRIP);
			for (i = 0; i < 255; i++)
				gl.glVertex2f((float) i, (float) histoGram.get(i)
						/ (float) iLargest * 128.0f);
			gl.glEnd();

			histogram = false;
			gl.glDisable(GL2.GL_HISTOGRAM);
		}

		gl.glMatrixMode(GL2.GL_COLOR);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glDisable(GL2.GL_CONVOLUTION_2D);
		gl.glDisable(GL2.GL_COLOR_TABLE);

		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GLDefaultImpl.reshapeBySize(drawable, width, height);
	}

}
