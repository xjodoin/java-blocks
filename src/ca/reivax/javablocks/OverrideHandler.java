/**
 * 
 */
package ca.reivax.javablocks;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public class OverrideHandler implements MethodHandler, OverrideMethod
{
	private Map<Method, MethodBody<?>> methodBodies = new HashMap<Method, MethodBody<?>>();
	private Method lastMethod;

	private Class masterClass;

	/**
	 * 
	 */
	public OverrideHandler(Class masterClass)
	{
		this.masterClass = masterClass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable
	{
		if (method.getDeclaringClass().equals(OverrideMethod.class))
		{
			return method.invoke(this, args);
		}
		else
		{
			if (methodBodies.containsKey(method))
			{
				return methodBodies.get(method).invoke(args);
			}
			else
			{
				lastMethod = method;
				return proceed.invoke(self, args);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.reivax.javablocks.OverrideMethod#with(ca.reivax.javablocks.MethodBody)
	 */
	@Override
	public void with(MethodBody body)
	{
		methodBodies.put(lastMethod, body);
	}

}
