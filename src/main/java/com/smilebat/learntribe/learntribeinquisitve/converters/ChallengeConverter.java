package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.AssessmentRequest;
import com.smilebat.learntribe.inquisitve.response.ChallengeResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Challenge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Challenge Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 */

@Component
public final class ChallengeConverter {

    /**
     * Converts the {@link Challenge} to {@link ChallengeResponse}.
     *
     * @param challenge the {@link Challenge}
     * @return the {@link ChallengeResponse}
     */
    public ChallengeResponse toResponse(Challenge challenge){
        ChallengeResponse challengeResponse = new ChallengeResponse();
        challengeResponse.setId(challenge.getId());
        challengeResponse.setQuestion(challenge.getQuestion());
        challengeResponse.setOptions(challenge.getOptions());
        return challengeResponse;
    }

    public List<ChallengeResponse> toResponse(List<Challenge> challenges){
        return challenges.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
