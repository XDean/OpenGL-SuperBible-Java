package xdean.OpenGLSuperBible.share;

import java.io.File;
import java.io.IOException;

public class Util {

	public static byte[] intArrayToByteArray(int[] is) {
		byte[] bs = new byte[is.length];
		for (int i = 0; i < is.length; i++)
			bs[i] = (byte) is[i];
		return bs;
	}

	public static void createFile(File f) throws IOException {
		if (f.exists())
			return;
		f.getParentFile().mkdirs();
		f.createNewFile();
	}

	public static void createFile(String filePath) throws IOException {
		createFile(new File(filePath));
	}
}
