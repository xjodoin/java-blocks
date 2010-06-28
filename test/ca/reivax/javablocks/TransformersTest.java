package ca.reivax.javablocks;

import static org.junit.Assert.*;

import org.junit.Test;

public class TransformersTest 
{
	@Test
	public void testNewInstance()
	{
		ca.reivax.javablocks.Test newInstance = Transformers.newInstance(ca.reivax.javablocks.Test.class, TestInterface.class);
		
		newInstance.standard("standard class test");
		
		if(newInstance instanceof TestInterface)
		{
			((TestInterface) newInstance).print("sysout text");
		}
		else
		{
			fail();
		}
		
	}
	
	@Test
	public void testNewInstanceWithExtendCurrent()
	{
		ca.reivax.javablocks.Test newInstance = Transformers.newInstance(ca.reivax.javablocks.Test.class, TestInterfaceExtends.class);
		
		newInstance.standard("standard class test");
		
		if(newInstance instanceof TestInterfaceExtends)
		{
			((TestInterfaceExtends) newInstance).print("sysout text");
		}
		else
		{
			fail();
		}
		
	}
}
