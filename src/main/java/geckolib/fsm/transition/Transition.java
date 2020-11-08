package geckolib.fsm.transition;

import geckolib.fsm.event.EventRegistry;
import geckolib.fsm.event.TransitionEvent;
import geckolib.fsm.state.State;

import javax.annotation.Nullable;

public class Transition
{
	final EventRegistry<TransitionEvent.Begin> startEventRegistry = new EventRegistry<>();
	final EventRegistry<TransitionEvent.End> endEventRegistry = new EventRegistry<>();
	final EventRegistry<TransitionEvent.Render> renderEventRegistry = new EventRegistry<>();

	State startState;
	State endState;

	public State getStartState()
	{
		return startState;
	}

	public State getEndState()
	{
		return endState;
	}

	@Nullable
	public CurveClip getCurveClip()
	{
		return curveClip;
	}

	@Nullable
	public String getTransitionAnim()
	{
		return transitionAnim;
	}

	@Nullable
	CurveClip curveClip;
	@Nullable
	String transitionAnim;

	Transition()
	{
	}

	public void onStart(TransitionEvent.Begin event)
	{
		this.startEventRegistry.on(event);
	}

	public void onEnd(TransitionEvent.End event)
	{
		this.endEventRegistry.on(event);
	}

	public void onRender(TransitionEvent.Render event)
	{
		this.renderEventRegistry.on(event);
	}
}
