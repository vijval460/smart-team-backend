package com.teamforge.teammatch.service;

import com.teamforge.teammatch.model.TeamMember;
import com.teamforge.teammatch.model.TeamMemberEntity;
import com.teamforge.teammatch.repository.TeamMemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MemberDirectory {
    private static final List<TeamMember> DEMO_MEMBERS = List.of(
            new TeamMember("TM-014", "Maya Chen", "Product designer", "Design", List.of("UI/UX", "Figma", "User research", "Prototyping"), 5, "Available", "coral"),
            new TeamMember("TM-027", "Ethan Brooks", "Full-stack engineer", "Engineering", List.of("React", "TypeScript", "Node.js", "PostgreSQL", "REST APIs"), 6, "Available", "blue"),
            new TeamMember("TM-031", "Priya Nair", "Data scientist", "Data & AI", List.of("Python", "Machine learning", "SQL", "Data visualization", "Statistics"), 4, "Available", "green"),
            new TeamMember("TM-038", "Noah Williams", "Backend engineer", "Engineering", List.of("Java", "Spring Boot", "PostgreSQL", "REST APIs", "Docker"), 7, "Available", "gold"),
            new TeamMember("TM-042", "Sofia Ramirez", "Product manager", "Product", List.of("Product strategy", "Agile", "User research", "Roadmapping", "Analytics"), 8, "Available", "violet"),
            new TeamMember("TM-053", "Lucas Martin", "Frontend engineer", "Engineering", List.of("React", "TypeScript", "CSS", "Accessibility", "Figma"), 3, "Available", "blue"),
            new TeamMember("TM-061", "Ava Patel", "QA engineer", "Quality", List.of("Test automation", "Java", "API testing", "CI/CD", "Agile"), 5, "Available", "green"),
            new TeamMember("TM-072", "Oliver Kim", "ML engineer", "Data & AI", List.of("Python", "Machine learning", "MLOps", "Docker", "Cloud"), 5, "Available", "violet"),
            new TeamMember("TM-086", "Amara Okafor", "UX researcher", "Design", List.of("User research", "Usability testing", "Accessibility", "Analytics", "Prototyping"), 6, "Available", "coral"),
            new TeamMember("TM-093", "Leo Fischer", "DevOps engineer", "Platform", List.of("Cloud", "Docker", "Kubernetes", "CI/CD", "Monitoring"), 7, "Available", "gold")
    );

    private final TeamMemberRepository repository;

    public MemberDirectory(TeamMemberRepository repository) {
        this.repository = repository;
    }

    public List<TeamMember> findAll() {
        return repository.findAllByOrderByNameAsc().stream().map(TeamMemberEntity::toModel).toList();
    }

    public void seedIfEmpty() {
        if (repository.count() == 0) {
            repository.saveAll(DEMO_MEMBERS.stream().map(TeamMemberEntity::fromModel).toList());
        }
    }
}
