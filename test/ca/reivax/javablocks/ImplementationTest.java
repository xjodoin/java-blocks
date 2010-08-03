/**
 * 
 */
package ca.reivax.javablocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author xjodoin
 * @author NetAppsID inc.
 * 
 * @version
 * 
 */
public class ImplementationTest
{
	@Test
	public void testImplementationDelegate() throws InstantiationException, IllegalAccessException
	{
		Implementation<Toto> implementation = new Implementation<Toto>(Toto.class, TotoImpl.class);
		Object newInstance = Transformers.newInstance(ArrayList.class, implementation);

		((List) newInstance).add(new Object());

		assertTrue(((List) newInstance).size() == 1);

		assertEquals("testToto", ((Toto) newInstance).testToto());

	}

	@Test
	public void testImplementationInterceptor() throws InstantiationException, IllegalAccessException
	{
		Implementation<Toto> implementation = new Implementation<Toto>(Toto.class, TotoImpl.class);
		Object newInstance = Transformers.newInstance(ArrayList.class, implementation);

		((List) newInstance).add(new Object());

		assertTrue(((Toto) newInstance).isIntercept());

	}

	@Test
	public void testRedirectToMasterWithInterface() throws InstantiationException, IllegalAccessException
	{
		Implementation<Toto> implementation = new Implementation<Toto>(Toto.class, TotoImpl.class);
		Object newInstance = Transformers.newInstance(ArrayList.class, implementation);

		Toto toto = (Toto) newInstance;
		toto.mustAddToList("Test Add");
		
		assertEquals("Test Add", ((List)newInstance).get(0));
	}
}
