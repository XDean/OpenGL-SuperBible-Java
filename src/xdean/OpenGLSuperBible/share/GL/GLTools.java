package xdean.OpenGLSuperBible.share.GL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.Math3d;
import xdean.OpenGLSuperBible.share.Util;
import xdean.OpenGLSuperBible.share.TGA.TGAReader;
import xdean.OpenGLSuperBible.share.TGA.TGAWriter;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;

public class GLTools {

	private static final int DEFAULT_FORMAT = GL2.GL_RGBA;
	private static final int DEFAULT_TYPE = GL2.GL_UNSIGNED_INT_8_8_8_8;

	private static final float PI = 3.1415926f;

	GL2 gl;

	public GLTools(GL2 gl) {
		this.gl = gl;
	}

	public IntBuffer gltLoadTGA(String fileName, IntWrapper width,
			IntWrapper height, IntWrapper type, IntWrapper format) {
		try {
			File file = new File(fileName);
			if (!file.exists())
				file = new File("image/" + fileName);
			if (!file.exists()) {
				throw new FileNotFoundException(file.toString());
			}
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

			int[] pixels;
			pixels = TGAReader.read(buffer, TGAReader.RGBA);
			width.set(TGAReader.getWidth(buffer));
			height.set(TGAReader.getHeight(buffer));
			format.set(DEFAULT_FORMAT);
			type.set(DEFAULT_TYPE);
			return IntBuffer.wrap(pixels);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void gltWriteTGA(String fileName) {
		IntBuffer viewport = IntBuffer.allocate(4);
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport);
		int width = viewport.get(2);
		int height = viewport.get(3);
		IntBuffer lastBuffer = IntBuffer.allocate(1);

		IntBuffer pixels = IntBuffer.allocate(width * 3 * height);

		gl.glPixelStorei(GL2.GL_PACK_ALIGNMENT, 1);
		gl.glPixelStorei(GL2.GL_PACK_ROW_LENGTH, 0);
		gl.glPixelStorei(GL2.GL_PACK_SKIP_ROWS, 0);
		gl.glPixelStorei(GL2.GL_PACK_SKIP_PIXELS, 0);

		gl.glGetIntegerv(GL2.GL_READ_BUFFER, lastBuffer);
		gl.glReadBuffer(GL2.GL_FRONT);
		gl.glReadPixels(0, 0, width, height, DEFAULT_FORMAT, DEFAULT_TYPE,
				pixels);
		gl.glReadBuffer(lastBuffer.get(0));

		try {
			byte[] buffer = TGAWriter.write(pixels.array(), width, height,
					TGAReader.RGBA);
			if (!fileName.toLowerCase().endsWith(".tga")) {
				fileName += ".tga";
			}
			String filePath = "output/" + fileName;
			Util.createFile(filePath);
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(buffer);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean gltIsExtSupported(String extension) {
		// #ifndef OPENGL_ES
		int[] nNumExtensions = new int[1];
		gl.glGetIntegerv(GL2.GL_NUM_EXTENSIONS, nNumExtensions, 0);

		for (int i = 0; i < nNumExtensions[0]; i++)
			if (gl.glGetStringi(GL2.GL_EXTENSIONS, i).equals(extension))
				return true;
		// #else
		// GLubyte *extensions = NULL;
		// const GLubyte *start;
		// GLubyte *where, *terminator;
		//
		// where = (GLubyte *) strchr(extension, ' ');
		// if (where || *extension == '\0')
		// return 0;
		//
		// extensions = (GLubyte *)glGetString(GL_EXTENSIONS);
		//
		// start = extensions;
		// for (;;)
		// {
		// where = (GLubyte *) strstr((const char *) start, extension);
		//
		// if (!where)
		// break;
		//
		// terminator = where + strlen(extension);
		//
		// if (where == start || *(where - 1) == ' ')
		// {
		// if (*terminator == ' ' || *terminator == '\0')
		// return 1;
		// }
		// start = terminator;
		// }
		// #endif
		return false;
	}

	public void gltDrawTorus(float majorRadius, float minorRadius,
			int numMajor, int numMinor) {
		float[] vNormal = new float[3];
		double majorStep = 2.0f * PI / numMajor;
		double minorStep = 2.0f * PI / numMinor;
		int i, j;

		for (i = 0; i < numMajor; ++i) {
			double a0 = i * majorStep;
			double a1 = a0 + majorStep;
			float x0 = (float) Math.cos(a0);
			float y0 = (float) Math.sin(a0);
			float x1 = (float) Math.cos(a1);
			float y1 = (float) Math.sin(a1);

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			for (j = 0; j <= numMinor; ++j) {
				double b = j * minorStep;
				float c = (float) Math.cos(b);
				float r = minorRadius * c + majorRadius;
				float z = minorRadius * (float) Math.sin(b);

				gl.glTexCoord2f((float) (i) / (float) (numMajor), (float) (j)
						/ (float) (numMinor));
				vNormal[0] = x0 * c;
				vNormal[1] = y0 * c;
				vNormal[2] = z / minorRadius;
				Math3d.m3dNormalizeVector(vNormal);
				gl.glNormal3fv(vNormal, 0);
				gl.glVertex3f(x0 * r, y0 * r, z);

				gl.glTexCoord2f((float) (i + 1) / (float) (numMajor),
						(float) (j) / (float) (numMinor));
				vNormal[0] = x1 * c;
				vNormal[1] = y1 * c;
				vNormal[2] = z / minorRadius;
				gl.glNormal3fv(vNormal, 0);
				gl.glVertex3f(x1 * r, y1 * r, z);
			}
			gl.glEnd();
		}
	}

	public void gltDrawSphere(float fRadius, int iSlices, int iStacks) {
		float drho = (float) (3.141592653589) / (float) iStacks;
		float dtheta = 2.0f * (float) (3.141592653589) / (float) iSlices;
		float ds = 1.0f / (float) iSlices;
		float dt = 1.0f / (float) iStacks;
		float t = 1.0f;
		float s = 0.0f;

		for (int i = 0; i < iStacks; i++) {
			float rho = (float) i * drho;
			float srho = (float) (Math.sin(rho));
			float crho = (float) (Math.cos(rho));
			float srhodrho = (float) (Math.sin(rho + drho));
			float crhodrho = (float) (Math.cos(rho + drho));

			gl.glBegin(GL2.GL_TRIANGLE_STRIP);
			s = 0.0f;
			for (int j = 0; j <= iSlices; j++) {
				float theta = (j == iSlices) ? 0.0f : j * dtheta;
				float stheta = (float) (-Math.sin(theta));
				float ctheta = (float) (Math.cos(theta));

				float x = stheta * srho;
				float y = ctheta * srho;
				float z = crho;

				gl.glTexCoord2f(s, t);
				gl.glNormal3f(x, y, z);
				gl.glVertex3f(x * fRadius, y * fRadius, z * fRadius);

				x = stheta * srhodrho;
				y = ctheta * srhodrho;
				z = crhodrho;
				gl.glTexCoord2f(s, t - dt);

				s += ds;
				gl.glNormal3f(x, y, z);
				gl.glVertex3f(x * fRadius, y * fRadius, z * fRadius);
			}
			gl.glEnd();

			t -= dt;
		}
	}
}
