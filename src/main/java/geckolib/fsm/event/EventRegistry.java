package geckolib.fsm.event;

import java.util.ArrayList;
import java.util.List;

public class EventRegistry<T extends FSMEvent>
{
	private final List<IEventListener<T>> listeners = new ArrayList();

	public void register(IEventListener<T> eventListener)
	{
		this.listeners.add(eventListener);
	}

	public void on(T event)
	{
		for(IEventListener eventListener : listeners)
		{
			eventListener.on(event);
		}
	}
}
