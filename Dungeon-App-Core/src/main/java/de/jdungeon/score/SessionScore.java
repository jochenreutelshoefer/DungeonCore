package de.jdungeon.score;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.jdungeon.user.DungeonFactory;
import de.jdungeon.user.DungeonSession;

public class SessionScore implements Json.Serializable {



    public String getSessionID() {
        return sessionID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getHeroName() {
        return heroName;
    }

    public int[] getSolvedLevelScores() {
        return solvedLevelScores;
    }

    public int getFails() {
        return fails;
    }

    public int getTotalScore() {
        return totalScore;
    }

    private String sessionID;
    private String playerName;
    private String heroName;
    private int[] solvedLevelScores;
    private int fails;
    private int totalScore;

    private String startTime;

    public SessionScore() {
    }

    public SessionScore(String sessionID, String playerName, String heroName, int[] solvedLevelScores, int fails, String startTime) {
        this.sessionID = sessionID;
        this.playerName = playerName;
        this.heroName = heroName;
        this.solvedLevelScores = solvedLevelScores;
        this.fails = fails;
        for (int solvedLevelScore : solvedLevelScores) {
            totalScore += solvedLevelScore;
        }
    }

    public String getStartTime() {
        return startTime;
    }

    public static SessionScore create(DungeonSession session) {

        Map<DungeonFactory, Integer> completedDungeons = session.getCompletedDungeons();
        List<DungeonFactory> completedDungeonsList = session.getCompletedDungeonsList();
        int[] scores = new int[completedDungeonsList.size()];
        int index = 0;
        for (DungeonFactory dungeonFactory : completedDungeonsList) {
            Integer integer = completedDungeons.get(dungeonFactory);
            if (integer != null) {
                scores[index] = integer.intValue();
            }
            index++;
        }
        String startTime = session.getSessionStart().toString();
        return new SessionScore(
                session.getSessionID(),
                session.getPlayerName(),
                session.getCurrentHero().getName(),
                scores,
                session.getNumberOfFails(),
                startTime);
    }


    private static final String SESSION_ID = "sessionID";
    private static final String PLAYER = "player_name";
    private static final String HERO = "hero_name";
    private static final String FAILS = "fails";
    private static final String TOTAL_SCORE = "total_score";
    private static final String START_TIME = "start_time";

    @Override
    public void write(Json json) {
        json.writeValue(SESSION_ID, sessionID);
        json.writeValue(HERO, heroName);
        json.writeValue(PLAYER, playerName);
        json.writeValue(FAILS, fails);
        json.writeValue(TOTAL_SCORE, totalScore);
        json.writeValue(START_TIME, startTime);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        sessionID = jsonData.getString(SESSION_ID);
        heroName = jsonData.getString(HERO);
        playerName = jsonData.getString(PLAYER);
        fails = jsonData.getInt(FAILS);
        totalScore = jsonData.getInt(TOTAL_SCORE);
        startTime = jsonData.getString(START_TIME);
    }
}
