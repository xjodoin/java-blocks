package ca.reivax.javablocks.easyfake;


public interface FakeOverride
{
	void with(FakeMethod<?> body);

	void andReturn(Object returnValue);

	void startFaking();
}
