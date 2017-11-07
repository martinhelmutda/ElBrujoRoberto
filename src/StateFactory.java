
public class StateFactory {
	public static GameState status = null;
	
	private StateFactory() {} //Constructor
	
	public static GameState getState(int seleccion, GameStateContext actual) {
		switch(seleccion) {
			case 1: return new GameStateListo(actual);
			case 2: if(status==null) {
				status = new GameStateJugando(actual);
			}
			return status;
		}
		return null;
		
	}
	
}
