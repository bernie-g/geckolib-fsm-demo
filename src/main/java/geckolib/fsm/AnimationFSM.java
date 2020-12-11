package geckolib.fsm;

import com.google.common.base.Preconditions;
import geckolib.fsm.state.State;
import geckolib.fsm.transition.Transition;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import software.bernie.geckolib.core.AnimationState;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AnimationFSM
{
	private Graph<State, Transition> stateGraph;
	private boolean clientOnly = false;
	private AllDirectedPaths<State, Transition> pathCalculator;

	private AnimationState playState;
	private AnimationFSM()
	{
	}

	public AnimationState getPlayState()
	{
		return playState;
	}

	public Optional getPath(State sourceState, State endState)
	{
		if(!stateGraph.containsVertex(sourceState))
		{
			throw new RuntimeException("Source state \"" + sourceState.getAnimation() + "\" not found!");
		}

		if(!stateGraph.containsVertex(endState))
		{
			throw new RuntimeException("End state \"" + endState.getAnimation() + "\" not found!");
		}

		List<GraphPath<State, Transition>> allPaths = pathCalculator.getAllPaths(sourceState, endState, true, null);
		allPaths.sort(Comparator.comparingInt((x) -> x.getLength()));
		return null;
	}

	public boolean isClientOnly()
	{
		return clientOnly;
	}

	public static class Builder
	{
		private final AnimationFSM fsm;
		private final Graph<State, Transition> graph;

		public Builder()
		{
			this.fsm = new AnimationFSM();
			this.graph = new DefaultDirectedGraph<>(Transition.class);
		}

		/**
		 * Registers a transition between two states in the state machine
		 *
		 * @param weight     The higher this number is the lower priority the path will be.
		 */
		public Builder transition(Transition transition, double weight)
		{
			graph.addVertex(transition.getStartState());
			graph.addVertex(transition.getEndState());
			graph.addEdge(transition.getStartState(), transition.getEndState(), transition);
			graph.setEdgeWeight(transition, weight);
			return this;
		}

		/**
		 * Registers a transition between two states in the state machine
		 */
		public Builder transition(Transition transition)
		{
			return transition(transition, 1);
		}

		/**
		 * Calling this method will make the finite state machine client side only, and attempting to use it server-side will not work
		 */
		public Builder clientOnly()
		{
			this.fsm.clientOnly = true;
			return this;
		}

		public AnimationFSM build()
		{
			fsm.stateGraph = graph;
			fsm.pathCalculator = new AllDirectedPaths<>(graph);
			Preconditions.checkState(graph.edgeSet().size() != 0, "Cannot have 0 transitions in a FSM");
			return this.fsm;
		}
	}
}
