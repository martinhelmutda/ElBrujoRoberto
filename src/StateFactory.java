
public class StateFactory {
	public static BoardState status = null;
	
	private StateFactory() {} //Constructor
	
	public static BoardState getState(int seleccion, Board tab) {
		switch(seleccion) {
			case 1: 
				return new Start(tab); 
			case 2: 
				if(status==null) {
					status = new Loader(tab);
				}
				return status;
			case 3:
				if(status==null) {
					status = new Turn1(tab);
				}
				return status;
			case 4:
				if(status==null) {
					status = new Turn2(tab);
				}
				return status;
			case 5:
				return new Win(tab);
			case 6:
				return status=null; //MATA_STATUS
		}
		return null;
		
	}
	
}
