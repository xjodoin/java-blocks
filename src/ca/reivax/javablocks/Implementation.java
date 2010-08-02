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
	private final T impl;

	/**
	 * @param class1
	 * @param class2
	 */
	public Implementation(Class<T> interfaceClass, T impl)
	{
		this.interfaceClass = interfaceClass;
		this.impl = impl;
	}

	/**
	 * @return the impl
	 */
	public T getImpl()
	{
		return impl;
	}

	/**
	 * @return
	 */
	public Class getInterface()
	{
		return interfaceClass;
	}

}
