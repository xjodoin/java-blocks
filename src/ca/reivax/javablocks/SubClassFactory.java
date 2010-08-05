/**
 * 
 */
package ca.reivax.javablocks;

import javassist.util.proxy.ProxyFactory;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public class SubClassFactory
{

	private static ThreadLocal threadLocal = new ThreadLocal();

	/**
	 * @param class1
	 * @return
	 */
	public static <T> T extend(Class<T> toSubClass)
	{
		try
		{
			ProxyFactory createProxyfactory = Transformers.createProxyfactory(toSubClass, OverrideMethod.class);
			createProxyfactory.setHandler(new OverrideHandler(toSubClass));
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

	/**
	 * @param testOveride
	 * @return
	 */
	public static OverrideMethod override(Object returnValue)
	{
		return (OverrideMethod) threadLocal.get();
	}

}
