package gui.bot;

import java.util.List;

import javax.swing.JComboBox;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;
import ai.GuiAI;

public class BotAISelectionBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BotAISelectionBox() {
		List<Class<?>> aiClasses = CPScanner.scanClasses(new ClassFilter()
				.superClass(AbstractHeroBotAI.class));
		Class<?> demoBot = null;
		for (Class<?> clazz : aiClasses) {
			this.addItem(clazz);
			if (clazz.getName().endsWith("DemoBot")) {
				demoBot = clazz;
			}
		}

		this.setSelectedItem(demoBot);
	}

	@SuppressWarnings("unchecked")
	public Class<? extends GuiAI> getSelectedBotAIClass() {
		return (Class<? extends GuiAI>) this.getSelectedItem();
	}

}
