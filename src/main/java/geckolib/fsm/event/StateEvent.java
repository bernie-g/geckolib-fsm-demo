package geckolib.fsm.event;

import geckolib.fsm.FSMInfo;
import geckolib.fsm.enums.Side;
import geckolib.fsm.state.State;

public class StateEvent extends FSMEvent
{
	private final State state;

	public StateEvent(Side side, State state, FSMInfo FSMInfo)
	{
		super(side, FSMInfo);
		this.state = state;
	}

	public State getState()
	{
		return state;
	}

	public static class Begin extends StateEvent
	{
		public Begin(Side side, State state, FSMInfo FSMInfo)
		{
			super(side, state, FSMInfo);
		}
	}

	public static class End extends StateEvent
	{
		public End(Side side, State state, FSMInfo FSMInfo)
		{
			super(side, state, FSMInfo);
		}
	}

	public static class Update extends StateEvent
	{
		public Update(Side side, State state, FSMInfo FSMInfo)
		{
			super(side, state, FSMInfo);
		}
	}
}
