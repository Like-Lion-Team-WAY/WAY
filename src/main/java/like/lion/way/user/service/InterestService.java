package like.lion.way.user.service;

import like.lion.way.user.domain.Interest;

public interface InterestService {

    Interest findOrSaveInterest(String interestName);
    Interest findByInterestName(String interestName);
    Interest saveInterest(Interest interest);
}