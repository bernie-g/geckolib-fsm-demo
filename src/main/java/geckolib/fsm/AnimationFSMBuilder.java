package geckolib.fsm;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import geckolib.fsm.state.State;
import geckolib.fsm.transition.Transition;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;

public class AnimationFSMBuilder
{
	private final AnimationFSM fsm;
	private final Graph<State, Transition> graph;

	public AnimationFSMBuilder()
	{
		this.fsm = new AnimationFSM();
		this.graph = new DefaultDirectedGraph<>(Transition.class);
	}

	public AnimationFSMBuilder transition(Transition transition)
	{
		graph.addVertex(transition.getStartState());
		graph.addVertex(transition.getEndState());
		graph.addEdge(transition.getStartState(), transition.getEndState(), transition);
		return this;
	}

	public AnimationFSMBuilder allowClientControl()
	{
		this.fsm.allowClientControl = true;
		return this;
	}

	public AnimationFSM build()
	{
		fsm.stateGraph = graph;
		fsm.pathCalculator = new DijkstraShortestPath(graph);
		Preconditions.checkState(graph.edgeSet().size() != 0, "Cannot have 0 transitions in a FSM");
		return this.fsm;
	}
}
