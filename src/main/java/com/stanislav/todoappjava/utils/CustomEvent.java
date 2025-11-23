package com.stanislav.todoappjava.utils;

import javafx.event.Event;
import javafx.event.EventType;

public class CustomEvent extends Event {

    public static final EventType<CustomEvent> OPEN_ADD_NOTE =
            new EventType<>(Event.ANY, "OPEN_ADD_NOTE");

    public static  final  EventType<CustomEvent> OPEN_NOTES_VIEW =
            new EventType<>(Event.ANY, "OPEN_NOTES_VIEW");

    public static  final  EventType<CustomEvent> OPEN_NOTE_VIEW =
            new EventType<>(Event.ANY, "OPEN_NOTE_VIEW");

    public static final  EventType<CustomEvent> REFRESH_NOTES =
            new EventType<>(Event.ANY, "REFRESH_NOTES");

    public CustomEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
