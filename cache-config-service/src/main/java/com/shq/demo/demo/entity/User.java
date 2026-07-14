package com.shq.demo.demo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private Long id;

    private String userName;

    private Integer gender;

}
