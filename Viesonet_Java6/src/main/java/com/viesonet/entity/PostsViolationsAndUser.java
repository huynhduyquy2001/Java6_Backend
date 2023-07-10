package com.viesonet.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostsViolationsAndUser {
	@Id
	private int postId;
	private String avatar;
	private String username;
	@Temporal(TemporalType.TIMESTAMP)
	private Date postDate;
	private String allImages;
	private String content;
	private int likeCount;
	private int commentCount;
}
