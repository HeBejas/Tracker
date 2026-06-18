package com.aleksandrov.tracker.model;

import lombok.Data;
import java.io.Serializable;

@Data
public class TaskParticipantId implements Serializable {
    private Long task;
    private Long user;
    private Long roleId;
}
