/**
 * Renderer Factory class
 * used for creating a renderer
 * @author Itay Turniansky
 */
public class RendererFactory {
	private static final String TYPE_ONE = "console";
	private static final String TYPE_TWO = "void";
	/**
	 * Default Constructor
	 */
	public RendererFactory() {
	}

	/**
	 * build a renderer
	 * @param type renderer type
	 * @param size used in console renderer for displaying
	 * @return renderer
	 */

	public Renderer buildRenderer(String type, int size) {
		switch (type) {
			case TYPE_ONE:
				return new ConsoleRenderer(size);
			case TYPE_TWO:
				return new VoidRenderer();
		}
		return null;

	}
}
