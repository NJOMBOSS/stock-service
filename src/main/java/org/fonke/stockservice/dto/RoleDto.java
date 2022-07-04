package org.fonke.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.fonke.stockservice.entity.Role;


@Data
@Builder
public class RoleDto {

    private Integer id;

    private String roleName;

    @JsonIgnore
    private UtilisateurDto utilisateurDto;

    public RoleDto fromEntity(Role role){
        if(role == null){
            return null;
        }

        return RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }

    public Role toEntity(RoleDto roleDto){
        if(roleDto == null){
            return null;
        }

        Role r = new Role();
        r.setId(roleDto.getId());
        r.setRoleName(roleDto.getRoleName());
        return r;
    }
}
