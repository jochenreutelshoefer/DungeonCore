package figure.memory;

import item.Item;

public class ItemMemory extends MemoryObject{
	
	private String text;
	private Class clazz;
	private int worth;
	private boolean isMagic;
	
	public ItemMemory(Item it) {
		worth = it.getWorth();
		isMagic = it.isMagic();
		String itemText = it.getText();
		text = null;
		if(itemText != null) {
			text = new String(itemText);
		}
		clazz = it.getClass();
	}
	
	public Class getClazz() {
		return clazz;
	}
	
	public String getText() {
		return text;
	}

	public boolean isMagic() {
		return isMagic;
	}

	public int getWorth() {
		return worth;
	}

}
