package xdean.OpenGLSuperBible.share.base;

public abstract class Wrapper<T> {
	private T value;

	public T get() {
		return value;
	}

	public void set(T t) {
		value = t;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}

	public static class IntWrapper extends Wrapper<Integer>{}
	public static class ByteWrapper extends Wrapper<Byte>{}
	public static class FloatWrapper extends Wrapper<Float>{}
	public static class DoubleWrapper extends Wrapper<Double>{}
	public static class BooleanWrapper extends Wrapper<Boolean>{}
	public static class ShortWrapper extends Wrapper<Short>{}
	public static class LongWrapper extends Wrapper<Long>{}
	public static class CharWrapper extends Wrapper<Character>{}
}
