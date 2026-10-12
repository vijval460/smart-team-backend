package com.teamforge.teammatch.model;

import java.util.List;

public record TeamMember(
        String id,
        String name,
        String role,
        String department,
        List<String> skills,
        int experienceYears,
        String availability,
        String color
) {}
