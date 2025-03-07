package com.game.backend.services.wiki.impl;

import com.game.backend.models.User;
import com.game.backend.repositories.wiki.WikiContributionRepository;
import com.game.backend.security.response.UserDetailsResponse;
import com.game.backend.services.wiki.WikiContributionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WikiContributionServiceImpl implements WikiContributionService {
    private final WikiContributionRepository wikiContributionRepository;
    private final ModelMapper modelMapper;

    public WikiContributionServiceImpl(final WikiContributionRepository wikiContributionRepository, final ModelMapper modelMapper) {
        this.wikiContributionRepository = wikiContributionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserDetailsResponse> getWikiContributors() {
        List<User> contributors = wikiContributionRepository.findDistinctContributors();

        return contributors.stream()
                .map(user -> modelMapper.map(user, UserDetailsResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDetailsResponse> getArticleContributors(String slug) {
        List<User> contributors = wikiContributionRepository.findContributorsByArticleSlug(slug);

        return contributors.stream()
                .map(user -> modelMapper.map(user, UserDetailsResponse.class))
                .collect(Collectors.toList());
    }
}
