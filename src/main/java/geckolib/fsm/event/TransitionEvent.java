package geckolib.fsm.event;

import geckolib.fsm.FSMInfo;
import geckolib.fsm.enums.Side;
import geckolib.fsm.transition.Transition;

public class TransitionEvent extends FSMEvent
{
	private final Transition transition;

	public TransitionEvent(Side side, Transition transition, FSMInfo FSMInfo)
	{
		super(side, FSMInfo);
		this.transition = transition;
	}

	public Transition getTransition()
	{
		return transition;
	}

	public static class Begin extends TransitionEvent
	{
		public Begin(Side side, Transition transition, FSMInfo FSMInfo)
		{
			super(side, transition, FSMInfo);
		}
	}

	public static class End extends TransitionEvent
	{
		public End(Side side, Transition transition, FSMInfo FSMInfo)
		{
			super(side, transition, FSMInfo);
		}
	}

	public static class Render extends TransitionEvent
	{
		public Render(Side side, Transition transition, FSMInfo FSMInfo)
		{
			super(side, transition, FSMInfo);
		}
	}
}
