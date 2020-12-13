package geckolib.fsm.transition;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import geckolib.fsm.enums.AutoAdvanceMode;
import geckolib.fsm.enums.SwitchTime;
import geckolib.fsm.event.EventRegistry;
import geckolib.fsm.event.IEventListener;
import geckolib.fsm.event.TransitionEvent;
import geckolib.fsm.state.State;
import software.bernie.geckolib.core.IAnimatable;

import java.util.*;
import java.util.function.Predicate;


public class Transition
{
	final EventRegistry<TransitionEvent.Begin> startEventRegistry = new EventRegistry<>();
	final EventRegistry<TransitionEvent.End> endEventRegistry = new EventRegistry<>();
	final EventRegistry<TransitionEvent.Update> updateEventRegistry = new EventRegistry<>();

	private HashMultimap<State, Connection> connections = HashMultimap.create();
	private SwitchTime switchTime = SwitchTime.END;
	private OptionalDouble delay = OptionalDouble.empty();
	private AutoAdvanceMode autoAdvance = AutoAdvanceMode.OFF;
	private Optional<Predicate<IAnimatable>> autoAdvancePredicate = Optional.empty();
	private Optional<CurveClip> curveClip = Optional.empty();

	Transition()
	{
	}

	public SwitchTime getSwitchMode()
	{
		return switchTime;
	}

	public OptionalDouble getDelay()
	{
		return delay;
	}

	public AutoAdvanceMode getAutoAdvanceMode()
	{
		return autoAdvance;
	}

	public Optional<Predicate<IAnimatable>> getAutoAdvancePredicate()
	{
		return autoAdvancePredicate;
	}

	public Optional<CurveClip> getCurveClip()
	{
		return curveClip;
	}

	public void onStart(TransitionEvent.Begin event)
	{
		this.startEventRegistry.on(event);
	}

	public void onEnd(TransitionEvent.End event)
	{
		this.endEventRegistry.on(event);
	}

	public void onUpdate(TransitionEvent.Update event)
	{
		this.updateEventRegistry.on(event);
	}

	public Set<Map.Entry<State, Connection>> getConnections()
	{
		return connections.entries();
	}

	public Optional<Set<Connection>> getConnection(State startState)
	{
		return Optional.ofNullable(this.connections.get(startState));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		Transition that = (Transition) o;
		return switchTime == that.switchTime &&
				Objects.equal(delay, that.delay) &&
				autoAdvance == that.autoAdvance &&
				Objects.equal(autoAdvancePredicate, that.autoAdvancePredicate) &&
				Objects.equal(curveClip, that.curveClip);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(switchTime, delay, autoAdvance, autoAdvancePredicate, curveClip);
	}

	public static class Builder
	{
		final Transition transition;

		public Builder()
		{
			this.transition = new Transition();
		}

		public Builder onBegin(IEventListener<TransitionEvent.Begin> eventListener)
		{
			this.transition.startEventRegistry.register(eventListener);
			return this;
		}

		public Builder onEnd(IEventListener<TransitionEvent.End> eventListener)
		{
			this.transition.endEventRegistry.register(eventListener);
			return this;
		}

		/**
		 * Run every tick on servers and every frame on clients
		 */
		public Builder onUpdate(IEventListener<TransitionEvent.Update> eventListener)
		{
			this.transition.updateEventRegistry.register(eventListener);
			return this;
		}

		/**
		 * Set a specific easing curve for this transition. Use a {@link CurveClip.Builder} to create a CurveClip.
		 */
		public Builder curveClip(CurveClip curveClip)
		{
			this.transition.curveClip = Optional.of(curveClip);
			return this;
		}


		/**
		 * The beginning states that this transition can go from. This transition will only be used is the state machine is in this state.
		 * <p>
		 * Pass in {@link State#ANY} to make this transition work for any state.
		 * <p>
		 * Self-loops are not supported, and neither are duplicate connections.
		 */
		public ConnectionBuilder from(State... startStates)
		{
			Preconditions.checkState(startStates != null && startStates.length > 0, "Transition must contain at least one start state");
			return new ConnectionBuilder(this, startStates);
		}


		/**
		 * When this transition will take effect. Read about every SwitchTime on the wiki. If you use a delayed SwitchTime you must also call {@link Transition.Builder#delay}
		 */
		public Builder switchTime(SwitchTime mode)
		{
			this.transition.switchTime = mode;
			return this;
		}

		/**
		 * Determines if this transition should automatically start after a certain state is reached. This is useful when used in conjunction with {@link SwitchTime#END} or {@link SwitchTime#END_DELAYED}
		 * <p>
		 * Optionally you can use {@link AutoAdvanceMode#PREDICATE} to only automatically transition when a certain predicate is true. Make sure to call {@link Transition.Builder#autoAdvancePredicate} if you wish to use this.
		 */
		public Builder autoAdvance(AutoAdvanceMode mode)
		{
			this.transition.autoAdvance = mode;
			return this;
		}


		/**
		 * Sets the predicate that will be checked every tick/frame to determine if the transition should happen.
		 */
		public Builder autoAdvancePredicate(Predicate<IAnimatable> predicate)
		{
			this.transition.autoAdvancePredicate = Optional.of(predicate);
			this.transition.autoAdvance = AutoAdvanceMode.PREDICATE;
			return this;
		}

		/**
		 * Sets the delay before running this transition. Use in conjunction with {@link SwitchTime#END_DELAYED} or {@link SwitchTime#BEGIN_DELAYED}
		 *
		 * @param delay The delay in seconds
		 */
		public Builder delay(double delay)
		{
			this.transition.delay = OptionalDouble.of(delay);
			return this;
		}

		public Transition build()
		{
			if (this.transition.autoAdvance == AutoAdvanceMode.PREDICATE)
			{
				Preconditions.checkState(transition.autoAdvancePredicate.isPresent(), "Please specify an auto advance predicate");
			}

			if (this.transition.switchTime == SwitchTime.BEGIN_DELAYED || this.transition.switchTime == SwitchTime.END_DELAYED)
			{
				Preconditions.checkState(transition.delay.isPresent(), "No delay specified");
			}

			if (this.transition.delay.isPresent())
			{
				Preconditions.checkState(transition.switchTime == SwitchTime.BEGIN_DELAYED || transition.switchTime == SwitchTime.END_DELAYED, "Delay specified but SwitchTime isn't delayed");
			}
			return this.transition;
		}
	}

	public static class ConnectionBuilder
	{
		private final Builder builder;
		private final State[] startStates;

		public ConnectionBuilder(Builder builder, State[] startStates)
		{
			this.builder = builder;
			this.startStates = startStates;
		}

		/**
		 * The end state that this connection will go to.
		 * <p>
		 * Self-loops are not supported, and neither are duplicate connections.
		 */
		public Builder to(State... endStates)
		{
			Preconditions.checkState(endStates != null && endStates.length > 0, "Transition must contain at least one end state");
			Arrays.stream(endStates).forEach(x -> Preconditions.checkState(!x.equals(State.ANY), "State.ANY cannot be used as an end state."));
			Arrays.stream(startStates).forEach(start ->
					Arrays.stream(endStates)
							.filter(x -> !x.equals(start))
							.forEach(end ->
							{
								Connection connection;
								if (start.equals(State.ANY))
								{
									connection = new Connection(null, end, this.builder.transition, true);
								}
								else
								{
									connection = new Connection(start, end, this.builder.transition);
								}
								this.builder.transition.connections.put(start, connection);
							}));
			return this.builder;
		}
	}
}
