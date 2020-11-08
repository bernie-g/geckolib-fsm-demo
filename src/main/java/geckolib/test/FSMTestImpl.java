package geckolib.test;

import geckolib.fsm.AnimationFSM;
import geckolib.fsm.AnimationFSMBuilder;
import geckolib.fsm.enums.LoopState;
import geckolib.fsm.event.StateEvent;
import geckolib.fsm.state.State;
import geckolib.fsm.state.StateBuilder;
import geckolib.fsm.transition.Transition;
import geckolib.fsm.transition.TransitionBuilder;
import org.jgrapht.GraphPath;
import software.bernie.geckolib.core.easing.EasingType;

import java.util.Optional;

public class FSMTestImpl
{
	public static State walk = new StateBuilder().length(10).anim("walk").loop(LoopState.LOOP).onStart(FSMTestImpl::onBeginWalk).build();
	public static State sit = new StateBuilder().length(5).anim("sit").loop(LoopState.LOOP).build();
	public static State stand = new StateBuilder().length(10).anim("stand").loop(LoopState.HOLD_ON_LAST).onRender(FSMTestImpl::onRenderStanding).build();

	public static State run = new StateBuilder().length(10).anim("run").loop(LoopState.HOLD_ON_LAST).onEnd(FSMTestImpl::onFinishStanding).build();

	public static Transition standUp = new TransitionBuilder()
			.startState(sit)
			.endState(stand)
			.curveClip()
			.ease(EasingType.EaseInBack)
			.curveLength(5)
			.createCurve()
			.build();

	public static Transition fancyBeginWalking = new TransitionBuilder()
			.startState(stand)
			.endState(walk)
			.transitionAnim("fancy_begin_walk_animation")
			.build();

	public static Transition speedUp = new TransitionBuilder()
			.startState(walk)
			.endState(run)
			.transitionAnim("fancy_begin_walk_animation")
			.build();

	public static Transition jumpUp = new TransitionBuilder()
			.startState(sit)
			.endState(run)
			.transitionAnim("jumpup")
			.build();

	public static AnimationFSM fsm = new AnimationFSMBuilder()
			.transition(standUp)
			.transition(fancyBeginWalking)
			.transition(speedUp)
			.transition(jumpUp)
			.allowClientControl()
			.build();

	public static void main(String[] args)
	{
		Optional<GraphPath<State, Transition>> path = fsm.getPath(sit, run);
		System.out.print(path);
	}

	private static void onRenderStanding(StateEvent.Render render)
	{

	}

	private static void onFinishStanding(StateEvent.End event)
	{

	}

	private static void onBeginWalk(StateEvent.Begin event)
	{
	}
}
