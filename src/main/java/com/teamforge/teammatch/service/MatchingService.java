package com.teamforge.teammatch.service;

import com.teamforge.teammatch.model.ProjectBrief;
import com.teamforge.teammatch.model.TeamMember;
import com.teamforge.teammatch.model.TeamRecommendation;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class MatchingService {
    private final MemberDirectory directory;

    public MatchingService(MemberDirectory directory) {
        this.directory = directory;
    }

    public TeamRecommendation recommend(ProjectBrief brief) {
        List<String> requested = brief.requiredSkills().stream().map(String::trim).distinct().toList();
        List<Candidate> ranked = directory.findAll().stream()
                .map(member -> score(member, requested))
                .sorted(Comparator.comparingInt(Candidate::score).reversed()
                        .thenComparing(candidate -> candidate.member().name()))
                .toList();

        List<TeamRecommendation.MatchedMember> selected = new ArrayList<>();
        Set<String> covered = new LinkedHashSet<>();
        for (Candidate candidate : ranked) {
            if (selected.size() >= brief.teamSize()) break;
            if (selected.isEmpty() || candidate.score() > 0) {
                selected.add(new TeamRecommendation.MatchedMember(candidate.member(), candidate.score(), candidate.matchedSkills()));
                covered.addAll(candidate.matchedSkills());
            }
        }
        List<String> missing = requested.stream().filter(skill -> !containsIgnoreCase(covered, skill)).toList();
        int coverage = requested.isEmpty() ? 0 : (int) Math.round(covered.size() * 100.0 / requested.size());
        int averageMatch = selected.isEmpty() ? 0 : (int) Math.round(selected.stream().mapToInt(TeamRecommendation.MatchedMember::matchScore).average().orElse(0));
        return new TeamRecommendation(selected, List.copyOf(covered), missing, coverage, averageMatch);
    }

    private Candidate score(TeamMember member, List<String> requested) {
        List<String> matched = requested.stream()
                .filter(skill -> member.skills().stream().anyMatch(own -> own.equalsIgnoreCase(skill)))
                .toList();
        int skillFit = requested.isEmpty() ? 0 : (int) Math.round(matched.size() * 75.0 / requested.size());
        int experienceFit = Math.min(member.experienceYears(), 8) * 2;
        return new Candidate(member, Math.min(100, skillFit + experienceFit), matched);
    }

    private boolean containsIgnoreCase(Set<String> values, String value) {
        return values.stream().anyMatch(item -> item.toLowerCase(Locale.ROOT).equals(value.toLowerCase(Locale.ROOT)));
    }

    private record Candidate(TeamMember member, int score, List<String> matchedSkills) {}
}
