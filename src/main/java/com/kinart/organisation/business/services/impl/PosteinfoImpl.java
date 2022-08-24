package com.kinart.organisation.business.services.impl;

import com.kinart.api.organisation.dto.PosteinfoDto;
import com.kinart.organisation.business.repository.PosteinfoRepository;
import com.kinart.organisation.business.services.PosteinfoService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PosteinfoImpl implements PosteinfoService {

    private PosteinfoRepository repository;

    @Autowired
    public PosteinfoImpl(PosteinfoRepository repository){
        this.repository = repository;
    }

    @Override
    public PosteinfoDto save(PosteinfoDto dto) {
        return PosteinfoDto.fromEntity(
                repository.save(
                        PosteinfoDto.toEntity(dto)
                )
        );
    }

    @Override
    public PosteinfoDto findById(Integer id) {
        return repository.findById(id).map(PosteinfoDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucune information avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND));
    }

    @Override
    public List<PosteinfoDto> findAll() {
        return repository.findAll().stream()
                .map(PosteinfoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Info ID is null");
            return;
        }
        repository.deleteById(id);
    }
}
