package org.cybersapien.watercollection.processors;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public enum ProcessingState {
    /** State for not started */
    NOT_STARTED("NOT_STARTED"),
    /** State for in progres */
    IN_PROGRESS("IN_PROGRESS"),
    /** State for analysis complete */
    ANALYSIS_COMPLETE("ANALYSIS_COMPLETE");

    /**
     * Internal mapping of string value to enum
     */
    private final static Map<String, ProcessingState> valueMap = new HashMap<>();

    private String processingState;

    /*
      Initialization block for reverse lookup value map
     */
    static
    {
        for(ProcessingState processingState : ProcessingState.values())
        {
            valueMap.put(processingState.processingState, processingState);
        }
    }

    /**
     * Constructor for a string processing state
     *
     * @param processingState the processing state
     */
    ProcessingState(@NonNull String processingState) {
        this.processingState = processingState;
    }

    /**
     * Convert a processing state string to its enum
     *
     * @param processingState the processing state
     *
     * @return the Processing state enum
     */
    public static ProcessingState fromString(@NonNull String processingState) {
        ProcessingState result = valueMap.get(processingState);
        if (null != result) {
            return result;
        } else {
            throw new IllegalArgumentException(processingState + "is not supported");
        }
    }

}
