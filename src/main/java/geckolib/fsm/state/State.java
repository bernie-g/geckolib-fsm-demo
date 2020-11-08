package geckolib.fsm.state;

import geckolib.fsm.enums.LoopState;
import geckolib.fsm.event.EventRegistry;
import geckolib.fsm.event.StateEvent;

public class State
{
	final EventRegistry<StateEvent.Begin> startEventRegistry = new EventRegistry<>();
	final EventRegistry<StateEvent.End> endEventRegistry = new EventRegistry<>();
	final EventRegistry<StateEvent.Render> renderEventRegistry = new EventRegistry<>();
	String animation;
	LoopState loop;

	double length;

	State()
	{
	}

	public double getLength()
	{
		return length;
	}

	public String getAnimation()
	{
		return animation;
	}

	public LoopState getLoopType()
	{
		return loop;
	}

	public void onStart(StateEvent.Begin event)
	{
		this.startEventRegistry.on(event);
	}

	public void onEnd(StateEvent.End event)
	{
		this.endEventRegistry.on(event);
	}

	public void onRender(StateEvent.Render event)
	{
		this.renderEventRegistry.on(event);
	}
}

