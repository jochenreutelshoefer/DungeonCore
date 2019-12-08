package dungeon;

//import com.tngtech.java.junit.dataprovider.DataProviderRunner;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
import junit.framework.TestCase;
import org.mockito.Mockito;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.11.19.
 */
//@RunWith(DataProviderRunner.class)
public class RoomTest extends TestCase {

	Dungeon dungeonMock;
	int x = 1;
	int y = 2;

	//@Before
	public void init() {
		dungeonMock = Mockito.mock(Dungeon.class);
		when(dungeonMock.getPoint(x, y)).thenReturn(new JDPoint(x, y));


	}
	public void testCreateRoom() {
		init(); // @Before not working for some reason
		Room room = new Room(x, y, dungeonMock);

		assertEquals("Room X not correct", room.getX(), x);
		assertEquals("Room Y not correct",room.getY(), y);
	}
}
