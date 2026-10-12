package com.teamforge.teammatch.controller;

import com.teamforge.teammatch.model.ProjectBrief;
import com.teamforge.teammatch.model.TeamMember;
import com.teamforge.teammatch.model.TeamRecommendation;
import com.teamforge.teammatch.service.MatchingService;
import com.teamforge.teammatch.service.MemberDirectory;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TeamController {
    private final MemberDirectory directory;
    private final MatchingService matchingService;

    public TeamController(MemberDirectory directory, MatchingService matchingService) {
        this.directory = directory;
        this.matchingService = matchingService;
    }

    @GetMapping("/members")
    public List<TeamMember> members() {
        return directory.findAll();
    }

    @PostMapping("/projects/recommend")
    public TeamRecommendation recommend(@Valid @RequestBody ProjectBrief brief) {
        return matchingService.recommend(brief);
    }
}
