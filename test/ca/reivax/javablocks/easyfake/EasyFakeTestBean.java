package ca.reivax.javablocks.easyfake;

public class EasyFakeTestBean
{
	public Object getter()
	{
		return null;
	}
	
	public void throwException() throws Exception
	{
		throw new RuntimeException();
	}
	
	public Object callGetter()
	{
		return getter();
	}
	
	public String returnsActual()
	{
		return "actual";
	}
}
