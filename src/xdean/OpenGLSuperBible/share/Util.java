package xdean.OpenGLSuperBible.share;

public class Util {

	public static byte[] intArrayToByteArray(int[] is) {
		byte[] bs = new byte[is.length];
		for (int i = 0; i < is.length; i++)
			bs[i] = (byte) is[i];
		return bs;
	}

}
