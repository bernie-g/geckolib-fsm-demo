package geckolib.fsm;

import geckolib.fsm.state.State;
import geckolib.fsm.transition.Transition;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;

public class FSMInfo
{
	private final Graph<State, Transition> graph;
	private final GraphPath<State, Transition> currentPath;
	private final int pathIndex;

	public FSMInfo(Graph<State, Transition> graph, GraphPath<State, Transition> currentPath, int pathIndex)
	{
		this.graph = graph;
		this.currentPath = currentPath;
		this.pathIndex = pathIndex;
	}
}
