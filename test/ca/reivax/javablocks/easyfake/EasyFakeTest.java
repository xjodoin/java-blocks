package ca.reivax.javablocks.easyfake;

import org.junit.Assert;
import org.junit.Test;

import ca.reivax.javablocks.easyfake.EasyFake;
import ca.reivax.javablocks.easyfake.FakeMethod;

public class EasyFakeTest
{
	@Test
	public void testOverrideAndReturn()
	{
		final EasyFakeTestBean bean = EasyFake.fake(EasyFakeTestBean.class);

		EasyFake.override(bean.getter()).andReturn("TEST");
		EasyFake.startFaking(bean);

		Assert.assertEquals("TEST", bean.getter());
	}

	@Test
	public void testOverrideAndReturn_NotOverriddenMethodWorksAsUsual()
	{
		final EasyFakeTestBean bean = EasyFake.fake(EasyFakeTestBean.class);

		EasyFake.override(bean.getter()).andReturn("TEST");
		EasyFake.startFaking(bean);

		Assert.assertEquals("actual", bean.returnsActual());
	}

	@Test
	public void testOverrideAndReturn_InternalMethodCallsOverride()
	{
		final EasyFakeTestBean bean = EasyFake.fake(EasyFakeTestBean.class);

		EasyFake.override(bean.getter()).andReturn("TEST");
		EasyFake.startFaking(bean);

		Assert.assertEquals("TEST", bean.callGetter());
	}

	@Test
	public void testOverrideWith()
	{
		final EasyFakeTestBean bean = EasyFake.fake(EasyFakeTestBean.class);

		EasyFake.override(bean.getter()).with(new FakeMethod<String>()
			{
				@Override
				public String fake(Object[] args)
				{
					return "TEST";
				}
			});
		EasyFake.startFaking(bean);

		Assert.assertEquals("TEST", bean.getter());
	}

	@Test
	public void testOverrideWith_VoidReturn()
	{
		final EasyFakeTestBean bean = EasyFake.fake(EasyFakeTestBean.class);

		try
		{
			bean.throwException();
		}
		catch (Exception e)
		{
			
		}
		
		EasyFake.voidOverride().with(new FakeMethod<Void>()
			{
				@Override
				public Void fake(Object[] args)
				{
					return null;
				}
			});
		EasyFake.startFaking(bean);
		
		try
		{
			bean.throwException();
		}
		catch (Exception e)
		{
			Assert.fail("Method body should be overridden to not throw exception");
		}
	}
}
