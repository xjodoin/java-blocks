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
public class ExplosionServicesBean implements ExplosionServices
{

	/**
	 * @return
	 */
	@Override
	public Object testOveride()
	{
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Object noOverride()
	{
		return "no override";
	}

	/**
	 * @return
	 */
	@Override
	public Object redirectToOveride()
	{
		return testOveride();
	}

}
