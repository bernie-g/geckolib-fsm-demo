package geckolib.fsm;

import geckolib.fsm.state.State;
import geckolib.fsm.transition.Transition;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import software.bernie.geckolib.core.AnimationState;

import java.util.Optional;

public class AnimationFSM
{
	Graph<State, Transition> stateGraph;
	boolean allowClientControl = false;
	DijkstraShortestPath<State, Transition> pathCalculator;

	private AnimationState playState;
	AnimationFSM()
	{
	}

	public AnimationState getPlayState()
	{
		return playState;
	}

	public Optional<GraphPath<State, Transition>> getPath(State sourceState, State endState)
	{
		if(!stateGraph.containsVertex(sourceState))
		{
			throw new RuntimeException("Source state \"" + sourceState.getAnimation() + "\" not found!");
		}

		if(!stateGraph.containsVertex(endState))
		{
			throw new RuntimeException("End state \"" + endState.getAnimation() + "\" not found!");
		}

		return Optional.ofNullable(pathCalculator.getPath(sourceState, endState));
	}

	public boolean canClientControl()
	{
		return allowClientControl;
	}
}
