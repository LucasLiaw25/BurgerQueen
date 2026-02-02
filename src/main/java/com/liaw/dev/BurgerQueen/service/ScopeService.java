package com.liaw.dev.BurgerQueen.service;

import com.liaw.dev.BurgerQueen.dto.dtos.ScopeDTO;
import com.liaw.dev.BurgerQueen.entity.Scope;
import com.liaw.dev.BurgerQueen.exception.exceptions.ScopeNotFoundException;
import com.liaw.dev.BurgerQueen.mapper.ScopeMapper;
import com.liaw.dev.BurgerQueen.repository.ScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScopeService {

    private final ScopeMapper mapper;
    private final ScopeRepository repository;

    public ScopeDTO createScope(ScopeDTO dto){
        Scope scope = mapper.toEntity(dto);
        repository.save(scope);
        return mapper.toDTO(scope);
    }

    public List<ScopeDTO> listScope(){
        List<Scope> scopes = repository.findAll();
        return scopes.stream().map(mapper::toDTO).toList();
    }

    public ScopeDTO findById(Long id){
        Scope scope = repository.findById(id)
                .orElseThrow(()-> new ScopeNotFoundException("Escopo não encontrado"));
        return mapper.toDTO(scope);
    }

    public void deleteScope(Long id){
        Scope scope = repository.findById(id)
                .orElseThrow(()-> new ScopeNotFoundException("Escopo não encontrado"));
        repository.deleteById(scope.getId());
    }

}
