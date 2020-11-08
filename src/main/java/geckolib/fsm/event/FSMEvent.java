package geckolib.fsm.event;

import geckolib.fsm.FSMInfo;
import geckolib.fsm.enums.Side;

public class FSMEvent
{
	private final Side side;

	private final FSMInfo FSMInfo;

	public FSMEvent(Side side, FSMInfo FSMInfo)
	{
		this.side = side;
		this.FSMInfo = FSMInfo;
	}

	public FSMInfo getPathInfo()
	{
		return FSMInfo;
	}

	public Side getSide()
	{
		return side;
	}
}
