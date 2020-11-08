package geckolib.fsm.transition;

import com.google.common.base.Preconditions;
import geckolib.fsm.event.IEventListener;
import geckolib.fsm.event.TransitionEvent;
import geckolib.fsm.state.State;

public class TransitionBuilder
{
	final Transition transition;

	public TransitionBuilder()
	{
		this.transition = new Transition();
	}

	public TransitionBuilder onBegin(IEventListener<TransitionEvent.Begin> eventListener)
	{
		this.transition.startEventRegistry.register(eventListener);
		return this;
	}

	public TransitionBuilder onEnd(IEventListener<TransitionEvent.End> eventListener)
	{
		this.transition.endEventRegistry.register(eventListener);
		return this;
	}

	public TransitionBuilder onRender(IEventListener<TransitionEvent.Render> eventListener)
	{
		this.transition.renderEventRegistry.register(eventListener);
		return this;
	}

	public CurveClipBuilder curveClip()
	{
		return new CurveClipBuilder(this);
	}

	public TransitionBuilder transitionAnim(String animationName)
	{
		this.transition.transitionAnim = animationName;
		return this;
	}

	public TransitionBuilder startState(State startState)
	{
		this.transition.startState = startState;
		return this;
	}

	public TransitionBuilder endState(State endState)
	{
		this.transition.endState = endState;
		return this;
	}

	public Transition build()
	{
		Preconditions.checkNotNull(transition.startState, "Start state cannot be null");
		Preconditions.checkNotNull(transition.endState, "End state cannot be null");
		Preconditions.checkState(!(transition.curveClip != null && transition.transitionAnim != null), "Cannot have both curve clip and transition animation");
		return this.transition;
	}
}
