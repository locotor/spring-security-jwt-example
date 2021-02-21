package cn.locotor.springsecurityjwtexample.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Role {
    @NonNull
    private String id;
    
    @NonNull
    private String roleName;
}