package xdean.OpenGLSuperBible.share;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private static final ExecutorService POOL = Executors.newCachedThreadPool();
	
	public static void execute(Runnable r){
		POOL.execute(r);
	}
}
