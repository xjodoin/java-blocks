/**
 * 
 */
package ca.reivax.javablocks;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public class SubClassFactoryTest
{

	@Test
	public void testOverrideSingleMethod()
	{
		ExplosionServicesBean bean = SubClassFactory.extend(ExplosionServicesBean.class);
		SubClassFactory.override(bean.testOveride()).with(new MethodBody<String>()
			{
				@Override
				public String invoke(Object[] args)
				{
					return "override with success";
				}
			});

		Assert.assertEquals("override with success", bean.testOveride());
	}

	@Test
	public void testNOtOverrideMethod()
	{
		ExplosionServicesBean bean = SubClassFactory.extend(ExplosionServicesBean.class);
		SubClassFactory.override(bean.testOveride()).with(new MethodBody<String>()
			{
				@Override
				public String invoke(Object[] args)
				{
					return "override with success";
				}
			});

		Assert.assertEquals("no override", bean.noOverride());
	}

	@Test
	public void testNoOverrideCallOverrideMethod()
	{
		ExplosionServicesBean bean = SubClassFactory.extend(ExplosionServicesBean.class);
		SubClassFactory.override(bean.testOveride()).with(new MethodBody<String>()
			{
				@Override
				public String invoke(Object[] args)
				{
					return "override with success";
				}
			});

		Assert.assertEquals("override with success", bean.redirectToOveride());
	}

}
