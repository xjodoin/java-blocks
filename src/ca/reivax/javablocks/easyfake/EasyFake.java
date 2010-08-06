package ca.reivax.javablocks.easyfake;

import ca.reivax.javablocks.Transformers;
import ca.reivax.javablocks.easyfake.internal.FakeOverrideHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * @author xjodoin
 */
public class EasyFake
{

	private static ThreadLocal threadLocal = new ThreadLocal();

	public static <T> T fake(Class<T> toSubClass)
	{
		try
		{
			ProxyFactory createProxyfactory = Transformers.createProxyfactory(toSubClass, FakeOverride.class);
			createProxyfactory.setHandler(new FakeOverrideHandler());
			T proxy = (T) createProxyfactory.createClass().newInstance();
			threadLocal.set(proxy);

			return proxy;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static FakeOverride override(Object returnValue)
	{
		return (FakeOverride) threadLocal.get();
	}
	
	public static FakeOverride voidOverride()
	{
		return (FakeOverride) threadLocal.get();
	}

	public static <T> void startFaking(T object)
	{
		if (object instanceof FakeOverride)
		{
			((FakeOverride) object).startFaking();
		}
	}
}
