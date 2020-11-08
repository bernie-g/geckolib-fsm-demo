package geckolib.fsm.transition;

import software.bernie.geckolib.core.easing.EasingType;

import java.util.function.Function;

public class CurveClip
{
	EasingType easingType;
	Function<Double, Double> customEase;
	double length;

	CurveClip()
	{

	}

	public double getLength()
	{
		return length;
	}

	public EasingType getEasingType()
	{
		return easingType;
	}

	public Function<Double, Double> getCustomEase()
	{
		return customEase;
	}
}
