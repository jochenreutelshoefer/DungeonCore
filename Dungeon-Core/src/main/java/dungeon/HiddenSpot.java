
package dungeon;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.memory.MemoryObject;
import figure.memory.SpotMemory;
import game.InfoEntity;
import game.InfoProvider;
import gui.Paragraph;
import gui.Paragraphable;
import item.Item;
import item.ItemInfo;
import item.interfaces.ItemOwner;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import util.JDColor;

/**
 * Ein Raum kann ein Versteck haben, es ist zu Beginn unsichtbar und kann per Zufall
 * oder mit dem Entdeckenzauberspruch gefunden werden. Es kann Gegenstaende enthalten.
 *
 */
public class HiddenSpot implements ItemOwner,Paragraphable,InfoProvider {

	
	private boolean found = false;

	
	private List<Item> items = new LinkedList<Item>();

	
	private int difficulty;

	
	private Room location;

	
	public HiddenSpot(Room location,int difficulty, LinkedList items) {
		this.location = location;
		this.difficulty = difficulty;
		this.items = items;
			
	}
	public HiddenSpot(Room location,int difficulty, Item i) {
		this.location = location;
		this.difficulty = difficulty;
		this.items.add(i);
			
	}
	@Override
	public Item getItem(ItemInfo it) {
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			if(ItemInfo.makeItemInfo(element,null).equals(it)) {
				return element;
			}
			
		}
		return null;
	}
	
	public MemoryObject getMemoryObject(FigureInfo info) {
		return new SpotMemory();
	}
	
	@Override
	public boolean addItems(List l, ItemOwner o) {
		for(int i = 0; i < l.size();i++) {
			Item it = (Item)(l.get(i));
			this.takeItem(it);
		}
		return true;
	}
	
	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new SpotInfo(this,map);
	}
	@Override
	public Item getItemNumber(int i) {
		return items.get(i);
	}
	
	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for(int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(items.get(i),map);
		}
		return array;
	}

	
	@Override
	public Room getRoom() {
		return location;
	}

	
	public boolean found(int psycho) {
			int k  = (int)(Math.random()*difficulty);
			if(k < psycho - 2) {
				found = true;
				return true;	
			}
			else {
				return false;	
			}
			
	}
	
	@Override
	public Paragraph[] getParagraphs() {
		Paragraph [] p = new Paragraph[3];
		p[0] = new Paragraph("Versteck");
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();
		
			
		String s2 = new String("(entdeckt)");
		
		p[1] = new Paragraph(s2);
		p[1].setSize(14);
		p[1].setCentered();
		p[1].setBold();
		
		String itemS = new String("");
		////System.out.println("in Kiste; "+items.size());
		for(int i = 0; i < items.size(); i++) {
			Item it = (items.get(i));
			////System.out.println(it.toString());
			itemS += it.toString()+"  ";	
		}
		p[2] = new Paragraph(itemS);
		p[2].setCentered();
		p[2].setSize(12);
		return p;
		
	}
	
	public void clicked(Figure f) {
		
//		if(f.isGuiControlled()) {	
//		ItemChoiceSpotView view = new ItemChoiceSpotView(f.getControl().getGui().getMainFrame(),
//		"Versteck",
//		f,
//		this,
//		true);
//		}
	
			
		
	}
	
	@Override
	public boolean removeItem(Item i) {
		return items.remove(i);	
	}
	
	
	
	@Override
	public boolean takeItem(Item i) {
		items.add(i);
		Item.notifyItem(i,this);
		return true;
	}

	
	public int getDifficulty() {
		return difficulty;
	}

	
	public boolean isFound() {
		return found;
	}

	
	public List<Item> getItems() {
		return items;
	}


	
//	public room getLocation() {
//		return location;
//	}
	
	@Override
	public JDPoint getLocation() {
		return location.getPoint();
	}

	
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	
	public void setFound(boolean found) {
		this.found = found;
	}

	
	public void setItems(LinkedList items) {
		this.items = items;
	}

	
	public void setLocation(Room location) {
		this.location = location;
	}

}