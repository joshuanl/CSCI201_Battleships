package cs201;

import java.io.Serializable;

class ChatMessageObject implements Serializable{

	private static final long serialVersionUID = 1L;
	private String message;
	private int logNum;
	public ChatMessageObject(String message, int logNum){
		this.message = message;
		this.logNum = logNum;
	}//end of chat message
	
	public String getMessage(){
		return message;
	}
	
	public int getLogNum(){
		return logNum;
	}
}//end of class

