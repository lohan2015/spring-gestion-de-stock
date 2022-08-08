package com.kinart.organisation.business.services;

import com.kinart.api.organisation.dto.PosteinfoDto;

import java.util.List;

public interface PosteinfoService {

    PosteinfoDto save(PosteinfoDto dto);

    PosteinfoDto findById(Integer id);

    List<PosteinfoDto> findAll();

    void delete(Integer id);
}
