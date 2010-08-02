/**
 * 
 */
package ca.reivax.javablocks;

import java.util.List;

/**
 * @author xjodoin
 * 
 */
public class JBClassLoader extends ClassLoader
{
	private final List<Class<?>> classes;

	/**
	 * 
	 */
	public JBClassLoader(List<Class<?>> classes)
	{
		this.classes = classes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.ClassLoader#loadClass(java.lang.String)
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException
	{
		Class found = null;

		for (Class<?> class1 : classes)
		{
			try
			{
				found = Class.forName(name, true, class1.getClassLoader());
				break;
			}
			catch (Exception e)
			{
				// do nothing you want to try on other classloader
			}
		}

		if (found == null)
		{
			throw new ClassNotFoundException(name);
		}
		else
		{
			return found;
		}
	}

}
