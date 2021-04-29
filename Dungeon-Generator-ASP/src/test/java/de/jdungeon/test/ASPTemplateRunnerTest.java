package de.jdungeon.test;

import de.jdungeon.dungeon.builder.DungeonBuilderASP;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.DungeonResult;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;


public class ASPTemplateRunnerTest extends TestCase {

    @Test
    public void testASPSolverExec() throws DungeonGenerationException {
        DungeonBuilderASP dungeonBuilderASP = new DungeonBuilderASP();
        DungeonResult defaultSettings = dungeonBuilderASP.build();
        assert(defaultSettings != null);
    }
}
