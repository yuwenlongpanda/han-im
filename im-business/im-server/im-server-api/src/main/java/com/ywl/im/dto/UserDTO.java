package com.ywl.im.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1;
    private String id;
    private String avatar;
    private String nickname;
}
