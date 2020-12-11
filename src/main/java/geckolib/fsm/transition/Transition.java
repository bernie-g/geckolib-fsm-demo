package geckolib.fsm.transition;

import com.google.common.base.Preconditions;
import geckolib.fsm.enums.AutoAdvanceMode;
import geckolib.fsm.enums.SwitchTime;
import geckolib.fsm.event.EventRegistry;
import geckolib.fsm.event.IEventListener;
import geckolib.fsm.event.TransitionEvent;
import geckolib.fsm.state.State;
import software.bernie.geckolib.core.IAnimatable;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Predicate;

public class Transition
{
	final EventRegistry<TransitionEvent.Begin> startEventRegistry = new EventRegistry<>();
	final EventRegistry<TransitionEvent.End> endEventRegistry = new EventRegistry<>();
	final EventRegistry<TransitionEvent.Update> updateEventRegistry = new EventRegistry<>();

	private State startState;
	private State endState;
	private SwitchTime switchTime;
	private OptionalDouble delay = OptionalDouble.empty();
	private AutoAdvanceMode autoAdvance;
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

	public State getStartState()
	{
		return startState;
	}

	public State getEndState()
	{
		return endState;
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
		 * The beginning state that this transition can go from. This transition will only be used is the state machine is in this state.
		 *
		 * Pass in {@link State#ANY} to make this transition work for any state.
		 */
		public Builder from(State startState)
		{
			this.transition.startState = startState;
			return this;
		}

		/**
		 * The end state that this transition will go to.
		 */
		public Builder to(State endState)
		{
			this.transition.endState = endState;
			return this;
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
		 *
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
			Preconditions.checkNotNull(transition.startState, "Start state cannot be null");
			Preconditions.checkNotNull(transition.endState, "End state cannot be null");

			if(this.transition.autoAdvance == AutoAdvanceMode.PREDICATE)
			{
				Preconditions.checkState(transition.autoAdvancePredicate.isPresent());
			}

			if(this.transition.switchTime == SwitchTime.BEGIN_DELAYED || this.transition.switchTime == SwitchTime.END_DELAYED)
			{
				Preconditions.checkState(transition.delay.isPresent());
			}
			return this.transition;
		}
	}
}
