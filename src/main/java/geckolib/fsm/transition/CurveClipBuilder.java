package geckolib.fsm.transition;

import com.google.common.base.Preconditions;
import software.bernie.geckolib.core.easing.EasingType;

import java.util.Optional;
import java.util.function.Function;

public class CurveClipBuilder
{
	private final TransitionBuilder builder;
	private CurveClip clip;

	public CurveClipBuilder(TransitionBuilder builder)
	{
		this.builder = builder;
		this.clip = new CurveClip();
	}

	public CurveClipBuilder curveLength(double seconds)
	{
		clip.length = seconds;
		return this;
	}

	public CurveClipBuilder customEase(Function<Double, Double> customEase)
	{
		clip.customEase = customEase;
		clip.easingType = EasingType.CUSTOM;
		return this;
	}

	public CurveClipBuilder ease(EasingType ease)
	{
		clip.easingType = ease;
		return this;
	}

	public TransitionBuilder createCurve()
	{
		Preconditions.checkState(clip.length != 0, "Clip length cannot be zero");
		if (clip.easingType == EasingType.CUSTOM)
		{
			Preconditions.checkState(clip.customEase != null, "Must provide a custom ease method to use EasingType.CUSTOM");
		}
		else
		{
			Preconditions.checkState(clip.customEase == null, "Cannot use a custom ease method when easingtype is not CUSTOM");
		}
		clip.easingType = Optional.ofNullable(clip.easingType).orElse(EasingType.Linear);
		this.builder.transition.curveClip = clip;
		return this.builder;
	}
}
