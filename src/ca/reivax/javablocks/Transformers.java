/**
 * 
 */
package ca.reivax.javablocks;

import javassist.util.proxy.ProxyFactory;

/**
 * @author Xavier
 *
 */
public class Transformers 
{
	private Transformers(){};
	
	public static <T> T newInstance(Class<T> mainClass,Class<?> ... interfaces)
	{
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setSuperclass(mainClass);
		proxyFactory.setInterfaces(interfaces);
		
		StaticMethodHandler staticMethodHandler = new StaticMethodHandler(interfaces);
		
		proxyFactory.setHandler(staticMethodHandler);
		
		Class newClass = proxyFactory.createClass();
		
		try 
		{
			Object newInstance = newClass.newInstance();
			staticMethodHandler.setInstance(newInstance);
			
			return (T) newInstance; 
		} 
		catch (InstantiationException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
