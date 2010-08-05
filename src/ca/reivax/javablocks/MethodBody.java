/**
 * 
 */
package ca.reivax.javablocks;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public interface MethodBody<T>
{
	public T invoke(Object[] args);
}
