package geckolib.fsm.enums;

public enum SwitchTime
{
	/**
	 * State machine transitions from the source state instantly, does not wait for it to end.
	 */
	IMMEDIATE,

	/**
	 * Transitions as soon as the source state ends
	 */
	END,

	/**
	 * Transitions as soon as the source state ends, with an extra delay at the end that can be set with .delayTime()
	 */
	END_DELAYED,

	/**
	 * Transitions a certain amount of time after the source state has run, can be set with .delayTime()
	 */
	BEGIN_DELAYED
}
