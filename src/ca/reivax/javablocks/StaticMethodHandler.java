/**
 * 
 */
package ca.reivax.javablocks;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

/**
 * @author Xavier
 * 
 */
public class StaticMethodHandler implements MethodHandler 
{
	private Map<Class, Object> mixins = new HashMap<Class, Object>();
	private Object instance;

	
	public StaticMethodHandler(Class<?>[] interfaces) {
		
		for (Class<?> mixinInteface : interfaces) {
			try 
			{
				if(mixinInteface.isAnnotationPresent(Mixin.class))
				{
					final Class<?> interfaceImpl = mixinInteface.getAnnotation(Mixin.class).value();
					
					ProxyFactory proxyFactory = new ProxyFactory();
					
					proxyFactory.setSuperclass(interfaceImpl);
					proxyFactory.setFilter(new MethodFilter() {
						
						@Override
						public boolean isHandled(Method method) 
						{
							//only handle method aren't in the interface
							return !method.getDeclaringClass().equals(interfaceImpl);
						}
					});
					
					proxyFactory.setHandler(new MethodHandler() {
						
						@Override
						public Object invoke(Object self, Method method, Method proceed,
								Object[] args)
								throws Throwable {
							
							//redirect to the master object
							return method.invoke(instance, args);
						}
					});
					
					mixins.put(mixinInteface, proxyFactory.createClass().newInstance());
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
	 * @see javassist.util.proxy.MethodHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object self, Method method, Method proceed,
			Object[] args) throws Throwable {

		if(mixins.containsKey(method.getDeclaringClass()))
		{
			return method.invoke(mixins.get(method.getDeclaringClass()), args);
		}
		else
		{
			return proceed.invoke(self, args);
		}
		
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Object getInstance() {
		return instance;
	}
}
