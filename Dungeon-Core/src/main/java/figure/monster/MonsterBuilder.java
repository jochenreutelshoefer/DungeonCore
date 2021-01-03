package figure.monster;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.21.
 */
public class MonsterBuilder {

	private Class<? extends Monster> monsterClass;
	private int changeToHitBaseValue;
	private int damageBaseValue;
	private int damageVariance;

	public MonsterBuilder(Class<? extends Monster> monsterClass, int changeToHitBaseValue, int damageBaseValue, int damageVariance) {
		this.monsterClass = monsterClass;
		this.changeToHitBaseValue = changeToHitBaseValue;
		this.damageBaseValue = damageBaseValue;
		this.damageVariance = damageVariance;
	}

	public MonsterBuilder(Class<? extends Monster> monsterClass, int value) {
		this.monsterClass = monsterClass;
		this.changeToHitBaseValue = changeToHitBaseValue;
		this.damageBaseValue = damageBaseValue;
		this.damageVariance = damageVariance;
	}

}
