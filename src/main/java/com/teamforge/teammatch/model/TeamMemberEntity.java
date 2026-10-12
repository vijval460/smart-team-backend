package com.teamforge.teammatch.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team_members")
public class TeamMemberEntity {
    @Id
    @Column(length = 32)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String department;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "team_member_skills", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "skill", nullable = false)
    private List<String> skills = new ArrayList<>();

    @Column(name = "experience_years", nullable = false)
    private int experienceYears;

    @Column(nullable = false)
    private String availability;

    @Column(nullable = false)
    private String color;

    protected TeamMemberEntity() {}

    private TeamMemberEntity(TeamMember member) {
        id = member.id();
        name = member.name();
        role = member.role();
        department = member.department();
        skills = new ArrayList<>(member.skills());
        experienceYears = member.experienceYears();
        availability = member.availability();
        color = member.color();
    }

    public static TeamMemberEntity fromModel(TeamMember member) {
        return new TeamMemberEntity(member);
    }

    public TeamMember toModel() {
        return new TeamMember(id, name, role, department, List.copyOf(skills), experienceYears, availability, color);
    }
}