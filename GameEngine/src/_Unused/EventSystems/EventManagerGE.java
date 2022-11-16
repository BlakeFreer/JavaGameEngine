// Blake Freer
// Jun 4, 2020
// Handles subscription, unsubscription and notification of Listeners for input events

package _Unused.EventSystems;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EventManagerGE {
    
    Map<Integer, List<EventListenerGE>> listeners = new HashMap<>();
    
    public EventManagerGE(int... eventCode){
        // Constructor takes all of the InputEvents that will be needed in the game
        // Ideally this will be changed as to not require the events to be inputted here,
        // but rather they are added upon subscription
        
        for(int code : eventCode){
            TryAddOperation(code);
        }
    }
    
    public void Subscribe(int eventCode, EventListenerGE listener){
        // Add a listener to the list of listeners under a specific event
        // The listener will now be notified for this event
        TryAddOperation(eventCode);
        List<EventListenerGE> users = listeners.get(eventCode);
        users.add(listener);
    }
    public void Unsubscribe(int eventCode, EventListenerGE listener){
        // Remove a listener from the list that will be notified for the event
        List<EventListenerGE> users = listeners.get(eventCode);
        users.remove(listener);
    }
    
    public void Notify(InputEvent event){
        // Event is the keycode of the event that occured
        // ActionType is a description of the event ('pressed', 'released', etc)
        
        int code = -1;
        
        if(event instanceof KeyEvent){
            code = ((KeyEvent) event).getKeyCode();
        } else if(event instanceof MouseEvent){
            code = ((MouseEvent) event).getButton();
        }
        
        List<EventListenerGE> users = listeners.get(code);
        if (users != null) {
            for (EventListenerGE lis : users) {
                // Notify all listeners about the event
                lis.UpdateEvent(event);
            }
        }
    }
    
    private void TryAddOperation(int eventCode){
        // Add an operation the map of possible subscription operations
        // Ensure the operation is not already present in the map keys
        if(listeners.get(eventCode)==null){
            listeners.put(eventCode, new ArrayList<>());
        }
    }

}
