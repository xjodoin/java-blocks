/**
 * 
 */
package ca.reivax.javablocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javassist.util.proxy.MethodHandler;

/**
 * @author Xavier
 * 
 */
public class StaticMethodHandler implements MethodHandler
{
	private Map<Class, Object> mixins = new HashMap<Class, Object>();
	private Map<Class, InterceptorHandler> interceptors = new HashMap<Class, InterceptorHandler>();
	private Object instance;

	public StaticMethodHandler(Class<?>[] interfaces)
	{

		for (Class<?> mixinInteface : interfaces)
		{
			try
			{
				if (mixinInteface.isAnnotationPresent(Mixin.class))
				{
					final Class<?> interfaceImpl = mixinInteface.getAnnotation(Mixin.class).value();

					Object newInstance = interfaceImpl.newInstance();
					mixins.put(mixinInteface, newInstance);
					findInterceptors(newInstance);
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
			Set<Entry<Class,InterceptorHandler>> entrySet = interceptors.entrySet();
			
			for (Entry<Class, InterceptorHandler> entry : entrySet)
			{
				if(entry.getKey().isAssignableFrom(method.getDeclaringClass()))
				{
					entry.getValue().call();
				}
			}
			
			if (mixins.containsKey(method.getDeclaringClass()))
			{
				return method.invoke(mixins.get(method.getDeclaringClass()), args);
			}
			else
			{
				return proceed.invoke(self, args);
			}
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
	 */
	public void addAllMixins(Map<Class, Object> mixins2)
	{
		Set<Entry<Class,Object>> entrySet = mixins2.entrySet();
		
		for (Entry<Class, Object> entry : entrySet)
		{
			Object value = entry.getValue();
			mixins.put(entry.getKey(), value);
			
			findInterceptors(value);
		}
	}

	/**
	 * @param value
	 */
	private void findInterceptors(Object value)
	{
		Method[] methods = value.getClass().getMethods();
		for (Method method : methods)
		{
			if(method.isAnnotationPresent(Interceptor.class))
			{
				Interceptor annotation = method.getAnnotation(Interceptor.class);
				Class<?>[] classes = annotation.value();
				
				for (Class<?> class1 : classes)
				{
					interceptors.put(class1, new InterceptorHandler(value,method));
				}
			}
		}
	}
}
