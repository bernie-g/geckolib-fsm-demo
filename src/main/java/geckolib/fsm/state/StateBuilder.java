package geckolib.fsm.state;

import com.google.common.base.Preconditions;
import geckolib.fsm.enums.LoopState;
import geckolib.fsm.event.IEventListener;
import geckolib.fsm.event.StateEvent;

public class StateBuilder
{
	private final State state;

	public StateBuilder()
	{
		this.state = new State();
	}

	public StateBuilder loop(LoopState loop)
	{
		this.state.loop = loop;
		return this;
	}

	public StateBuilder anim(String animationName)
	{
		this.state.animation = animationName;
		return this;
	}

	public StateBuilder length(double length)
	{
		this.state.length = length;
		return this;
	}

	public StateBuilder onStart(IEventListener<StateEvent.Begin> eventListener)
	{
		this.state.startEventRegistry.register(eventListener);
		return this;
	}

	public StateBuilder onEnd(IEventListener<StateEvent.End> eventListener)
	{
		this.state.endEventRegistry.register(eventListener);
		return this;
	}

	public StateBuilder onRender(IEventListener<StateEvent.Render> eventListener)
	{
		this.state.renderEventRegistry.register(eventListener);
		return this;
	}

	public State build()
	{
		Preconditions.checkNotNull(state.animation, "Animation name cannot be null");
		Preconditions.checkNotNull(state.loop, "Loop state cannot be null");
		Preconditions.checkState(state.length != 0, "Length of state cannot be 0");
		return this.state;
	}
}
