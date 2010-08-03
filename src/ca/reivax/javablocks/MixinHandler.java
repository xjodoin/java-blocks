/**
 * 
 */
package ca.reivax.javablocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * @author Xavier
 * 
 */
public class MixinHandler implements MethodHandler
{
	private Map<Class, Object> mixins = new HashMap<Class, Object>();
	private Map<Class, InterceptorHandler> interceptors = new HashMap<Class, InterceptorHandler>();
	private Object instance;
	private final Set<Class> allInterfaces = new HashSet<Class>();

	private class RedirectHandler implements MethodHandler
	{
		private final Class<?> mixinInterface;

		/**
		 * 
		 */
		public RedirectHandler(Class<?> mixinInterface)
		{
			this.mixinInterface = mixinInterface;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
		 */
		@Override
		public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable
		{
			try
			{
				if (mixinInterface.isAssignableFrom(method.getDeclaringClass()))
				{
					return proceed.invoke(self, args);
				}
				// redirect to master
				return method.invoke(instance, args);
			}
			catch (InvocationTargetException e)
			{
				throw e.getTargetException();
			}
		}
	}

	public MixinHandler(Class<?>[] masterClassInterfaces, Class<?>[] interfaces)
	{

		Collections.addAll(allInterfaces, masterClassInterfaces);
		Collections.addAll(allInterfaces, interfaces);

		for (Class<?> mixinInteface : interfaces)
		{
			try
			{
				if (mixinInteface.isAnnotationPresent(Mixin.class))
				{
					final Class<?> interfaceImpl = mixinInteface.getAnnotation(Mixin.class).value();

					Set<Class> copy = new HashSet<Class>(allInterfaces);
					copy.remove(mixinInteface);

					ProxyFactory createProxyfactory = Transformers.createProxyfactory(interfaceImpl, copy.toArray(new Class[0]));
					createProxyfactory.setHandler(new RedirectHandler(mixinInteface));

					Object newInstance = createProxyfactory.createClass().newInstance();
					mixins.put(mixinInteface, newInstance);
					findInterceptors(newInstance, interfaceImpl);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable
	{
		try
		{
			Set<Entry<Class, InterceptorHandler>> entrySet = interceptors.entrySet();

			for (Entry<Class, InterceptorHandler> entry : entrySet)
			{
				if (entry.getKey().isAssignableFrom(method.getDeclaringClass()))
				{
					entry.getValue().call();
				}
			}

			for (Entry<Class, Object> entry : mixins.entrySet())
			{
				if (method.getDeclaringClass().isAssignableFrom(entry.getKey()))
				{
					return method.invoke(entry.getValue(), args);
				}
			}

			return proceed.invoke(self, args);
		}
		catch (InvocationTargetException e)
		{
			throw e.getTargetException();
		}

	}

	public void setInstance(Object instance)
	{
		this.instance = instance;
	}

	public Object getInstance()
	{
		return instance;
	}

	/**
	 * @param mixins2
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void addAllMixins(Map<Class, Class> mixins2) throws InstantiationException, IllegalAccessException
	{
		Set<Entry<Class, Class>> entrySet = mixins2.entrySet();

		for (Entry<Class, Class> entry : entrySet)
		{
			Class mixinInteface = entry.getKey();
			Set<Class> copy = new HashSet<Class>(allInterfaces);
			copy.remove(mixinInteface);

			ProxyFactory createProxyfactory = Transformers.createProxyfactory(entry.getValue(), copy.toArray(new Class[0]));
			createProxyfactory.setHandler(new RedirectHandler(mixinInteface));

			Object newInstance = createProxyfactory.createClass().newInstance();

			mixins.put(mixinInteface, newInstance);

			findInterceptors(newInstance, entry.getValue());
		}
	}

	/**
	 * @param value
	 */
	private void findInterceptors(Object interceptor, Class interceptorClass)
	{
		Method[] methods = interceptorClass.getMethods();
		for (Method method : methods)
		{
			if (method.isAnnotationPresent(Interceptor.class))
			{
				Interceptor annotation = method.getAnnotation(Interceptor.class);
				Class<?>[] classes = annotation.value();

				for (Class<?> class1 : classes)
				{
					interceptors.put(class1, new InterceptorHandler(interceptor, method));
				}
			}
		}
	}
}
