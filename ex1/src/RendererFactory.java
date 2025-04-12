public class RendererFactory {
	public RendererFactory () {}
	public Renderer buildRenderer(String type, int size) {
		switch (type) {
			case "console":
				return new ConsoleRenderer(size);
			case "void":
				return new VoidRenderer();
		}
		return null;

	}
}
