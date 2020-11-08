package geckolib.fsm.event;

public interface IEventListener<T extends FSMEvent>
{
	void on(T event);
}
