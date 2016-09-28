package xdean.OpenGLSuperBible.chapter06;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class App6_03_Fogged extends App6_01_SphereWorld3 {
	public static void main(String[] args) {
		new App6_03_Fogged().setVisible(true);
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		super.init(drawable);
		gl.glEnable(GL2.GL_FOG);
		gl.glFogfv(GL2.GL_FOG_COLOR, fLowLight);
//		gl.glFogf(GL2.GL_FOG_START, 5f);
//		gl.glFogf(GL2.GL_FOG_END, 50);
		gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP2);
		gl.glFogf(GL2.GL_FOG_DENSITY, 0.3f);
	}
}
