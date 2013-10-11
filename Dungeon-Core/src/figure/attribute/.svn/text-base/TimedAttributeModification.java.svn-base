package figure.attribute;

import item.PotionMod;


/**
 * Modifikation eines Attributes um einen Wert fuer eine gewisse Zeit.
 *
 */
public class TimedAttributeModification{

	
	private Attribute a;

    private int value;

    private int round;
   
    public  boolean fired;

    public TimedAttributeModification(Attribute att, int v, int rounds){
		a = att;
		value = v;
		round = rounds;
		fired = false;
		if(round == 0) fire();
    }

 	public TimedAttributeModification(Attribute att, PotionMod values){
		a = att;
		value = values.getValue();
		round = values.getRound();
		fired = false;
		if(round == 0) fire();
    }


    public boolean newRound(){
		round--;
		if((round < 1) && (fired == false)){
	   	 fire();
	   	 return true;
		}
		return false;
    }
    

    private void fire(){
	//System.out.println("Modifikation gefeuert!!! "+Integer.toString(value));
	a.modValue(value);
	//System.out.println(Double.toString(a.getValue()));
	fired = true;
    }

    public boolean hasFired(){
	return fired;
    }
    public String toString(){
	return ("Wert: "+Integer.toString(value)+" zeit: "+Integer.toString(round));

    }
	
	public String getText() {
		String s = a.toString();
		s += " "+value;
		return s;
	
    }
}
