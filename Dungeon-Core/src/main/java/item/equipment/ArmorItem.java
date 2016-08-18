package item.equipment;
import item.Item;
import figure.attribute.Attribute;
import game.JDEnv;

public class ArmorItem extends EquipmentItem{

	
	public int armorValue;
	private int damg = 0;
	int dmgPerHP = 35;
	
	public ArmorItem(int armor, int value, boolean m, int hitPoints) {
		
		super(value, m);
		armorValue = armor;
		this.hitPoints = new Attribute(Attribute.HIT_POINTS,hitPoints);
	}
	

	public void hit(int value) {
		damg += value;
		if(damg > dmgPerHP) {
			int points = damg/dmgPerHP;
			if(getHitPoints().getValue() > 0) {
				this.getHitPoints().modValue((-1) * points);
			}
		}
		damg = damg % dmgPerHP;
	}
	
	public int getArmorValue() {
		if (hitPoints.perCent() >= 70) {
			return armorValue;
		} else if (hitPoints.perCent() >= 50) {
			return (int) (2 * ((float) armorValue) / 3);
		} else if (hitPoints.perCent() >= 40) {
			return (int) (1 * ((float) armorValue) / 3);
		} else if (hitPoints.perCent() >= 30) {
			return (int) (1 * ((float) armorValue) / 4);
		} else if (hitPoints.perCent() >= 20) {
			return (int) (1 * ((float) armorValue) / 6);
		} else {
			return 0;
		}

	}

    
	public void takeRelDamage(double d ) {
				double k = (int) (d * hitPoints.getValue());
				hitPoints.modValue((-1)*k); 
			}
		

 public String getText(){
	return ("\n"+JDEnv.getResourceBundle().getString("gui_armor")+": "+getArmorValue()+"/"+armorValue+"\n"+ JDEnv.getResourceBundle().getString("state")+": "
	+ (int) hitPoints.getValue()
	+ "/"
	+ (int) hitPoints.getBasic())+"\n";
    } 

}
	
	
