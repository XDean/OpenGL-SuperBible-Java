package xdean.OpenGLSuperBible.share;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public abstract class BaseApp extends JFrame implements GLEventListener {

	private static final long serialVersionUID = 1L;

	protected final static float PI = 3.1415f;

	protected static final int GLUT_KEY_LEFT = 0;
	protected static final int GLUT_KEY_UP = 1;
	protected static final int GLUT_KEY_RIGHT = 2;
	protected static final int GLUT_KEY_DOWN = 3;

	protected GLCanvas glcanvas;
	protected GL2 gl;
	protected GLU glu;
	protected GLUT glut;

	public BaseApp(GraphicsConfiguration gc) {
		super(gc);
	}

	public BaseApp(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

	public BaseApp(String title) throws HeadlessException {
		super(title);
	}

	protected BaseApp() {
		super();
		setTitle(this.getClass().getSimpleName());
	}

	@Override
	protected void frameInit() {
		super.frameInit();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (isOpenSpecialKey())
			keyInit();

		glcanvas = new GLCanvas(getGLCapabilities());
		glcanvas.addGLEventListener(this);

		Container contentPane = getContentPane();
		contentPane.setLayout(null);
		contentPane.add(glcanvas);

		setSize(500, 300);
		centerWindow(this);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				glcanvas.setBounds(0, 0, getWidth(), getHeight());
				glcanvas.display();
			}
		});
	}

	private void keyInit() {
		// Container contentPane = getContentPane();
		// JButton up = new JButton();
		// contentPane.add(up);
		// up.setBounds(40, 10, 30, 20);
		// up.addActionListener(e -> specialKeys(GLUT_KEY_UP, 0, 0));
		//
		// JButton down = new JButton();
		// contentPane.add(down);
		// down.setBounds(40, 60, 30, 20);
		// down.addActionListener(e -> specialKeys(GLUT_KEY_DOWN, 0, 0));
		//
		// JButton left = new JButton();
		// contentPane.add(left);
		// left.setBounds(10, 35, 30, 20);
		// left.addActionListener(e -> specialKeys(GLUT_KEY_LEFT, 0, 0));
		//
		// JButton right = new JButton();
		// contentPane.add(right);
		// right.setBounds(70, 35, 30, 20);
		// right.addActionListener(e -> specialKeys(GLUT_KEY_RIGHT, 0, 0));

		getToolkit().addAWTEventListener(e -> {
			if (e instanceof KeyEvent) {
				KeyEvent ke = (KeyEvent) e;
				switch (ke.getKeyCode()) {
				case KeyEvent.VK_UP:
					specialKeys(GLUT_KEY_UP, 0, 0);
					break;
				case KeyEvent.VK_LEFT:
					specialKeys(GLUT_KEY_LEFT, 0, 0);
					break;
				case KeyEvent.VK_RIGHT:
					specialKeys(GLUT_KEY_RIGHT, 0, 0);
					break;
				case KeyEvent.VK_DOWN:
					specialKeys(GLUT_KEY_DOWN, 0, 0);
					break;
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
	}

	protected GLCapabilities getGLCapabilities() {
		return new GLCapabilities(null);
	}

	/**
	 * Override this method to listen the special key event. And don't forget
	 * override <code>isOpenSpecialKey</code> to open this function
	 * 
	 * @param key
	 *            one of <code>GLUT_KEY_LEFT, GLUT_KEY_UP, GLUT_KEY_RIGHT,
	 *            GLUT_KEY_DOWN</code>
	 * @param x
	 * @param y
	 */
	protected void specialKeys(int key, int x, int y) {

	}

	/**
	 * Override this method to return true, if you want to use the direction
	 * key.
	 * 
	 * @return
	 */
	protected boolean isOpenSpecialKey() {
		return false;
	}

	/************************************************************************/
	@Override
	public void init(GLAutoDrawable drawable) {
		this.gl = drawable.getGL().getGL2();
		this.glu = GLU.createGLU(gl);
		this.glut = new GLUT();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		DefaultImpl.reshapeAtRadio(drawable, width, height);
	}

	/************************************************************************/

	protected void glutPostRedisplay() {
		glcanvas.display();
	}

	/**
	 * Entry point to C language function: <code> void {@native glutTimerFunc}
	 * (unsigned int msecs, void (*func)(int value), int value) </code>
	 * 
	 * @param millis
	 * @param func
	 * @param value
	 */
	protected void glutTimerFunc(int millis, Consumer<Integer> func, int value) {
		ThreadPool.execute(() -> {
			try {
				Thread.sleep(millis);
				func.accept(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private JMenuBar menuBar;
	private JMenu currentMenu;
	private Consumer<Integer> menuProcceser;
	private List<JMenu> menuList;

	protected int glutCreateMenu(Consumer<Integer> menuProcceser) {
		this.menuProcceser = menuProcceser;
		createMenuBarIfNot();
		currentMenu = new JMenu("Menu");
		menuBar.add(currentMenu);
		menuList.add(currentMenu);
		return menuList.size() - 1 + 10000;
	}

	protected void glutAddMenuEntry(String text, int index) {
		JMenuItem item = new JMenuItem(text);
		currentMenu.add(item);
		item.addActionListener(e -> menuProcceser.accept(index));
	}

	protected void glutAddSubMenu(String text, int subIndex) {
		JMenu subMenu = menuList.get(subIndex - 10000);
		if (subMenu == null || subMenu.equals(currentMenu))
			throw new Error();
		menuBar.remove(subMenu);
		subMenu.setText(text);
		currentMenu.add(subMenu);
	}

	/************************************************************************/
	private void centerWindow(Component frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();

		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;

		frame.setLocation((screenSize.width - frameSize.width) >> 1,
				(screenSize.height - frameSize.height) >> 1);
	}

	private void createMenuBarIfNot() {
		if (menuBar != null)
			return;
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		menuList = new ArrayList<>();
	}
}