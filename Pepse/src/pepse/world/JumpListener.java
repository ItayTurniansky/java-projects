package pepse.world;

/**
 * An interface for listening to jump events triggered by the avatar.
 *
 * <p>Classes that implement this interface can respond when the avatar performs a jump.</p>
 *
 * @author itayturni
 */
public interface JumpListener {
	/**
	 * Called when the avatar jumps.
	 */
	void onJump();
}
