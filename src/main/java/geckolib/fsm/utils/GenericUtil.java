package geckolib.fsm.utils;

import java.util.function.Consumer;

public class GenericUtil
{
	public static <T extends Object> void consumeIf(T t, Consumer<T> consumer, boolean shouldConsume)
	{
		if(shouldConsume)
		{
			consumer.accept(t);
		}
	}
}
