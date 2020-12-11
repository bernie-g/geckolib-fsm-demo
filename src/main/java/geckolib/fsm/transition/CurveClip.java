package geckolib.fsm.transition;

import com.google.common.base.Preconditions;
import software.bernie.geckolib.core.easing.EasingType;

import java.util.Optional;
import java.util.function.Function;

public class CurveClip
{
	private EasingType easingType;
	private Function<Double, Double> customEase;
	private double length;

	private CurveClip()
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

	public static class Builder
	{
		private CurveClip clip;

		public Builder()
		{
			this.clip = new CurveClip();
		}


		/**
		 * The length of the transition curve, in seconds
		 *
		 * @param seconds the seconds
		 * @return the builder
		 */
		public Builder curveLength(double seconds)
		{
			clip.length = seconds;
			return this;
		}

		/**
		 * Sets a custom ease method. Read more about custom easings in the Geckolib wiki.
		 */
		public Builder customEase(Function<Double, Double> customEase)
		{
			clip.customEase = customEase;
			clip.easingType = EasingType.CUSTOM;
			return this;
		}

		/**
		 * Sets the easing type, such as easeInSine, easeInOutExpo, etc. Pass in EasingType.CUSTOM and call {@link CurveClip.Builder#customEase}
		 *
		 */
		public Builder ease(EasingType ease)
		{
			clip.easingType = ease;
			return this;
		}

		/**
		 * Builds the curve.
		 */
		public CurveClip createCurve()
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
			return this.clip;
		}
	}

}
