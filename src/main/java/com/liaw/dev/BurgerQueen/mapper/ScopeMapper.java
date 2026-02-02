package com.liaw.dev.BurgerQueen.mapper;

import com.liaw.dev.BurgerQueen.dto.dtos.ScopeDTO;
import com.liaw.dev.BurgerQueen.entity.Scope;
import org.springframework.stereotype.Component;

@Component
public class ScopeMapper {

    public ScopeDTO toDTO(Scope scope){
        return new ScopeDTO(
                scope.getId(),
                scope.getName()
        );
    }

    public Scope toEntity(ScopeDTO scope){
        return new Scope(
                scope.getId(),
                scope.getName()
        );
    }

}
