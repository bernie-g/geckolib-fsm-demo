package geckolib.fsm;

import com.google.common.base.Preconditions;
import geckolib.fsm.state.State;
import geckolib.fsm.transition.Connection;
import geckolib.fsm.transition.Transition;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraManyToManyShortestPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import software.bernie.geckolib.core.AnimationState;

import javax.annotation.Nonnull;
import java.util.*;

import static geckolib.fsm.utils.GenericUtil.consumeIf;

public class AnimationFSM
{
	private Graph<State, Connection> stateGraph;
	private boolean clientOnly = false;
	private DijkstraShortestPath<State, Connection> shortestPathCalculator;

	private AnimationState playState;

	private AnimationFSM()
	{
	}

	public AnimationState getPlayState()
	{
		return playState;
	}

	public Optional<GraphPath<State, Connection>> getPath(@Nonnull State sourceState, @Nonnull State endState)
	{
		if (!stateGraph.containsVertex(sourceState))
		{
			throw new RuntimeException("Source state " + sourceState + " not found!");
		}

		if (!stateGraph.containsVertex(endState))
		{
			throw new RuntimeException("End state " + endState + " not found!");
		}

		return Optional.ofNullable(shortestPathCalculator.getPath(sourceState, endState));
	}

	public boolean isClientOnly()
	{
		return clientOnly;
	}

	/**
	 * Returns an immutable graph representing all the possible transitions and states in this state machine.
	 *
	 * The graph is weighted, doesn't allow self-loops, and supports duplicate edges
	 */
	public Graph<State, Connection> getStateGraph()
	{
		return this.stateGraph;
	}

	public Set<Connection> getAllOutGoing(State sourceState)
	{
		return this.stateGraph.outgoingEdgesOf(sourceState);
	}

	public static class Builder
	{
		private final AnimationFSM fsm;
		private final GraphBuilder<State, Connection, Graph<State, Connection>> graphBuilder;
		private HashMap<Connection, Double> anyConnections = new HashMap<>();
		private Set<State> allStates = new LinkedHashSet<>();


		public Builder()
		{
			this.fsm = new AnimationFSM();
			graphBuilder = GraphTypeBuilder.<State, Connection>directed().allowingMultipleEdges(true).allowingSelfLoops(false).weighted(true).buildGraphBuilder();
		}

		/**
		 * Registers a transition between two states in the state machine
		 * Duplicate connections will be removed.
		 *
		 * @param weight The higher this number is the lower priority the path will be.
		 */
		public Builder transition(Transition transition, double weight)
		{
			//Filters out all connections that start from State.ANY and adds them to the graph
			//Also adds any connections that start at State.ANY and puts them in anyConnections
			transition.getConnections().stream()
					.map(Map.Entry::getValue)
					.peek(con -> consumeIf(con, x -> anyConnections.put(x, weight), con.isANY))
					.filter(con -> !con.isANY)
					.forEach(con ->
					{
						graphBuilder.addVertex(con.startState);
						graphBuilder.addVertex(con.endState);
						graphBuilder.addEdge(con.startState, con.endState, con, weight);
						allStates.add(con.startState);
						allStates.add(con.endState);
					});
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
			this.anyConnections.forEach((con, weight) -> {
				graphBuilder.addVertex(con.endState);
				allStates.stream()
						.filter(x -> !x.equals(con.endState))
						.forEach(state -> graphBuilder.addEdge(state, con.endState, new Connection(state, con.endState, con.transition, true), weight));
			});
			fsm.stateGraph = graphBuilder.buildAsUnmodifiable();
			fsm.shortestPathCalculator = new DijkstraShortestPath(fsm.stateGraph);
			Preconditions.checkState(fsm.stateGraph.edgeSet().size() != 0, "Cannot have 0 transitions in a FSM");
			return this.fsm;
		}
	}
}
