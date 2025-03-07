package com.game.backend.services.wiki;

import com.game.backend.security.response.UserDetailsResponse;

import java.util.List;

public interface WikiContributionService {
    List<UserDetailsResponse> getWikiContributors();

    List<UserDetailsResponse> getArticleContributors(String slug);
}
