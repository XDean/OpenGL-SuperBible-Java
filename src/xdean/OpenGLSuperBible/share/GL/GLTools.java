package xdean.OpenGLSuperBible.share.GL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import xdean.OpenGLSuperBible.share.Util;
import xdean.OpenGLSuperBible.share.TGA.TGAReader;
import xdean.OpenGLSuperBible.share.TGA.TGAWriter;
import xdean.OpenGLSuperBible.share.base.Wrapper.IntWrapper;

import com.jogamp.opengl.GL2;

public class GLTools {

	private static final int DEFAULT_FORMAT = GL2.GL_RGBA;
	private static final int DEFAULT_TYPE = GL2.GL_UNSIGNED_INT_8_8_8_8;
	
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
		gl.glReadPixels(0, 0, width, height, DEFAULT_FORMAT, DEFAULT_TYPE, pixels);
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
}
