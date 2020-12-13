package geckolib.fsm.transition;

import com.google.common.base.Preconditions;
import geckolib.fsm.state.State;

import java.util.Objects;

public class Connection
{
	public final State startState;
	public final State endState;
	public final Transition transition;
	public boolean isANY = false;

	public Connection(State startState, State endState, Transition transition)
	{
		if (startState != null)
		{
			Preconditions.checkState(!startState.equals(State.ANY), "Start state cannot be ANY in a connection, use the other constructor");
		}
		this.startState = startState;
		this.endState = endState;
		this.transition = transition;
	}

	public Connection(State startState, State endState, Transition transition, boolean isSourceANY)
	{
		this(startState, endState, transition);
		this.isANY = isSourceANY;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(startState, endState);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}

		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}

		Connection connection = (Connection) obj;

		return (isANY || Objects.equals(startState, connection.startState)) && Objects.equals(endState, connection.endState) && Objects.equals(transition, connection.transition);
	}

	@Override
	public String toString()
	{
		return (isANY ? "ANY" : startState.toString()) + " -> " + endState.toString();
	}
}
