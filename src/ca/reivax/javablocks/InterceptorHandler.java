/**
 * 
 */
package ca.reivax.javablocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xjodoin
 * 
 */
public class InterceptorHandler
{
	private Object interceptorInstance;
	private Method toCall;
	
	/**
	 * 
	 */
	public InterceptorHandler(Object interceptorInstance,Method toCall)
	{
		this.interceptorInstance = interceptorInstance;
		this.toCall = toCall;
	}
	
	public void call() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		toCall.invoke(interceptorInstance);
	}
}
