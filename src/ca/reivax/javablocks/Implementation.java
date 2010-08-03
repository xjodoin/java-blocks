/**
 * 
 */
package ca.reivax.javablocks;

/**
 * @author xjodoin
 */
public class Implementation<T>
{

	private final Class<T> interfaceClass;
	private final Class<? extends T> impl;

	/**
	 * @param class1
	 * @param class2
	 */
	public Implementation(Class<T> interfaceClass, Class<? extends T> impl)
	{
		this.interfaceClass = interfaceClass;
		this.impl = impl;
	}

	/**
	 * @return the impl
	 */
	public Class<? extends T> getImpl()
	{
		return impl;
	}

	/**
	 * @return
	 */
	public Class<T> getInterface()
	{
		return interfaceClass;
	}

}
