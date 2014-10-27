package GUI;

import Core.Game;
import Core.User;
import mab.EpsilonGreedy;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketAppender;

import java.util.ArrayList;
import java.util.List;

public class GuiMain {
	public static void main(String[] args) {

		// reset logging configuration
		Logger.getRootLogger().getLoggerRepository().resetConfiguration();

		//configure logger
		SocketAppender appender = new SocketAppender();
		appender.setName("centeral");
		appender.setRemoteHost("178.62.214.87");
		appender.setPort(9998);
		appender.setReconnectionDelay(10000);
		appender.setLocationInfo(true);
		appender.setThreshold(Level.INFO);
		// works only if acces to server exist
		// todo: enable it to log!
		appender.activateOptions();

		//set logger
		Logger.getRootLogger().addAppender(appender);


		List<User> users = new ArrayList<>();
		users.add(new EpsilonGreedy(1));
		users.add(new EpsilonGreedy(2));
		int boardsize = 7;
		int bombCounter = 8;
		int explosionArea = 4;

		int maxSteps = 100;

		new GuiStart(new Game(users, boardsize, bombCounter, explosionArea, maxSteps));
	}
}
