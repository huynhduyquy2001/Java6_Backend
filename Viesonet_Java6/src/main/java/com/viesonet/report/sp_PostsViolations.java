package com.viesonet.report;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import com.viesonet.entity.ListAcc;
import com.viesonet.entity.PostsViolationsAndUser;
import com.viesonet.entity.TopPostLike;


public interface sp_PostsViolations extends JpaRepository<PostsViolationsAndUser, String> {
 
    @Procedure("sp_PostsViolations")
    List<PostsViolationsAndUser> executePostsViolations();
}