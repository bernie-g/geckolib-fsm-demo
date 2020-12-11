package geckolib.test;

import geckolib.fsm.AnimationFSM;
import geckolib.fsm.enums.AutoAdvanceMode;
import geckolib.fsm.enums.LoopState;
import geckolib.fsm.enums.SwitchTime;
import geckolib.fsm.event.StateEvent;
import geckolib.fsm.state.State;
import geckolib.fsm.transition.CurveClip;
import geckolib.fsm.transition.Transition;
import org.jgrapht.GraphPath;
import software.bernie.geckolib.core.IAnimatable;
import software.bernie.geckolib.core.easing.EasingType;

import java.util.Optional;

public class FSMTestImpl
{
	public static State walk = new State.Builder().length(10).anim("walk").loop(LoopState.LOOP).onStart(FSMTestImpl::onBeginWalk).build();
	public static State sit = new State.Builder().length(5).anim("sit").loop(LoopState.LOOP).build();
	public static State stand = new State.Builder().length(10).anim("stand").loop(LoopState.HOLD_ON_LAST).onUpdate(FSMTestImpl::onRenderStanding).build();
	public static State run = new State.Builder().length(10).anim("run").loop(LoopState.HOLD_ON_LAST).onEnd(FSMTestImpl::onFinishStanding).build();

	public static Transition standUp = new Transition.Builder()
			.from(sit)
			.to(stand)
			.curveClip(new CurveClip.Builder().ease(EasingType.EaseInBack).curveLength(5).createCurve())
			.build();

	public static Transition fancyBeginWalking = new Transition.Builder()
			.from(stand)
			.to(walk)
			.switchTime(SwitchTime.END_DELAYED)
			.delay(5)
			.build();

	public static Transition speedUp = new Transition.Builder()
			.from(walk)
			.switchTime(SwitchTime.IMMEDIATE)
			.autoAdvance(AutoAdvanceMode.PREDICATE)
			.autoAdvancePredicate(FSMTestImpl::shouldSpeedUp)
			.to(run)
			.build();

	private static boolean shouldSpeedUp(IAnimatable animatable)
	{
		return true;
	}

	public static Transition jumpUp = new Transition.Builder()
			.from(sit)
			.to(run)
			.build();

	public static AnimationFSM fsm = new AnimationFSM.Builder()
			.transition(standUp, 1)
			.transition(fancyBeginWalking, 1)
			.transition(speedUp, 1)
			.transition(jumpUp, 4)
			.clientOnly()
			.build();

	public static void main(String[] args)
	{
		Optional<GraphPath<State, Transition>> path = fsm.getPath(sit, run);
		System.out.print(path);
	}

	private static void onRenderStanding(StateEvent.Update update)
	{

	}

	private static void onFinishStanding(StateEvent.End event)
	{

	}

	private static void onBeginWalk(StateEvent.Begin event)
	{
	}
}
