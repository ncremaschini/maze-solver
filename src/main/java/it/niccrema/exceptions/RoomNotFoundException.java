package it.niccrema.exceptions;

public class RoomNotFoundException extends RuntimeException{

    /**
     *
     */
    private static final long serialVersionUID = 5260287048168870156L;

    public RoomNotFoundException(Integer roomId) {
        super(String.format("Room id %d not found", roomId));
    }
    
}
