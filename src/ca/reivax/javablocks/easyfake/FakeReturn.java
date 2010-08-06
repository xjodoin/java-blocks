package ca.reivax.javablocks.easyfake;

public class FakeReturn<T> implements FakeMethod<T>
{
	private final T returnValue;

	public FakeReturn(T returnValue)
	{
		this.returnValue = returnValue;
	}
	
	@Override
	public T fake(Object[] args)
	{
		return returnValue;
	}
}
