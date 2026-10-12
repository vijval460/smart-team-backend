package com.teamforge.teammatch.model;

import java.util.List;

public record TeamRecommendation(
        List<MatchedMember> members,
        List<String> coveredSkills,
        List<String> missingSkills,
        int coverage,
        int averageMatch
) {
    public record MatchedMember(TeamMember member, int matchScore, List<String> matchedSkills) {}
}
