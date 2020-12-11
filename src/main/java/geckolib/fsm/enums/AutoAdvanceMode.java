package geckolib.fsm.enums;

/**
 * Controls if this transition is run automatically once a state is reached
 */
public enum AutoAdvanceMode
{
	/**
	 * Automatically advances at the appropriate SwitchTime as soon as a state is reached
	 */
	ON,

	/**
	 * Disables automatic advancing
	 */
	OFF,

	/**
	 * Only advances once a predicate returns true.
	 */
	PREDICATE
}
