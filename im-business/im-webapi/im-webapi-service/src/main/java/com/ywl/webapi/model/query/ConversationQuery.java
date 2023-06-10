package com.ywl.webapi.model.query;

import lombok.Data;

@Data
public class ConversationQuery {

    private String userId;

    private Integer nextConversation;

    private Integer count;

}
