package gui.bot;

import java.util.List;

import javax.swing.JComboBox;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ClassFilter;
import ai.AI;

public class BotAISelectionBox extends JComboBox {

	public BotAISelectionBox() {
		List<Class<?>> aiClasses = CPScanner.scanClasses(new ClassFilter()
				.superClass(AI.class));
		for (Class<?> clazz : aiClasses) {
			this.addItem(clazz);
		}
	}

	@SuppressWarnings("unchecked")
	public Class<? extends AI> getSelectedBotAIClass() {
		return (Class<? extends AI>) this.getSelectedItem();
	}

}
