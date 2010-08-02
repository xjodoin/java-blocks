/**
 * 
 */
package ca.reivax.javablocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.util.proxy.ProxyFactory;

/**
 * @author Xavier
 * 
 */
public class Transformers
{
	private Transformers()
	{};

	public static <T> T newInstance(Class<T> mainClass, Class<?>... interfaces) throws InstantiationException, IllegalAccessException
	{
		StaticMethodHandler staticMethodHandler = newInstance(createProxyfactory(mainClass, interfaces));

		return (T) staticMethodHandler.getInstance();
	}

	/**
	 * @param <T>
	 * @param mainClass
	 * @param interfaces
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static StaticMethodHandler newInstance(ProxyFactory proxyFactory) throws InstantiationException, IllegalAccessException
	{
		StaticMethodHandler staticMethodHandler = new StaticMethodHandler(proxyFactory.getInterfaces());

		proxyFactory.setHandler(staticMethodHandler);

		Class newClass = proxyFactory.createClass();

		Object newInstance = newClass.newInstance();
		staticMethodHandler.setInstance(newInstance);

		return staticMethodHandler;
	}

	/**
	 * @param mainClass
	 * @param interfaces
	 * @return
	 */
	private static ProxyFactory createProxyfactory(final Class<?> mainClass, final Class<?>... interfaces)
	{
		ProxyFactory proxyFactory = new ProxyFactory()
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see javassist.util.proxy.ProxyFactory#getClassLoader0()
				 */
				@Override
				protected ClassLoader getClassLoader0()
				{
					List<Class<?>> classes = new ArrayList<Class<?>>();
					classes.add(mainClass);
					classes.addAll(Arrays.asList(interfaces));
					return new JBClassLoader(classes);
				}
			};

		proxyFactory.setSuperclass(mainClass);
		proxyFactory.setInterfaces(interfaces);
		return proxyFactory;
	}

	/**
	 * @param implementation
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T newInstance(Class<T> mainClass, Implementation<?>... implementations) throws InstantiationException, IllegalAccessException
	{
		Map<Class, Object> mixins = new HashMap<Class, Object>();

		for (int i = 0; i < implementations.length; i++)
		{
			Implementation<?> implementation = implementations[i];
			mixins.put(implementation.getInterface(), implementation.getImpl());
		}

		StaticMethodHandler staticMethodHandler = newInstance(createProxyfactory(mainClass, mixins.keySet().toArray(new Class[0])));

		staticMethodHandler.addAllMixins(mixins);

		return (T) staticMethodHandler.getInstance();

	}

}
