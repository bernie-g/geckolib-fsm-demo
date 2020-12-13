package geckolib.fsm.state;

import com.google.common.base.Preconditions;
import geckolib.fsm.enums.LoopState;
import geckolib.fsm.event.EventRegistry;
import geckolib.fsm.event.IEventListener;
import geckolib.fsm.event.StateEvent;

import java.util.Objects;

public class State
{
	public static final State ANY = new State.Builder().anim("").loop(LoopState.PLAY_ONCE).length(1).build();

	final EventRegistry<StateEvent.Begin> startEventRegistry = new EventRegistry<>();
	final EventRegistry<StateEvent.End> endEventRegistry = new EventRegistry<>();
	final EventRegistry<StateEvent.Update> updateEventRegistry = new EventRegistry<>();
	String animation;
	LoopState loop = LoopState.LOOP;
	double length;

	private State()
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

	public void onUpdate(StateEvent.Update event)
	{
		this.updateEventRegistry.on(event);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(animation, loop, length);
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

		State state = (State) obj;

		return Objects.equals(length, state.length) && Objects.equals(animation, state.animation) && Objects.equals(loop, this.loop);
	}

	@Override
	public String toString()
	{
		if (this == State.ANY)
		{
			return "ANY";
		}
		return animation + "[" + length + ", " + this.loop + "]";
	}

	public static class Builder
	{
		private final State state;

		public Builder()
		{
			this.state = new State();
		}

		/**
		 * Sets the desired loop mode of this state. {@link LoopState#HOLD_ON_LAST} will cause the state to hold on the very last keyframe instead of reset to the beginning.
		 */
		public Builder loop(LoopState loop)
		{
			this.state.loop = loop;
			return this;
		}

		/**
		 * Sets the actual animation name for this state. This must match an animation name in the json file.
		 */
		public Builder anim(String animationName)
		{
			this.state.animation = animationName;
			return this;
		}

		/**
		 * The length of this state, this should almost always be the same as the animation length.
		 * <p>
		 * The reason you have to provide this is because the server can't read the animation json file.
		 *
		 * @param length the length
		 * @return the builder
		 */
		public Builder length(double length)
		{
			this.state.length = length;
			return this;
		}

		public Builder onStart(IEventListener<StateEvent.Begin> eventListener)
		{
			this.state.startEventRegistry.register(eventListener);
			return this;
		}

		public Builder onEnd(IEventListener<StateEvent.End> eventListener)
		{
			this.state.endEventRegistry.register(eventListener);
			return this;
		}

		/**
		 * Run every tick on servers and every frame on clients
		 */
		public Builder onUpdate(IEventListener<StateEvent.Update> eventListener)
		{
			this.state.updateEventRegistry.register(eventListener);
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
}

