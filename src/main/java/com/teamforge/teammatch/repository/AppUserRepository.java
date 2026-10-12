package com.teamforge.teammatch.repository;

import com.teamforge.teammatch.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {}