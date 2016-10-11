package xdean.OpenGLSuperBible.chapter11;

import xdean.OpenGLSuperBible.share.BaseApp;

public abstract class App11_02_AirplaneBase extends BaseApp {

	protected static final short[] face_indicies, face_indiciesGlass;
	protected static final float[] vertices, normals, textures;
	protected static final float[] verticesGlass, normalsGlass, texturesGlass;
	static {
		// XXX: Alter the library path if you move the DLL file.
		// -Djava.library.path="${workspace_loc}\OpenGLSuperBible\lib\chapter11;${env_var:PATH}"
		try {
			System.load(System.getProperty("user.dir")
					+ "\\lib\\chapter11\\libAirplaneData.dll");
		} catch (Exception e) {
			System.err.println("load data error");
			e.printStackTrace();
			System.exit(1);
		}

		face_indicies = getBodyFaceIndicies();
		vertices = getBodyVertices();
		normals = getBodyNormals();
		textures = getBodyTextures();
		face_indiciesGlass = getGlassFaceIndicies();
		verticesGlass = getGlassVertices();
		normalsGlass = getGlassNormals();
		texturesGlass = getGlassTextures();
	}

	private static native short[] getBodyFaceIndicies();

	private static native float[] getBodyVertices();

	private static native float[] getBodyNormals();

	private static native float[] getBodyTextures();

	private static native short[] getGlassFaceIndicies();

	private static native float[] getGlassVertices();

	private static native float[] getGlassNormals();

	private static native float[] getGlassTextures();

}
